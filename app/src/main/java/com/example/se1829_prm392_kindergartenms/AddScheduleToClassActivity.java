package com.example.se1829_prm392_kindergartenms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;

import java.util.*;

public class AddScheduleToClassActivity extends AppCompatActivity {

    private LinearLayout classCheckboxContainer, subjectCheckboxContainer;
    private Spinner spinnerDayOfWeek;
    private TextView tvSubjectTime;
    private Button btnSaveSchedule, btnBack;
    private SqlDatabaseHelper dbHelper;

    private List<String> classIds = new ArrayList<>();
    private List<ScheduleModel> subjectList = new ArrayList<>();
    private List<ScheduleModel> selectedSubjects = new ArrayList<>();

    private class ScheduleModel {
        String scheduleId;
        String activityName;
        String timeStart;
        String timeEnd;

        ScheduleModel(String id, String name, String start, String end) {
            this.scheduleId = id;
            this.activityName = name;
            this.timeStart = start;
            this.timeEnd = end;
        }

        String getTimeRange() {
            return timeStart + " - " + timeEnd;
        }

        @Override
        public String toString() {
            return activityName + " (" + timeStart + " - " + timeEnd + ")";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule_to_class);

        classCheckboxContainer = findViewById(R.id.classCheckboxContainer);
        subjectCheckboxContainer = findViewById(R.id.subjectCheckboxContainer);
        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        tvSubjectTime = findViewById(R.id.tvSubjectTime);
        btnSaveSchedule = findViewById(R.id.btnSaveSchedule);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new SqlDatabaseHelper(this);

        setupDayOfWeekSpinner();
        loadClasses();
        loadSubjectsFromSchedule();

        btnSaveSchedule.setOnClickListener(v -> saveScheduleToClass());
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupDayOfWeekSpinner() {
        String[] days = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDayOfWeek.setAdapter(adapter);
    }

    private void loadClasses() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT classId, className FROM classes", null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            classIds.add(id);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(name);
            checkBox.setTag(id);
            classCheckboxContainer.addView(checkBox);
        }
        cursor.close();
    }

    private void loadSubjectsFromSchedule() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT scheduleId, activityName, timeStart, timeEnd FROM schedules", null);

        subjectList.clear();
        subjectCheckboxContainer.removeAllViews();

        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String start = cursor.getString(2);
            String end = cursor.getString(3);

            ScheduleModel model = new ScheduleModel(id, name, start, end);
            subjectList.add(model);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(model.toString());
            checkBox.setTag(model);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                ScheduleModel selected = (ScheduleModel) buttonView.getTag();
                if (isChecked) {
                    for (ScheduleModel existing : selectedSubjects) {
                        if (overlaps(selected, existing)) {
                            buttonView.setChecked(false);
                            showNotification("Trùng giờ", "Trùng giờ với: " + existing.activityName);
                            return;
                        }
                    }
                    selectedSubjects.add(selected);
                } else {
                    selectedSubjects.remove(selected);
                }
                updateSelectedTimesText();
            });

            subjectCheckboxContainer.addView(checkBox);
        }

        cursor.close();
    }

    private boolean overlaps(ScheduleModel a, ScheduleModel b) {
        return a.timeStart.compareTo(b.timeEnd) < 0 && b.timeStart.compareTo(a.timeEnd) < 0;
    }

    private void updateSelectedTimesText() {
        StringBuilder sb = new StringBuilder();
        for (ScheduleModel s : selectedSubjects) {
            sb.append(s.activityName).append(": ").append(s.getTimeRange()).append("\n");
        }
        tvSubjectTime.setText(sb.toString().trim().isEmpty() ? "Giờ học: --:-- - --:--" : sb.toString().trim());
    }

    private void saveScheduleToClass() {
        List<String> selectedClassIds = new ArrayList<>();
        for (int i = 0; i < classCheckboxContainer.getChildCount(); i++) {
            View view = classCheckboxContainer.getChildAt(i);
            if (view instanceof CheckBox && ((CheckBox) view).isChecked()) {
                selectedClassIds.add((String) view.getTag());
            }
        }

        if (selectedClassIds.isEmpty()) {
            showNotification("Thiếu thông tin", "Vui lòng chọn ít nhất một lớp học");
            return;
        }

        if (selectedSubjects.isEmpty()) {
            showNotification("Thiếu thông tin", "Vui lòng chọn ít nhất một môn học");
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (String classId : selectedClassIds) {
            for (ScheduleModel subject : selectedSubjects) {
                db.execSQL("INSERT INTO schedulesToClass (scheduleClassID, scheduleId, classId) VALUES (?, ?, ?)",
                        new Object[]{UUID.randomUUID().toString(), subject.scheduleId, classId});
            }
        }

        showNotification("Thành công", "Đã lưu lịch học thành công");
        finish();
    }

    private void showNotification(String title, String message) {
        String channelId = "schedule_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Thông báo lịch học",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification) // Đảm bảo có icon này trong drawable
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}