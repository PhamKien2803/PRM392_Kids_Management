package com.example.project_prm392_kidmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_prm392_kidmanagement.DAO.TeacherDao;
import com.example.project_prm392_kidmanagement.Entity.Teacher;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherHomeManagerActivity extends AppCompatActivity {

    private TextView tvTeacherName, tvTeacherClass;
    private Button btnViewTimetable, btnManageSchedule, btnManageClass, btnLogout;

    private TeacherDao teacherDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_home);

        String teacherId = getIntent().getStringExtra("teacherId");
        if (teacherId == null) {
            Toast.makeText(this, "Không tìm thấy mã giáo viên", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        teacherDao = new TeacherDao(this);
        Teacher teacher = teacherDao.getById(teacherId);

        tvTeacherName = findViewById(R.id.tvTeacherName);
        tvTeacherClass = findViewById(R.id.tvTeacherClass);
        btnViewTimetable = findViewById(R.id.btnViewTimetable);
        btnManageSchedule = findViewById(R.id.btnManageSchedule);
//        btnManageClass = findViewById(R.id.btnManageClass);
        btnLogout = findViewById(R.id.btnLogout);

        if (teacher != null) {
            tvTeacherName.setText("👩‍🏫 Giáo viên: " + teacher.getFullName());
            tvTeacherClass.setText("📚 Lớp phụ trách: Lá 2");
        } else {
            tvTeacherName.setText("👩‍🏫 Giáo viên: Không rõ");
            tvTeacherClass.setText("📚 Lớp phụ trách: -");
        }

        btnViewTimetable.setOnClickListener(view -> {
            Intent intent = new Intent(this, TeacherViewTimeTableManagerActivity.class);
            intent.putExtra("teacherId", teacherId);
            startActivity(intent);
        });

        btnManageSchedule.setOnClickListener(view -> {
            Intent intent = new Intent(this, TeacherScheduleManagerActivity.class);
            intent.putExtra("teacherId", teacherId);
            startActivity(intent);
        });

//        btnManageClass.setOnClickListener(view -> {
//            Intent intent = new Intent(this, TeacherClassManagerActivity.class);
//            intent.putExtra("teacherId", teacherId);
//            startActivity(intent);
//        });


        btnLogout.setOnClickListener(view -> {
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AccountManagerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

    }
}
