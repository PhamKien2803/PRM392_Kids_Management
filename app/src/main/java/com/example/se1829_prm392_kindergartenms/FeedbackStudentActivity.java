package com.example.se1829_prm392_kindergartenms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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
        students.clear();

        isToday = selectedDate.equals(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

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
            loadStudents();
        } else {
            tvToday.setText(isToday ? "Hôm nay không có lịch học." : "Không có lịch học ngày " + selectedDate);
            lvStudentList.setVisibility(View.GONE);
            if (lvStudentList.getAdapter() != null) {
                ((StudentAdapter) lvStudentList.getAdapter()).notifyDataSetInvalidated();
            }
        }
    }

    private void loadStudents() {
        students.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT studentId, fullName FROM students WHERE classId = ?", new String[]{classId});
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String history = getLatestFeedback(id);
                students.add(new StudentModel(id, name, history));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

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

            Feedback existingFeedbackForSelectedDate = feedbackDao.getFeedbackByStudentIdAndDate(student.id, selectedDate);

            boolean canInteract = isToday && hasScheduleToday;

            if (existingFeedbackForSelectedDate != null) {
                edtFeedback.setText(existingFeedbackForSelectedDate.getContent());
                btnSubmit.setText("Cập nhật");
                edtFeedback.setEnabled(canInteract);
                btnSubmit.setVisibility(canInteract ? View.VISIBLE : View.GONE);
            } else {
                edtFeedback.setText("");
                btnSubmit.setText("Gửi");
                edtFeedback.setEnabled(canInteract);
                btnSubmit.setVisibility(canInteract ? View.VISIBLE : View.GONE);
            }

            if (!canInteract) {
                edtFeedback.setEnabled(false);
                btnSubmit.setVisibility(View.GONE);
            }

            edtFeedback.setFocusableInTouchMode(canInteract);
            edtFeedback.setFocusable(canInteract);

            btnSubmit.setOnClickListener(view -> {
                String content = edtFeedback.getText().toString().trim();
                if (content.isEmpty()) {
                    showNotification("Thiếu nội dung", "Chưa nhập nội dung");
                    return;
                }

                Feedback currentFeedbackForSelectedDate = feedbackDao.getFeedbackByStudentIdAndDate(student.id, selectedDate);
                boolean success;

                if (currentFeedbackForSelectedDate != null) {
                    success = feedbackDao.updateFeedback(currentFeedbackForSelectedDate.getFeedbackId(), content);
                    if (success) {
                        showNotification("Thành công", "Đã cập nhật feedback");
                    } else {
                        showNotification("Lỗi", "Lỗi khi cập nhật feedback");
                    }
                } else {
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
                        showNotification("Thành công", "Đã gửi feedback");
                    } else {
                        showNotification("Lỗi", "Lỗi khi gửi feedback");
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

    private void showNotification(String title, String message) {
        String channelId = "feedback_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Thông báo feedback học sinh",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}