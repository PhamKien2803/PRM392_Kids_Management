package com.example.se1829_prm392_kindergartenms;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.se1829_prm392_kindergartenms.DAO.TeacherDao;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TeacherViewTimeTableManagerActivity extends AppCompatActivity {

    private Button btnBack, btnPickDate;
    private TextView tvHeaderTitle, tvHeaderSubtitle;
    private LinearLayout llScheduleList;

    private TeacherDao teacherDao;
    private String teacherId;
    private String teacherName;
    private String dayOfWeek;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_timetable_view);

        // Ánh xạ View
        btnBack = findViewById(R.id.btnBack);
        btnPickDate = findViewById(R.id.btnPickDate);
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        tvHeaderSubtitle = findViewById(R.id.tvHeaderSubtitle);
        llScheduleList = findViewById(R.id.llScheduleList);

        // Khởi tạo DAO
        teacherDao = new TeacherDao(this);

        // Lấy teacherId từ Intent
        Intent intent = getIntent();
        teacherId = intent.getStringExtra("teacherId");

        // Lấy ngày hiện tại
        Calendar current = Calendar.getInstance();
        updateDateInfo(current.getTime());

        // Truy vấn tên giáo viên từ DB
        Teacher teacher = teacherDao.getById(teacherId);
        teacherName = teacher != null ? teacher.getFullName() : "Không rõ tên";

        // Tải dữ liệu
        loadSchedules();

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Xử lý chọn ngày
        btnPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        updateDateInfo(selectedDate.getTime());
                        loadSchedules();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void updateDateInfo(Date selectedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("vi"));
        date = dateFormat.format(selectedDate);
        dayOfWeek = capitalizeFirstLetter(dayFormat.format(selectedDate));
    }

    private void loadSchedules() {
        // Header
        tvHeaderTitle.setText("📅 Lịch dạy hôm nay");
        tvHeaderSubtitle.setText("👩‍🏫 " + teacherName + " | " + dayOfWeek + " – " + date);

        // Lấy danh sách tiết học từ DB
        List<String> schedules = teacherDao.getTeacherSchedulesInClass(teacherId, "CL01", date);

        llScheduleList.removeAllViews();

        for (int i = 0; i < schedules.size(); i++) {
            String[] info = schedules.get(i).split("\\|");
            String lop = info.length > 0 ? info[0] : "Lớp chưa rõ";
            String mon = info.length > 1 ? info[1] : "Môn chưa rõ";
            String bai = info.length > 2 ? info[2] : "Bài chưa rõ";
            String start = info.length > 3 ? info[3] : "--";
            String end = info.length > 4 ? info[4] : "--";

            CardView card = new CardView(this);
            card.setCardElevation(2);
            card.setRadius(12);
            card.setUseCompatPadding(true);

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 24);
            card.setLayoutParams(cardParams);

            LinearLayout innerLayout = new LinearLayout(this);
            innerLayout.setOrientation(LinearLayout.VERTICAL);
            innerLayout.setPadding(32, 24, 32, 24);

            TextView tvLop = new TextView(this);
            tvLop.setText("Tiết " + (i + 1) + " – " + lop + " (" + start + " - " + end + ")");
            tvLop.setTextSize(16);
            tvLop.setTextColor(0xFF0D47A1);
            tvLop.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView tvMon = new TextView(this);
            tvMon.setText("Môn: " + mon + " – Bài: " + bai);
            tvMon.setTextSize(14);
            tvMon.setTextColor(0xFF333333);
            tvMon.setPadding(0, 8, 0, 0);

            innerLayout.addView(tvLop);
            innerLayout.addView(tvMon);

            card.addView(innerLayout);
            llScheduleList.addView(card);
        }

        if (schedules.isEmpty()) {
            TextView tvEmpty = new TextView(this);
            tvEmpty.setText("Không có lịch dạy nào cho ngày " + date);
            tvEmpty.setTextSize(15);
            tvEmpty.setTextColor(0xFF757575);
            tvEmpty.setGravity(Gravity.CENTER);
            llScheduleList.addView(tvEmpty);
        }
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
