package com.example.se1829_prm392_kindergartenms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se1829_prm392_kindergartenms.DAO.ClassDao;
import com.example.se1829_prm392_kindergartenms.DAO.TeacherDao;
import com.example.se1829_prm392_kindergartenms.Entity.Class;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherAddClassManagerActivity extends AppCompatActivity {
    private Button btnBack, btnAddClass;
    private EditText edtClassName;
    private Spinner spinnerTeacher;
    private List<Teacher> teacherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_add_class);

        btnBack = findViewById(R.id.btnBack);
        btnAddClass = findViewById(R.id.btnAddClass);
        edtClassName = findViewById(R.id.edtClassName);
        spinnerTeacher = findViewById(R.id.spinnerTeacher);

        btnBack.setOnClickListener(v -> finish());

        // Lấy danh sách giáo viên từ DB
        TeacherDao teacherDao = new TeacherDao(this);
        teacherList = teacherDao.getAll();
        List<String> teacherNames = new ArrayList<>();
        for (Teacher t : teacherList) {
            teacherNames.add(t.getFullName() + " (" + t.getTeacherId() + ")");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teacherNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeacher.setAdapter(adapter);

        btnAddClass.setOnClickListener(v -> {
            String className = edtClassName.getText().toString().trim();
            int selectedPosition = spinnerTeacher.getSelectedItemPosition();
            if (className.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên lớp!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (teacherList == null || teacherList.isEmpty() || selectedPosition < 0) {
                Toast.makeText(this, "Vui lòng chọn giáo viên!", Toast.LENGTH_SHORT).show();
                return;
            }
            Teacher selectedTeacher = teacherList.get(selectedPosition);
            // Tạo đối tượng Class
            Class classroom = new Class();
            classroom.setClassId("CL" + System.currentTimeMillis());
            classroom.setClassName(className);
            classroom.setTeacherId(selectedTeacher);
            classroom.setScheduleId(null);
            classroom.setSchoolYear("2024-2025"); // hoặc giá trị mặc định
            ClassDao classDao = new ClassDao(this);
            long result = classDao.insert(classroom);
            if (result != -1) {
                Toast.makeText(this, "Thêm lớp thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, TeacherClassManagerActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Thêm lớp thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}