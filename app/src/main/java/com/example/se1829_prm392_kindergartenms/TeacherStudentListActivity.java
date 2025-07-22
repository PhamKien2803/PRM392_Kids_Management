package com.example.se1829_prm392_kindergartenms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TeacherStudentListActivity extends AppCompatActivity {

    private SqlDatabaseHelper dbHelper;
    private String teacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_view_studentlist);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Danh sách học sinh");
        }

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        teacherId = getIntent().getStringExtra("teacherId");
        Log.d("DEBUG", "Teacher ID nhận được: " + teacherId);

        if (teacherId == null) {
            showNotification("Lỗi", "Không tìm thấy mã giáo viên");
            finish();
            return;
        }

        dbHelper = new SqlDatabaseHelper(this);

        loadClassName();
        loadStudents();
    }

    private void loadClassName() {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT className FROM classes WHERE teacherId = ?";
            Cursor cursor = db.rawQuery(sql, new String[]{teacherId});

            if (cursor.moveToFirst()) {
                String className = cursor.getString(0);
                TextView tvClassName = findViewById(R.id.tvClassName);
                tvClassName.setText("📚 Danh sách học sinh lớp " + className);
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            showNotification("Lỗi", "Lỗi khi tải tên lớp: " + e.getMessage());
        }
    }

    private void loadStudents() {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String sql = "SELECT s.fullName " +
                    "FROM students s " +
                    "JOIN studentToClass sc ON s.studentId = sc.studentId " +
                    "JOIN classes c ON sc.classId = c.classId " +
                    "WHERE c.teacherId = ?";

            Cursor cursor = db.rawQuery(sql, new String[]{teacherId});

            List<String> studentList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String studentName = cursor.getString(0);
                studentList.add("👶 " + studentName);
            }
            cursor.close();

            ListView listViewStudents = findViewById(R.id.listViewStudents);
            if (listViewStudents == null) {
                showNotification("Lỗi", "Không tìm thấy ListView (listViewStudents)");
                return;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, studentList
            );
            listViewStudents.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
            showNotification("Lỗi", "Lỗi khi tải danh sách: " + e.getMessage());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showNotification(String title, String message) {
        String channelId = "teacher_student_list_channel";
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
}