package com.example.se1829_prm392_kindergartenms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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

        studentListLayout = findViewById(R.id.studentListLayout);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        dbHelper = new SqlDatabaseHelper(this);

        classId = getIntent().getStringExtra("classId");
        className = getIntent().getStringExtra("className");

        if (classId == null || className == null) {
            showNotification("Thiếu thông tin", "Missing class information");
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
            showNotification("Lỗi", "Error loading student list");
        }
    }

    private void addStudentRow(int stt, String studentId, String studentName, String dob, String parentName) {
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rowLayout.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12));
        rowLayout.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvStt = new TextView(this);
        tvStt.setText(String.valueOf(stt));
        tvStt.setTextColor(Color.BLACK);
        tvStt.setTextSize(16);
        tvStt.setWidth(dpToPx(40));
        rowLayout.addView(tvStt);

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

    private void showNotification(String title, String message) {
        String channelId = "class_student_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Thông báo danh sách học sinh",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudentList();
    }
}