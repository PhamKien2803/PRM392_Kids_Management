package com.example.se1829_prm392_kindergartenms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.se1829_prm392_kindergartenms.DAO.TeacherDao;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;
import com.example.se1829_prm392_kindergartenms.R;

public class AddTeacherActivity extends AppCompatActivity {
    private EditText edtFullName, edtAddress, edtPhone, edtDob;
    private Button btnSaveTeacher;
    private TeacherDao teacherDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnViewTeachers = findViewById(R.id.btnViewTeachers);
        btnViewTeachers.setOnClickListener(v -> {
            Intent intent = new Intent(AddTeacherActivity.this, TeacherListActivity.class);
            startActivity(intent);
        });

        edtFullName = findViewById(R.id.edtFullName);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
        edtDob = findViewById(R.id.edtDob);
        btnSaveTeacher = findViewById(R.id.btnSaveTeacher);

        teacherDao = new TeacherDao(this);

        btnSaveTeacher.setOnClickListener(v -> saveTeacher());
    }

    private void saveTeacher() {
        String name = edtFullName.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            showNotification("Thiếu thông tin", "Vui lòng nhập họ tên giáo viên");
            return;
        }

        String id = teacherDao.generateAutoTeacherId();
        Teacher teacher = new Teacher(id, name, address, phone, dob);

        long result = teacherDao.insert(teacher);
        if (result != -1) {
            showNotification("Thành công", "Thêm giáo viên thành công! Mã: " + id);
            finish();
        } else {
            showNotification("Lỗi", "Thêm giáo viên thất bại");
        }
    }

    private void showNotification(String title, String message) {
        String channelId = "teacher_channel";
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