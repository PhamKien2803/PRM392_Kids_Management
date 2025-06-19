package com.example.project_prm392_kidmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.project_prm392_kidmanagement.DAO.ClassDao;
import com.example.project_prm392_kidmanagement.DAO.ParentDao;
import com.example.project_prm392_kidmanagement.DAO.ScheduleDao;
import com.example.project_prm392_kidmanagement.DAO.StudentDao;
import com.example.project_prm392_kidmanagement.DAO.TeacherDao;
import com.example.project_prm392_kidmanagement.Entity.Class;
import com.example.project_prm392_kidmanagement.Entity.Parent;
import com.example.project_prm392_kidmanagement.Entity.Schedule;
import com.example.project_prm392_kidmanagement.Entity.Student;
import com.example.project_prm392_kidmanagement.Entity.Teacher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParentHomeManagerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvStudentName, tvClassInfo, tvTeacherName;
    private Button btnLogout;
    private LinearLayout scheduleContainer;

    private ParentDao parentDao;
    private StudentDao studentDao;
    private ClassDao classDao;
    private TeacherDao teacherDao;
    private ScheduleDao scheduleDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_schedule);

        String parentId = getIntent().getStringExtra("parentId");
        if (parentId == null) {
            Toast.makeText(this, "Không tìm thấy mã phụ huynh", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        toolbar = findViewById(R.id.toolbar);
        tvStudentName = findViewById(R.id.tvStudentName);
        tvClassInfo = findViewById(R.id.tvClassInfo);
        tvTeacherName = findViewById(R.id.tvTeacherName);
        btnLogout = findViewById(R.id.btnLogout);
        scheduleContainer = findViewById(R.id.scheduleContainer);

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
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AccountManagerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}