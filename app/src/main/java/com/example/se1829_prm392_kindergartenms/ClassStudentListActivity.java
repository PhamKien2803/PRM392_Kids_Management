package com.example.se1829_prm392_kindergartenms;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;

public class ClassStudentListActivity extends AppCompatActivity {

    private static final String TAG = "ClassStudentList";
    private LinearLayout studentListLayout;
    private SqlDatabaseHelper dbHelper;
    private String classId, className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_student_list);

        // Initialize views
        studentListLayout = findViewById(R.id.studentListLayout);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        dbHelper = new SqlDatabaseHelper(this);

        // Get data from intent
        classId = getIntent().getStringExtra("classId");
        className = getIntent().getStringExtra("className");

        if (classId == null || className == null) {
            showToast("Missing class information");
            finish();
            return;
        }

        TextView tvClassName = findViewById(R.id.tvClassName);
        tvClassName.setText("Class: " + className);

        loadStudentList();
    }

    private void loadStudentList() {
        studentListLayout.removeAllViews();

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(
                     "SELECT s.studentId, s.fullName, s.dob, p.fullName " +
                             "FROM students s " +
                             "LEFT JOIN parents p ON s.parentId = p.parentId " +
                             "WHERE s.classId = ?",
                     new String[]{classId})) {

            if (cursor.getCount() == 0) {
                showEmptyMessage();
            } else {
                int stt = 1;
                while (cursor.moveToNext()) {
                    addStudentRow(
                            stt++,
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.isNull(3) ? "No parent info" : cursor.getString(3)
                    );
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading students", e);
            showToast("Error loading student list");
        }
    }

    private void addStudentRow(int stt, String studentId, String studentName, String dob, String parentName) {
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
//        rowLayout.setBackgroundResource(R.drawable.bg_student_row);
        rowLayout.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12));
        rowLayout.setGravity(Gravity.CENTER_VERTICAL);

        // STT Column
        TextView tvStt = new TextView(this);
        tvStt.setText(String.valueOf(stt));
        tvStt.setTextColor(Color.BLACK);
        tvStt.setTextSize(16);
        tvStt.setWidth(dpToPx(40));
        rowLayout.addView(tvStt);

        // Student Info Column
        LinearLayout infoLayout = new LinearLayout(this);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        infoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1));
        infoLayout.setPadding(dpToPx(16), 0, dpToPx(16), 0);

        TextView tvName = new TextView(this);
        tvName.setText(studentName);
        tvName.setTextColor(Color.BLACK);
        tvName.setTextSize(16);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        infoLayout.addView(tvName);

        TextView tvDetails = new TextView(this);
        tvDetails.setText(String.format("DOB: %s | Parent: %s", dob, parentName));
        tvDetails.setTextColor(Color.DKGRAY);
        tvDetails.setTextSize(14);
        infoLayout.addView(tvDetails);

        rowLayout.addView(infoLayout);

        // Detail Button
        Button btnDetail = new Button(this);
        btnDetail.setText("Chi Tiết");
        btnDetail.setBackgroundColor(Color.parseColor("#2196F3"));
        btnDetail.setTextColor(Color.WHITE);
        btnDetail.setOnClickListener(v -> openStudentDetail(studentId));

        rowLayout.addView(btnDetail);
        studentListLayout.addView(rowLayout);
    }

    private void openStudentDetail(String studentId) {
        Intent intent = new Intent(this, StudentDetailWithParentActivity.class);
        intent.putExtra("studentId", studentId);
        startActivity(intent);
    }

    private void showEmptyMessage() {
        TextView tvEmpty = new TextView(this);
        tvEmpty.setText("Không có học sinh ở lớp này");
        tvEmpty.setGravity(Gravity.CENTER);
        tvEmpty.setTextSize(18);
        studentListLayout.addView(tvEmpty);
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudentList();
    }
}