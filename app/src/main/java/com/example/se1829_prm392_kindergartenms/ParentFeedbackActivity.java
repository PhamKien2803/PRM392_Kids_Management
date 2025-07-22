package com.example.se1829_prm392_kindergartenms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.se1829_prm392_kindergartenms.DAO.ClassDao;
import com.example.se1829_prm392_kindergartenms.DAO.ParentDao;
import com.example.se1829_prm392_kindergartenms.DAO.StudentDao;
import com.example.se1829_prm392_kindergartenms.DAO.TeacherDao;
import com.example.se1829_prm392_kindergartenms.Entity.Class;
import com.example.se1829_prm392_kindergartenms.Entity.Student;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;

import java.util.List;

public class ParentFeedbackActivity extends AppCompatActivity {

    private TextView tvStudentName, tvClassInfo, tvTeacherName, btnBack;

    private ParentDao parentDao;
    private StudentDao studentDao;
    private ClassDao classDao;
    private TeacherDao teacherDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_feedback);

        String parentId = getIntent().getStringExtra("parentId");
        if (parentId == null) {
            showNotification("Lỗi", "Không tìm thấy mã phụ huynh");
            finish();
            return;
        }

        tvStudentName = findViewById(R.id.tvStudentName);
        tvClassInfo = findViewById(R.id.tvClassInfo);
        tvTeacherName = findViewById(R.id.tvTeacherName);
        btnBack = findViewById(R.id.btnBack);

        parentDao = new ParentDao(this);
        studentDao = new StudentDao(this);
        classDao = new ClassDao(this);
        teacherDao = new TeacherDao(this);

        loadStudentInfo(parentId);

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadStudentInfo(String parentId) {
        List<Student> studentList = studentDao.getStudentsByParentId(parentId);

        if (!studentList.isEmpty()) {
            Student student = studentList.get(0);
            tvStudentName.setText("🧒 Bé: " + student.getFullName());

            Class classroom = classDao.getById(student.getClassId().getClassId());
            if (classroom != null) {
                tvClassInfo.setText("📚 Lớp: " + classroom.getClassName() + " - Năm học: " + classroom.getSchoolYear());

                Teacher teacher = teacherDao.getById(classroom.getTeacherId().getTeacherId());
                if (teacher != null) {
                    tvTeacherName.setText("👩‍🏫 Giáo viên: " + teacher.getFullName());
                }
            }
        } else {
            showNotification("Lỗi", "Không tìm thấy thông tin học sinh");
        }
    }

    private void showNotification(String title, String message) {
        String channelId = "parent_feedback_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Thông báo phụ huynh",
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