package com.example.se1829_prm392_kindergartenms;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;

public class TeacherClassManagerActivity extends AppCompatActivity {
    private LinearLayout classListContainer;
    private String teacherId;
    private SqlDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_class_management);

        Button btnAddTeacher = findViewById(R.id.btnAddTeacher);
        btnAddTeacher.setOnClickListener(v -> {
            Intent intent = new Intent(TeacherClassManagerActivity.this, AddTeacherActivity.class);
            startActivity(intent);
        });

        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnAddClass = findViewById(R.id.btnAddClass);
        classListContainer = findViewById(R.id.classListContainer);
        dbHelper = new SqlDatabaseHelper(this);

        Button buttonAddSchedule = findViewById(R.id.btnAddSchedule);

        Button btnAddScheduleDetail = findViewById(R.id.btnAddScheduleDetail);
        btnAddScheduleDetail.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddScheduleToClassActivity.class);
            startActivity(intent);
        });


        teacherId = getIntent().getStringExtra("teacherId");

        btnAddClass.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeacherAddClassManagerActivity.class);
            intent.putExtra("teacherId", teacherId);
            startActivity(intent);
        });

        buttonAddSchedule.setOnClickListener((v) -> {
            Intent intent = new Intent(this, PrincipalAddSchedule.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
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
            final String classId = cursor.getString(0);
            final String className = cursor.getString(1);
            String teacherFullName = cursor.getString(2);
            String teacherShortName = getShortName(teacherFullName);

            CardView cardView = new CardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, dpToPx(12));
            cardView.setLayoutParams(cardParams);
            cardView.setRadius(dpToPx(10));
            cardView.setCardElevation(dpToPx(2));
            cardView.setCardBackgroundColor(Color.WHITE);
            cardView.setUseCompatPadding(true);

            LinearLayout rowContent = new LinearLayout(this);
            rowContent.setOrientation(LinearLayout.HORIZONTAL);
            rowContent.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
            rowContent.setGravity(Gravity.CENTER_VERTICAL);
            cardView.addView(rowContent);

            LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2);
            LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3);
            LinearLayout.LayoutParams actionContainerParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2);

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            buttonParams.gravity = Gravity.CENTER_HORIZONTAL;
            buttonParams.setMargins(0, 0, 0, dpToPx(4));

            TextView tvSTT = new TextView(this);
            tvSTT.setText(String.valueOf(stt));
            tvSTT.setTextColor(Color.parseColor("#333333"));
            tvSTT.setTextSize(14);
            tvSTT.setGravity(Gravity.CENTER);
            tvSTT.setLayoutParams(p1);

            TextView tvClassName = new TextView(this);
            tvClassName.setText(className);
            tvClassName.setTextColor(Color.parseColor("#424242"));
            tvClassName.setTextSize(14);
            tvClassName.setTypeface(null, Typeface.BOLD);
            tvClassName.setGravity(Gravity.CENTER);
            tvClassName.setLayoutParams(p2);

            TextView tvTeacher = new TextView(this);
            tvTeacher.setText(getString(R.string.teacher_short_name_format, teacherShortName));
            tvTeacher.setTextColor(Color.parseColor("#424242"));
            tvTeacher.setTextSize(14);
            tvTeacher.setGravity(Gravity.CENTER);
            tvTeacher.setLayoutParams(p3);

            LinearLayout actionButtonsContainer = new LinearLayout(this);
            actionButtonsContainer.setOrientation(LinearLayout.VERTICAL);
            actionButtonsContainer.setGravity(Gravity.CENTER_HORIZONTAL);
            actionButtonsContainer.setLayoutParams(actionContainerParams);

            // Nút "Xem"
            Button btnViewDetail = new Button(this);
            btnViewDetail.setText(getString(R.string.button_view_detail));
            btnViewDetail.setTextSize(10);
            btnViewDetail.setTextColor(Color.WHITE);
            // Đặt màu xanh lá cây cho nút "Xem"
            btnViewDetail.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            btnViewDetail.setPadding(dpToPx(10), dpToPx(6), dpToPx(10), dpToPx(6));
            btnViewDetail.setMinimumHeight(0);
            btnViewDetail.setMinimumWidth(0);
            btnViewDetail.setLayoutParams(buttonParams); // Thêm margin bottom cho nút Xem
            btnViewDetail.setOnClickListener(view -> {
                Intent intent = new Intent(this, ClassStudentListActivity.class);
                intent.putExtra("classId", classId);
                intent.putExtra("className", className);
                startActivity(intent);
            });
            actionButtonsContainer.addView(btnViewDetail);

            // Nút "Sửa"
            Button btnEdit = new Button(this);
            btnEdit.setText(getString(R.string.button_edit_class));
            btnEdit.setTextSize(10);
            btnEdit.setTextColor(Color.WHITE);
            // Đặt màu xanh lá cây cho nút "Sửa" giống nút "Xem"
            btnEdit.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            btnEdit.setPadding(dpToPx(10), dpToPx(6), dpToPx(10), dpToPx(6));
            btnEdit.setMinimumHeight(0);
            btnEdit.setMinimumWidth(0);
            // Nút cuối cùng không cần margin bottom
            LinearLayout.LayoutParams lastButtonParams = new LinearLayout.LayoutParams(buttonParams);
            lastButtonParams.setMargins(0, 0, 0, 0); // Loại bỏ margin bottom cho nút cuối cùng
            btnEdit.setLayoutParams(lastButtonParams);
            btnEdit.setOnClickListener(view -> {
                Intent intent = new Intent(this, TeacherEditClassManagerActivity.class);
                intent.putExtra("classId", classId);
                startActivity(intent);
            });
            actionButtonsContainer.addView(btnEdit);


            rowContent.addView(tvSTT);
            rowContent.addView(tvClassName);
            rowContent.addView(tvTeacher);
            rowContent.addView(actionButtonsContainer);

            classListContainer.addView(cardView);
            stt++;
        }

        cursor.close();
        db.close();
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private String getShortName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "";
        String[] parts = fullName.trim().split(" ");
        return parts[parts.length - 1];
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClassList();
    }
}