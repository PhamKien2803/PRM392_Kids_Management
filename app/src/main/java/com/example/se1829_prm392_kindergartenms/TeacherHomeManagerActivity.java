package com.example.se1829_prm392_kindergartenms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.se1829_prm392_kindergartenms.DAO.TeacherDao;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;
import com.example.se1829_prm392_kindergartenms.DAO.ClassDao;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherHomeManagerActivity extends AppCompatActivity {

    private TextView tvTeacherName, tvTeacherClass;
    private Button btnViewTimetable, btnViewlistStudent, btnLogout;
    private TeacherDao teacherDao;
    private Button btnFeedback;

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
        btnViewlistStudent = findViewById(R.id.btnViewlistStudent);
        btnLogout = findViewById(R.id.btnLogout);
        btnFeedback = findViewById(R.id.btnFeedback);

        if (teacher != null) {
            tvTeacherName.setText("👩‍🏫 Giáo viên: " + teacher.getFullName());
            tvTeacherClass.setText("📚 Lớp phụ trách: Lá 2");
        } else {
            tvTeacherName.setText("👩‍🏫 Giáo viên: Không rõ");
            tvTeacherClass.setText("📚 Lớp phụ trách: -");
        }

        // Xem thời khóa biểu
        btnViewTimetable.setOnClickListener(view -> {
            Intent intent = new Intent(this, TeacherViewTimeTableManagerActivity.class);
            intent.putExtra("teacherId", teacherId);
            startActivity(intent);
        });

        // Xem danh sách học sinh
        btnViewlistStudent.setOnClickListener(view -> {
            Intent intent = new Intent(this, TeacherStudentListActivity.class);
            intent.putExtra("teacherId", teacherId);
            startActivity(intent);
        });
        btnFeedback.setOnClickListener(view -> {
            ClassDao classDao = new ClassDao(this);
            String classId = classDao.getClassIdByTeacherId(teacherId);

            if (classId != null) {
                Intent intent = new Intent(this, FeedbackStudentActivity.class);
                intent.putExtra("teacherId", teacherId);
                intent.putExtra("classId", classId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Không tìm thấy lớp của giáo viên", Toast.LENGTH_SHORT).show();
            }
        });



        // Đăng xuất
        btnLogout.setOnClickListener(view -> {
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

    }
}