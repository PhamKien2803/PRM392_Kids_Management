package com.example.se1829_prm392_kindergartenms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.example.se1829_prm392_kindergartenms.DAO.ClassDao;
import com.example.se1829_prm392_kindergartenms.DAO.ParentDao;
import com.example.se1829_prm392_kindergartenms.DAO.ScheduleDao;
import com.example.se1829_prm392_kindergartenms.DAO.StudentDao;
import com.example.se1829_prm392_kindergartenms.DAO.TeacherDao;
import com.example.se1829_prm392_kindergartenms.Entity.Parent;
import com.example.se1829_prm392_kindergartenms.Entity.Schedule;
import com.example.se1829_prm392_kindergartenms.Entity.Student;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;
import com.example.se1829_prm392_kindergartenms.Entity.Class;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParentHomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvStudentName, tvClassInfo, tvTeacherName;
    private Button btnLogout;
    private LinearLayout scheduleContainer;
    private Button btnFeedback, btnContact;

    private ParentDao parentDao;
    private StudentDao studentDao;
    private ClassDao classDao;
    private TeacherDao teacherDao;
    private ScheduleDao scheduleDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);

        String parentId = getIntent().getStringExtra("parentId");
        if (parentId == null) {
            showNotification("Lỗi", "Không tìm thấy mã phụ huynh");
            finish();
            return;
        }

        toolbar = findViewById(R.id.toolbar);
        tvStudentName = findViewById(R.id.tvStudentName);
        tvClassInfo = findViewById(R.id.tvClassInfo);
        tvTeacherName = findViewById(R.id.tvTeacherName);
        btnLogout = findViewById(R.id.btnLogout);
        scheduleContainer = findViewById(R.id.scheduleContainer);
        btnFeedback = findViewById(R.id.btnNotifications);
        btnContact = findViewById(R.id.btnContact);

        setSupportActionBar(toolbar);

        parentDao = new ParentDao(this);
        studentDao = new StudentDao(this);
        classDao = new ClassDao(this);
        teacherDao = new TeacherDao(this);
        scheduleDao = new ScheduleDao(this);

        Parent parent = parentDao.getById(parentId);
        if (parent != null) {
            toolbar.setSubtitle("Phụ huynh: " + parent.getFullName());
        }

        List<Student> studentList = studentDao.getStudentsByParentId(parentId);
        if (!studentList.isEmpty()) {
            Student student = studentList.get(0);
            tvStudentName.setText("\uD83E\uDDD2 Bé: " + student.getFullName());

            Class classroom = classDao.getById(student.getClassId().getClassId());
            if (classroom != null) {
                tvClassInfo.setText("\uD83D\uDCDA Lớp: " + classroom.getClassName() + " – Năm học: " + classroom.getSchoolYear());

                Teacher teacher = teacherDao.getById(classroom.getTeacherId().getTeacherId());
                if (teacher != null) {
                    tvTeacherName.setText("\uD83D\uDC69‍\uD83C\uDFEB GV: " + teacher.getFullName());
                }

                List<Schedule> schedules = scheduleDao.getSchedulesByClassId(classroom.getClassId());
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));

                for (Schedule schedule : schedules) {
                    String formattedDate = schedule.getTimeDate();
                    try {
                        Date date = inputFormat.parse(schedule.getTimeDate());
                        formattedDate = outputFormat.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    TextView tvDate = new TextView(this);
                    tvDate.setText("\uD83D\uDCC5 " + formattedDate + " , " + schedule.getTimeStart() + " - " + schedule.getTimeEnd());
                    tvDate.setTextColor(Color.parseColor("#1A237E"));
                    tvDate.setTextSize(15);
                    tvDate.setTypeface(null, Typeface.BOLD);
                    tvDate.setPadding(0, 12, 0, 4);
                    scheduleContainer.addView(tvDate);

                    TextView tvActivity = new TextView(this);
                    tvActivity.setText("\uD83D\uDD39 " + schedule.getActivityName());
                    tvActivity.setTextColor(Color.parseColor("#37474F"));
                    tvActivity.setTextSize(14);
                    tvActivity.setPadding(16, 0, 0, 8);
                    scheduleContainer.addView(tvActivity);
                }
            }
        }

        btnLogout.setOnClickListener(view -> {
            showNotification("Đăng xuất", "Đăng xuất thành công");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        btnFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(ParentHomeActivity.this, ParentFeedbackActivity.class);
            intent.putExtra("parentId", parentId);
            startActivity(intent);
        });

        btnContact.setOnClickListener(v -> {
            showNotification("Thông báo", "Tính năng liên hệ đang phát triển");
        });
    }

    private void showNotification(String title, String message) {
        String channelId = "parent_home_channel";
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