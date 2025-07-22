package com.example.se1829_prm392_kindergartenms;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se1829_prm392_kindergartenms.DAO.ClassDao;
import com.example.se1829_prm392_kindergartenms.DAO.ParentDao;
import com.example.se1829_prm392_kindergartenms.DAO.StudentDao;
import com.example.se1829_prm392_kindergartenms.DAO.TeacherDao;
import com.example.se1829_prm392_kindergartenms.Entity.Class;
import com.example.se1829_prm392_kindergartenms.Entity.Student;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;

import java.util.List;

public class ParentFeedbackActivity extends AppCompatActivity {

    private TextView tvStudentName, tvClassInfo, tvTeacherName, btnBack;

    private ParentDao parentDao;
    private StudentDao studentDao;
    private ClassDao classDao;
    private TeacherDao teacherDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_feedback);

        String parentId = getIntent().getStringExtra("parentId");
        if (parentId == null) {
            Toast.makeText(this, "Không tìm thấy mã phụ huynh", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        tvStudentName = findViewById(R.id.tvStudentName);
        tvClassInfo = findViewById(R.id.tvClassInfo);
        tvTeacherName = findViewById(R.id.tvTeacherName);
        btnBack = findViewById(R.id.btnBack);

        parentDao = new ParentDao(this);
        studentDao = new StudentDao(this);
        classDao = new ClassDao(this);
        teacherDao = new TeacherDao(this);

        loadStudentInfo(parentId);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadStudentInfo(String parentId) {
        List<Student> studentList = studentDao.getStudentsByParentId(parentId);

        if (!studentList.isEmpty()) {
            Student student = studentList.get(0);
            tvStudentName.setText("🧒 Bé: " + student.getFullName());

            Class classroom = classDao.getById(student.getClassId().getClassId());
            if (classroom != null) {
                tvClassInfo.setText("📚 Lớp: " + classroom.getClassName() + " - Năm học: " + classroom.getSchoolYear());

                Teacher teacher = teacherDao.getById(classroom.getTeacherId().getTeacherId());
                if (teacher != null) {
                    tvTeacherName.setText("👩‍🏫 Giáo viên: " + teacher.getFullName());
                }
            }
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin học sinh", Toast.LENGTH_SHORT).show();
        }
    }
}