package com.example.project_prm392_kidmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherViewTimeTableManagerActivity extends AppCompatActivity {

    private Button btnPrevDay, btnNextDay, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_timetable_view);

        btnPrevDay = findViewById(R.id.btnPrevDay);
        btnNextDay = findViewById(R.id.btnNextDay);
        btnBack = findViewById(R.id.btnBack);

        btnPrevDay.setOnClickListener(v -> {
            Toast.makeText(this, "📅 Đang hiển thị hôm trước ", Toast.LENGTH_SHORT).show();
        });

        btnNextDay.setOnClickListener(v -> {
            Toast.makeText(this, "📅 Đang hiển thị hôm sau ", Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}
