package com.example.se1829_prm392_kindergartenms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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

        etTeacherCode = findViewById(R.id.etTeacherCode);
        etFullName = findViewById(R.id.etFullName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        etDob = findViewById(R.id.etDob);
        btnUpdateTeacher = findViewById(R.id.btnUpdateTeacher);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new SqlDatabaseHelper(this);

        Intent intent = getIntent();
        teacherId = intent.getStringExtra("teacherId");

        if (teacherId != null) {
            loadTeacherInfo(teacherId);
        } else {
            showNotification("Lỗi", "Không tìm thấy mã giáo viên");
            finish();
        }

        btnUpdateTeacher.setOnClickListener(v -> updateTeacherInfo());
        btnBack.setOnClickListener(v -> finish());
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
            showNotification("Lỗi", "Không tìm thấy thông tin giáo viên");
            finish();
        }
    }

    private void updateTeacherInfo() {
        String fullName = etFullName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dob = etDob.getText().toString().trim();

        if (fullName.isEmpty()) {
            showNotification("Thiếu thông tin", "Vui lòng nhập tên giáo viên");
            return;
        }

        boolean success = dbHelper.updateTeacher(new Teacher(teacherId, fullName, address, phone, dob));

        if (success) {
            showNotification("Thành công", "Cập nhật thành công");
            finish();
        } else {
            showNotification("Lỗi", "Cập nhật thất bại");
        }
    }

    private void showNotification(String title, String message) {
        String channelId = "edit_teacher_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Thông báo giáo viên",
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