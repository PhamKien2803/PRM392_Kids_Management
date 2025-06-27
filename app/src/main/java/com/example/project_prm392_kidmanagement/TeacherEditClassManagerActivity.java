package com.example.project_prm392_kidmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392_kidmanagement.DAO.ClassDao;
import com.example.project_prm392_kidmanagement.DAO.TeacherDao;
import com.example.project_prm392_kidmanagement.Entity.Class;
import com.example.project_prm392_kidmanagement.Entity.Teacher;

import java.util.List;

public class TeacherEditClassManagerActivity extends AppCompatActivity {
    private Button btnBack, btnSaveChanges, btnDelete;

    private EditText edtEditClassName, edtEditTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_edit_class);
        String classId = getIntent().getStringExtra("classId");

        ClassDao classDao = new ClassDao(this);
        TeacherDao teacherDao = new TeacherDao(this);
        Class class1 = classDao.getById(classId);
        Teacher teacher1 = class1.getTeacherId();
        // Ánh xạ
        btnBack = findViewById(R.id.btnBack);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnDelete = findViewById(R.id.btnDelete);

        edtEditClassName = findViewById(R.id.edtEditClassName);
        edtEditTeacher = findViewById(R.id.edtEditTeacher);

        // Lấy classId từ Intent
        if (class1 != null) {
            edtEditClassName.setText(class1.getClassName());
            edtEditTeacher.setText(teacher1.getFullName());
        }


        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeacherClassManagerActivity.class);
            startActivity(intent);
        });

        btnSaveChanges.setOnClickListener(v -> {
            String newClassName = edtEditClassName.getText().toString().trim();
            String newTeacherName = edtEditTeacher.getText().toString().trim();

            if (newClassName.isEmpty() || newTeacherName.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Kiểm tra giáo viên tồn tại
            List<Teacher> allTeachers = teacherDao.getAll();
            Teacher foundTeacher = null;
            for (Teacher t : allTeachers) {
                if (t.getFullName().equalsIgnoreCase(newTeacherName)) {
                    foundTeacher = t;
                    break;
                }
            }

            if (foundTeacher == null) {
                Toast.makeText(this, "Không tìm thấy giáo viên tên \"" + newTeacherName + "\"", Toast.LENGTH_LONG).show();
                return;
            }

            // 2. Kiểm tra giáo viên đang dạy lớp khác (không phải lớp này)
            List<Class> allClasses = classDao.getAll();
            for (Class c : allClasses) {
                if (c.getTeacherId() != null &&
                        c.getTeacherId().getTeacherId().equals(foundTeacher.getTeacherId()) &&
                        !c.getClassId().equals(class1.getClassId())) {
                    Toast.makeText(this, "Giáo viên này đã được phân công dạy lớp khác", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            // 3. Cập nhật thông tin
            class1.setClassName(newClassName);
            class1.setTeacherId(foundTeacher);

            boolean updated = classDao.update(class1);
            if (updated) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, TeacherClassManagerActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Có lỗi xảy ra khi cập nhật", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            boolean deleted = classDao.delete(class1.getClassId());
            if (deleted) {
                Toast.makeText(this, "Xóa lớp học thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, TeacherClassManagerActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Không thể xóa lớp học", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
