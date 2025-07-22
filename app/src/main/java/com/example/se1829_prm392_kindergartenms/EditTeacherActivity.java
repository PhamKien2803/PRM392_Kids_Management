package com.example.se1829_prm392_kindergartenms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;

public class EditTeacherActivity extends AppCompatActivity {

    private EditText etTeacherCode, etFullName, etAddress, etPhone, etDob;
    private Button btnUpdateTeacher, btnBack;
    private SqlDatabaseHelper dbHelper;
    private String teacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teacher);

        // Ánh xạ view
        etTeacherCode = findViewById(R.id.etTeacherCode);
        etFullName = findViewById(R.id.etFullName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        etDob = findViewById(R.id.etDob);
        btnUpdateTeacher = findViewById(R.id.btnUpdateTeacher);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new SqlDatabaseHelper(this);

        // Nhận teacherId từ Intent
        Intent intent = getIntent();
        teacherId = intent.getStringExtra("teacherId");

        if (teacherId != null) {
            loadTeacherInfo(teacherId);
        } else {
            Toast.makeText(this, "Không tìm thấy mã giáo viên", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnUpdateTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTeacherInfo();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại
            }
        });
    }

    private void loadTeacherInfo(String id) {
        Teacher teacher = dbHelper.getTeacherById(id);
        if (teacher != null) {
            etTeacherCode.setText(teacher.getTeacherId());
            etFullName.setText(teacher.getFullName());
            etAddress.setText(teacher.getAddress());
            etPhone.setText(teacher.getPhone());
            etDob.setText(teacher.getDob());
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin giáo viên", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateTeacherInfo() {
        String fullName = etFullName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dob = etDob.getText().toString().trim();

        if (fullName.isEmpty() ) {
            Toast.makeText(this, "Vui lòng nhập tên giáo viên", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.updateTeacher(new Teacher(teacherId, fullName, address, phone, dob));

        if (success) {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại màn hình trước
        } else {
            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}

