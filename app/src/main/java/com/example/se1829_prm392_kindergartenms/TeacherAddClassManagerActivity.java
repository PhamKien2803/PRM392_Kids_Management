package com.example.se1829_prm392_kindergartenms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.se1829_prm392_kindergartenms.DAO.ClassDao;
import com.example.se1829_prm392_kindergartenms.DAO.TeacherDao;
import com.example.se1829_prm392_kindergartenms.Entity.Class;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherAddClassManagerActivity extends AppCompatActivity {
    private Button btnBack, btnAddClass;
    private EditText edtClassName;
    private Spinner spinnerTeacher;
    private List<Teacher> teacherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_add_class);

        btnBack = findViewById(R.id.btnBack);
        btnAddClass = findViewById(R.id.btnAddClass);
        edtClassName = findViewById(R.id.edtClassName);
        spinnerTeacher = findViewById(R.id.spinnerTeacher);

        btnBack.setOnClickListener(v -> finish());

        TeacherDao teacherDao = new TeacherDao(this);
        teacherList = teacherDao.getAll();
        List<String> teacherNames = new ArrayList<>();
        for (Teacher t : teacherList) {
            teacherNames.add(t.getFullName() + " (" + t.getTeacherId() + ")");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teacherNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeacher.setAdapter(adapter);

        btnAddClass.setOnClickListener(v -> {
            String className = edtClassName.getText().toString().trim();
            int selectedPosition = spinnerTeacher.getSelectedItemPosition();
            if (className.isEmpty()) {
                showNotification("Thiếu thông tin", "Vui lòng nhập tên lớp!");
                return;
            }
            if (teacherList == null || teacherList.isEmpty() || selectedPosition < 0) {
                showNotification("Thiếu thông tin", "Vui lòng chọn giáo viên!");
                return;
            }
            Teacher selectedTeacher = teacherList.get(selectedPosition);
            Class classroom = new Class();
            classroom.setClassId("CL" + System.currentTimeMillis());
            classroom.setClassName(className);
            classroom.setTeacherId(selectedTeacher);
            classroom.setScheduleId(null);
            classroom.setSchoolYear("2024-2025");
            ClassDao classDao = new ClassDao(this);
            long result = classDao.insert(classroom);
            if (result != -1) {
                showNotification("Thành công", "Thêm lớp thành công!");
                Intent intent = new Intent(this, TeacherClassManagerActivity.class);
                startActivity(intent);
            } else {
                showNotification("Lỗi", "Thêm lớp thất bại!");
            }
        });
    }

    private void showNotification(String title, String message) {
        String channelId = "teacher_add_class_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Thông báo thêm lớp",
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