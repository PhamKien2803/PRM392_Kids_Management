package com.example.project_prm392_kidmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherEditClassManagerActivity extends AppCompatActivity {
    private Button btnBack;
    private TextView tvClassIdDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_edit_class);

        // Ánh xạ
        btnBack = findViewById(R.id.btnBack);
        tvClassIdDisplay = findViewById(R.id.tvClassIdDisplay);

        // Lấy classId từ Intent
        String classId = getIntent().getStringExtra("classId");

        if (classId != null) {
            tvClassIdDisplay.setText("Mã lớp: " + classId);
        } else {
            tvClassIdDisplay.setText("Không tìm thấy mã lớp");
        }

        btnBack.setOnClickListener(v -> finish());
    }
}
