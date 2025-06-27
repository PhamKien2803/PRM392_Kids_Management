package com.example.project_prm392_kidmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392_kidmanagement.DAO.TeacherDao;

import java.util.List;

public class TeacherViewTimeTableManagerActivity extends AppCompatActivity {

    private Button btnPrevDay, btnNextDay, btnBack;
    private TextView tvDate, tvTeacherName;
    private LinearLayout llScheduleList;

    private TeacherDao teacherDao;
    private String teacherId = "GV001"; // mẫu ID
    private String classId = "CL01";    // mẫu class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_timetable_view);

        btnPrevDay = findViewById(R.id.btnPrevDay);
        btnNextDay = findViewById(R.id.btnNextDay);
        btnBack = findViewById(R.id.btnBack);
        tvDate = findViewById(R.id.tvDate);
        tvTeacherName = findViewById(R.id.tvTeacherName);
        llScheduleList = findViewById(R.id.llScheduleList);

        teacherDao = new TeacherDao(this);

        loadSchedules();

        btnPrevDay.setOnClickListener(v -> Toast.makeText(this, "Hôm trước ⏪", Toast.LENGTH_SHORT).show());
        btnNextDay.setOnClickListener(v -> Toast.makeText(this, "⏩ Hôm sau", Toast.LENGTH_SHORT).show());
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadSchedules() {
        tvTeacherName.setText("👩‍🏫 Giáo viên: " + teacherId);
        tvDate.setText("📘 Lớp: " + classId);

        List<String> schedules = teacherDao.getTeacherSchedulesInClass(teacherId, classId);

        llScheduleList.removeAllViews();

        for (String line : schedules) {
            TextView tv = new TextView(this);
            tv.setText(line);
            tv.setPadding(32, 24, 32, 24);
            tv.setTextSize(15);
            tv.setTextColor(0xFF212121);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 16, 0, 0);
            tv.setLayoutParams(params);

            llScheduleList.addView(tv);
        }
    }
}
