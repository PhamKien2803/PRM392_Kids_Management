package com.example.se1829_prm392_kindergartenms;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se1829_prm392_kindergartenms.DAO.TeacherDao;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;
import com.example.se1829_prm392_kindergartenms.R;

public class AddTeacherActivity extends AppCompatActivity {
    private EditText edtFullName, edtAddress, edtPhone, edtDob;
    private Button btnSaveTeacher;
    private TeacherDao teacherDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()); // Quay lại màn hình trước

        Button btnViewTeachers = findViewById(R.id.btnViewTeachers);
        btnViewTeachers.setOnClickListener(v -> {
            Intent intent = new Intent(AddTeacherActivity.this, TeacherListActivity.class);
            startActivity(intent);
        });

        // Ánh xạ view (bỏ edtTeacherId)
        edtFullName = findViewById(R.id.edtFullName);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
        edtDob = findViewById(R.id.edtDob);
        btnSaveTeacher = findViewById(R.id.btnSaveTeacher);

        teacherDao = new TeacherDao(this);

        btnSaveTeacher.setOnClickListener(v -> saveTeacher());
    }

    private void saveTeacher() {
        String name = edtFullName.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Vui lòng nhập họ tên giáo viên", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo ID tự động
        String id = teacherDao.generateAutoTeacherId();
        Teacher teacher = new Teacher(id, name, address, phone, dob);

        long result = teacherDao.insert(teacher);
        if (result != -1) {
            Toast.makeText(this, "Thêm giáo viên thành công! Mã: " + id, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm giáo viên thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
