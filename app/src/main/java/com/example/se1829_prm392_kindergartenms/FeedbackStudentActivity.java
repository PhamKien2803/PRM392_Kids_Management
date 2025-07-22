package com.example.se1829_prm392_kindergartenms;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se1829_prm392_kindergartenms.DAO.FeedbackDao;
import com.example.se1829_prm392_kindergartenms.DAO.ScheduleDao;
import com.example.se1829_prm392_kindergartenms.DAO.ScheduleToClassDao;
import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;
import com.example.se1829_prm392_kindergartenms.Entity.Feedback;
import com.example.se1829_prm392_kindergartenms.Entity.Schedule;

import java.text.SimpleDateFormat;
import java.util.*;

public class FeedbackStudentActivity extends AppCompatActivity {

    private ListView lvStudentList;
    private TextView tvToday;
    private DatePicker datePicker;
    private Button btnBack;

    private SqlDatabaseHelper dbHelper;
    private FeedbackDao feedbackDao;
    private ScheduleDao scheduleDao;
    private ScheduleToClassDao scheduleToClassDao;

    private String teacherId, classId, selectedDate;
    private List<StudentModel> students = new ArrayList<>();

    private boolean isToday, hasScheduleToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_student);

        teacherId = getIntent().getStringExtra("teacherId");
        classId = getIntent().getStringExtra("classId");

        lvStudentList = findViewById(R.id.lvStudentList);
        tvToday = findViewById(R.id.tvToday);
        datePicker = findViewById(R.id.datePicker);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new SqlDatabaseHelper(this);
        feedbackDao = new FeedbackDao(this);
        scheduleDao = new ScheduleDao(this);
        scheduleToClassDao = new ScheduleToClassDao(this);

        Calendar calendar = Calendar.getInstance();
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());

        checkScheduleAndLoad();

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth);
                    selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selected.getTime());
                    checkScheduleAndLoad();
                });

        btnBack.setOnClickListener(v -> finish());
    }

    private void checkScheduleAndLoad() {
        students.clear(); // Xóa danh sách học sinh cũ trước khi tải mới

        // Kiểm tra xem ngày được chọn có phải hôm nay không
        isToday = selectedDate.equals(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        // Kiểm tra xem có lịch học cho classId trong ngày đó không
        List<Schedule> schedules = scheduleDao.getSchedulesByClassId(classId);
        hasScheduleToday = false;

        for (Schedule s : schedules) {
            if (s.getTimeDate().equals(selectedDate)) {
                hasScheduleToday = true;
                break;
            }
        }

        if (hasScheduleToday) {
            tvToday.setText(isToday ? "Feedback cho lớp hôm nay" : "Xem lại feedback lớp ngày " + selectedDate);
            lvStudentList.setVisibility(View.VISIBLE);
            loadStudents(); // Tải danh sách học sinh và trạng thái feedback
        } else {
            tvToday.setText(isToday ? "Hôm nay không có lịch học." : "Không có lịch học ngày " + selectedDate);
            lvStudentList.setVisibility(View.GONE);
            // Nếu không có lịch học, xóa adapter để không hiển thị dữ liệu cũ
            if (lvStudentList.getAdapter() != null) {
                ((StudentAdapter) lvStudentList.getAdapter()).notifyDataSetInvalidated();
            }
        }
    }

    private void loadStudents() {
        students.clear(); // Xóa dữ liệu cũ trước khi tải lại

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT studentId, fullName FROM students WHERE classId = ?", new String[]{classId});
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String history = getLatestFeedback(id); // Lấy feedback gần nhất để hiển thị lịch sử
                students.add(new StudentModel(id, name, history));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Kiểm tra nếu adapter đã tồn tại để tái sử dụng, ngược lại tạo mới
        if (lvStudentList.getAdapter() == null) {
            StudentAdapter adapter = new StudentAdapter();
            lvStudentList.setAdapter(adapter);
        } else {
            ((StudentAdapter) lvStudentList.getAdapter()).notifyDataSetChanged();
        }
    }

    private String getLatestFeedback(String studentId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT content, date FROM feedback WHERE studentId = ? ORDER BY date DESC LIMIT 1", new String[]{studentId});
            if (cursor.moveToFirst()) {
                String content = cursor.getString(0);
                String date = cursor.getString(1);
                return date + ": " + content;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            // Không đóng db ở đây nếu nó được quản lý bởi SqlDatabaseHelper
        }
        return "Chưa có feedback nào.";
    }

    private class StudentModel {
        String id, name, lastFeedback;

        StudentModel(String id, String name, String lastFeedback) {
            this.id = id;
            this.name = name;
            this.lastFeedback = lastFeedback;
        }
    }

    private class StudentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return students.size();
        }

        @Override
        public Object getItem(int position) {
            return students.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StudentModel student = students.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(FeedbackStudentActivity.this).inflate(R.layout.item_student_feedback, parent, false);
            }

            TextView tvName = convertView.findViewById(R.id.tvStudentName);
            EditText edtFeedback = convertView.findViewById(R.id.edtFeedback);
            Button btnSubmit = convertView.findViewById(R.id.btnSubmitFeedback);
            TextView tvHistory = convertView.findViewById(R.id.tvHistory);

            tvName.setText(student.name);
            tvHistory.setText("Lịch sử gần nhất: " + student.lastFeedback);

            // Kiểm tra xem đã có feedback cho học sinh này vào ngày được chọn chưa
            Feedback existingFeedbackForSelectedDate = feedbackDao.getFeedbackByStudentIdAndDate(student.id, selectedDate);

            // Xác định xem có thể gửi/chỉnh sửa feedback hay không (chỉ hôm nay và có lịch)
            boolean canInteract = isToday && hasScheduleToday;

            if (existingFeedbackForSelectedDate != null) {
                // Đã có feedback, hiển thị nội dung và cho phép chỉnh sửa
                edtFeedback.setText(existingFeedbackForSelectedDate.getContent());
                btnSubmit.setText("Cập nhật");
                edtFeedback.setEnabled(canInteract);
                btnSubmit.setVisibility(canInteract ? View.VISIBLE : View.GONE); // Chỉ hiện nút nếu là hôm nay và có lịch
            } else {
                // Chưa có feedback, cho phép gửi mới
                edtFeedback.setText("");
                btnSubmit.setText("Gửi");
                edtFeedback.setEnabled(canInteract); // Chỉ cho nhập mới nếu là hôm nay và có lịch
                btnSubmit.setVisibility(canInteract ? View.VISIBLE : View.GONE); // Chỉ hiện nút nếu là hôm nay và có lịch
            }

            // Vô hiệu hóa EditText và Button nếu không phải hôm nay hoặc không có lịch học
            if (!canInteract) {
                edtFeedback.setEnabled(false);
                btnSubmit.setVisibility(View.GONE);
            }

            edtFeedback.setFocusableInTouchMode(canInteract);
            edtFeedback.setFocusable(canInteract);


            btnSubmit.setOnClickListener(view -> {
                String content = edtFeedback.getText().toString().trim();
                if (content.isEmpty()) {
                    Toast.makeText(FeedbackStudentActivity.this, "Chưa nhập nội dung", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lấy lại feedback hiện có ngay trước khi submit để đảm bảo trạng thái mới nhất
                Feedback currentFeedbackForSelectedDate = feedbackDao.getFeedbackByStudentIdAndDate(student.id, selectedDate);
                boolean success;

                if (currentFeedbackForSelectedDate != null) {
                    // Đã có feedback, thực hiện cập nhật
                    success = feedbackDao.updateFeedback(currentFeedbackForSelectedDate.getFeedbackId(), content);
                    if (success) {
                        Toast.makeText(FeedbackStudentActivity.this, "Đã cập nhật feedback", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FeedbackStudentActivity.this, "Lỗi khi cập nhật feedback", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Chưa có feedback, thực hiện thêm mới
                    Feedback feedback = new Feedback(
                            UUID.randomUUID().toString(),
                            teacherId,
                            student.id,
                            classId,
                            content,
                            selectedDate
                    );
                    success = feedbackDao.insertFeedback(feedback);
                    if (success) {
                        Toast.makeText(FeedbackStudentActivity.this, "Đã gửi feedback", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FeedbackStudentActivity.this, "Lỗi khi gửi feedback", Toast.LENGTH_SHORT).show();
                    }
                }

                if (success) {
                    student.lastFeedback = selectedDate + ": " + content;
                    tvHistory.setText("Lịch sử gần nhất: " + student.lastFeedback);

                    if (canInteract) {
                        btnSubmit.setText("Cập nhật");
                    }
                    ((StudentAdapter) lvStudentList.getAdapter()).notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }
}