package com.example.project_prm392_kidmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392_kidmanagement.DB.SqlDatabaseHelper;

public class TeacherClassManagerActivity extends AppCompatActivity {

    private Button btnLogout, btnAddClass;
    private LinearLayout classListContainer;
    private String teacherId;
    private SqlDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_class_management);

        // Ánh xạ
        btnLogout = findViewById(R.id.btnLogout);
        btnAddClass = findViewById(R.id.btnAddClass);
        classListContainer = findViewById(R.id.classListContainer);
        dbHelper = new SqlDatabaseHelper(this);

        // Lấy mã giáo viên
        teacherId = getIntent().getStringExtra("teacherId");

        // Nút thêm
        btnAddClass.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeacherAddClassManagerActivity.class);
            intent.putExtra("teacherId", teacherId);
            startActivity(intent);
        });

        // Nút đăng xuất
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccountManagerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Load danh sách lớp
        loadClassList();
    }

    private void loadClassList() {
        classListContainer.removeAllViews();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT c.classId, c.className, t.fullName " +
                "FROM classes c " +
                "JOIN teachers t ON c.teacherId = t.teacherId " +
                "ORDER BY c.classId ASC";

        Cursor cursor = db.rawQuery(sql, null);
        int stt = 1;

        while (cursor.moveToNext()) {
            String classId = cursor.getString(0);
            String className = cursor.getString(1);
            String teacherFullName = cursor.getString(2);
            String teacherShortName = getShortName(teacherFullName);

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(12, 12, 12, 12);
            row.setBackgroundColor(stt % 2 == 0 ? 0xFFF5F5F5 : 0xFFFFFFFF);

            // Params
            LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2);
            LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3);
            LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2);

            // STT
            TextView tvSTT = new TextView(this);
            tvSTT.setText(String.valueOf(stt));
            tvSTT.setTextColor(0xFF333333);
            tvSTT.setTextSize(14);
            tvSTT.setGravity(Gravity.CENTER_VERTICAL);
            tvSTT.setLayoutParams(p1);

            // Tên lớp
            TextView tvClassName = new TextView(this);
            tvClassName.setText(className);
            tvClassName.setTextColor(0xFF333333);
            tvClassName.setTextSize(14);
            tvClassName.setLayoutParams(p2);

            // Tên giáo viên
            TextView tvTeacher = new TextView(this);
            tvTeacher.setText("Cô " + teacherShortName);
            tvTeacher.setTextColor(0xFF333333);
            tvTeacher.setTextSize(14);
            tvTeacher.setLayoutParams(p3);

            // Button Sửa
            Button btnEdit = new Button(this);
            btnEdit.setText("Sửa");
            btnEdit.setTextSize(14);
            btnEdit.setTextColor(0xFFFFFFFF);
            btnEdit.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_dark));
            btnEdit.setLayoutParams(p4);
            btnEdit.setOnClickListener(view -> {
                Intent intent = new Intent(this, TeacherEditClassManagerActivity.class);
                intent.putExtra("classId", classId);
                startActivity(intent);
            });

            // Add vào dòng
            row.addView(tvSTT);
            row.addView(tvClassName);
            row.addView(tvTeacher);
            row.addView(btnEdit);

            // Thêm dòng vào danh sách
            classListContainer.addView(row);
            stt++;
        }

        cursor.close();
    }

    private String getShortName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "";
        String[] parts = fullName.trim().split(" ");
        return parts[parts.length - 1];
    }
}
