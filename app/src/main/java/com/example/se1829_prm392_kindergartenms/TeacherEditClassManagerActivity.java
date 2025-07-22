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

import com.example.se1829_prm392_kindergartenms.DAO.ClassDao;
import com.example.se1829_prm392_kindergartenms.DAO.TeacherDao;
import com.example.se1829_prm392_kindergartenms.Entity.Class;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;

import java.util.List;

public class TeacherEditClassManagerActivity extends AppCompatActivity {
    private Button btnBack, btnSaveChanges, btnDelete;
    private EditText edtEditClassName, edtEditTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_edit_class);
        String classId = getIntent().getStringExtra("classId");

        ClassDao classDao = new ClassDao(this);
        TeacherDao teacherDao = new TeacherDao(this);
        Class class1 = classDao.getById(classId);
        Teacher teacher1 = class1.getTeacherId();

        btnBack = findViewById(R.id.btnBack);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnDelete = findViewById(R.id.btnDelete);

        edtEditClassName = findViewById(R.id.edtEditClassName);
        edtEditTeacher = findViewById(R.id.edtEditTeacher);

        if (class1 != null) {
            edtEditClassName.setText(class1.getClassName());
            edtEditTeacher.setText(teacher1.getFullName());
        }

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeacherClassManagerActivity.class);
            startActivity(intent);
        });

        btnSaveChanges.setOnClickListener(v -> {
            String newClassName = edtEditClassName.getText().toString().trim();
            String newTeacherName = edtEditTeacher.getText().toString().trim();

            if (newClassName.isEmpty() || newTeacherName.isEmpty()) {
                showNotification("Thiếu thông tin", "Vui lòng điền đầy đủ thông tin");
                return;
            }

            List<Teacher> allTeachers = teacherDao.getAll();
            Teacher foundTeacher = null;
            for (Teacher t : allTeachers) {
                if (t.getFullName().equalsIgnoreCase(newTeacherName)) {
                    foundTeacher = t;
                    break;
                }
            }

            if (foundTeacher == null) {
                showNotification("Lỗi", "Không tìm thấy giáo viên tên \"" + newTeacherName + "\"");
                return;
            }

            List<Class> allClasses = classDao.getAll();
            for (Class c : allClasses) {
                if (c.getTeacherId() != null &&
                        c.getTeacherId().getTeacherId().equals(foundTeacher.getTeacherId()) &&
                        !c.getClassId().equals(class1.getClassId())) {
                    showNotification("Lỗi", "Giáo viên này đã được phân công dạy lớp khác");
                    return;
                }
            }

            class1.setClassName(newClassName);
            class1.setTeacherId(foundTeacher);

            boolean updated = classDao.update(class1);
            if (updated) {
                showNotification("Thành công", "Cập nhật thành công");
                Intent intent = new Intent(this, TeacherClassManagerActivity.class);
                startActivity(intent);
            } else {
                showNotification("Lỗi", "Có lỗi xảy ra khi cập nhật");
            }
        });

        btnDelete.setOnClickListener(v -> {
            boolean deleted = classDao.delete(class1.getClassId());
            if (deleted) {
                showNotification("Thành công", "Xóa lớp học thành công");
                Intent intent = new Intent(this, TeacherClassManagerActivity.class);
                startActivity(intent);
            } else {
                showNotification("Lỗi", "Không thể xóa lớp học");
            }
        });
    }

    private void showNotification(String title, String message) {
        String channelId = "teacher_edit_class_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Thông báo chỉnh sửa lớp",
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