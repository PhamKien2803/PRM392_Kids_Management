package com.example.project_prm392_kidmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherClassManagerActivity extends AppCompatActivity {

    private Button btnLogout, btnAddClass, btnEditClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_class_management);

        btnLogout = findViewById(R.id.btnLogout);
        btnAddClass = findViewById(R.id.btnAddClass);
        btnEditClass = findViewById(R.id.btnEditClass);

        String teacherId = getIntent().getStringExtra("teacherId");
        if (teacherId != null) {
            Toast.makeText(this, "Mã giáo viên: " + teacherId, Toast.LENGTH_SHORT).show();
        }

        btnAddClass.setOnClickListener(view -> {
            Intent intent = new Intent(this, TeacherAddClassManagerActivity.class);
            intent.putExtra("teacherId", teacherId);
            startActivity(intent);
        });

        btnEditClass.setOnClickListener(view -> {
            Intent intent = new Intent(this, TeacherEditClassManagerActivity.class);
            intent.putExtra("teacherId", teacherId);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(view -> {
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AccountManagerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
