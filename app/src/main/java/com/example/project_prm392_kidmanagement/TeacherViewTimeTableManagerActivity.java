package com.example.project_prm392_kidmanagement;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.project_prm392_kidmanagement.DAO.TeacherDao;

import java.util.List;

public class TeacherViewTimeTableManagerActivity extends AppCompatActivity {

    private Button btnPrevDay, btnNextDay, btnBack;
    private TextView tvHeaderTitle, tvHeaderSubtitle;
    private LinearLayout llScheduleList;

    private TeacherDao teacherDao;
    private String teacherId = "GV001";
    private String teacherName = "Cô Trần Thị Mai";
    private String dayOfWeek = "Thứ 2";
    private String date = "17/06/2025";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_timetable_view);

        // Ánh xạ
//        btnPrevDay = findViewById(R.id.btnPrevDay);
//        btnNextDay = findViewById(R.id.btnNextDay);
        btnBack = findViewById(R.id.btnBack);
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        tvHeaderSubtitle = findViewById(R.id.tvHeaderSubtitle);
        llScheduleList = findViewById(R.id.llScheduleList);

        teacherDao = new TeacherDao(this);

        // Load data
        loadSchedules();

        // Listener
//        btnPrevDay.setOnClickListener(v -> Toast.makeText(this, "Hôm trước ⏪", Toast.LENGTH_SHORT).show());
//        btnNextDay.setOnClickListener(v -> Toast.makeText(this, "⏩ Hôm sau", Toast.LENGTH_SHORT).show());
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadSchedules() {
        // Header
        tvHeaderTitle.setText("📅 Lịch dạy hôm nay");
        tvHeaderSubtitle.setText("👩‍🏫 " + teacherName + " | " + dayOfWeek + " – " + date);

        // Get data từ DAO
        List<String> schedules = teacherDao.getTeacherSchedulesInClass(teacherId, "CL01");

        llScheduleList.removeAllViews();

        for (int i = 0; i < schedules.size(); i++) {
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

            if (i < schedules.size()) {
                String[] info = schedules.get(i).split("\\|");
                String lop = info.length > 0 ? info[0] : "Lớp chưa rõ";
                String mon = info.length > 1 ? info[1] : "Môn chưa rõ";
                String bai = info.length > 2 ? info[2] : "Bài chưa rõ";

                TextView tvLop = new TextView(this);
                tvLop.setText("Tiết " + (i + 1) + " – " + lop);
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
            } else {
                TextView tvEmpty = new TextView(this);
                tvEmpty.setText("Tiết " + (i + 1) + " – Chưa có thông tin");
                tvEmpty.setTextSize(14);
                tvEmpty.setTextColor(0xFF90A4AE);
                tvEmpty.setGravity(Gravity.CENTER_VERTICAL);
                innerLayout.addView(tvEmpty);
            }

            card.addView(innerLayout);
            llScheduleList.addView(card);
        }
    }
}
