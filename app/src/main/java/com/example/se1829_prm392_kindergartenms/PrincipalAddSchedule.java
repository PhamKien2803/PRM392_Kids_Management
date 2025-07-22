package com.example.se1829_prm392_kindergartenms;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se1829_prm392_kindergartenms.DAO.ScheduleDao;
import com.example.se1829_prm392_kindergartenms.Entity.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
import java.util.Date;

public class PrincipalAddSchedule extends AppCompatActivity {

    private EditText edtActivityName;
    private EditText edtDate;
    private EditText edtTime;
    private EditText edtTimeEnd;
    private EditText edtNote;
    private Button btnSave;
    private Button btnViewSchedule;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        initViews();

        edtDate.setOnClickListener(v -> showDatePicker(edtDate));
        edtTime.setOnClickListener(v -> showTimePicker(edtTime));
        edtTimeEnd.setOnClickListener(v -> showTimePicker(edtTimeEnd));

        btnSave.setOnClickListener(v -> saveSchedule());

        btnViewSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, PrincipalManagementSchedule.class);
            startActivity(intent);
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        edtActivityName = findViewById(R.id.edtActivityName);
        edtDate = findViewById(R.id.edtDate);
        edtTime = findViewById(R.id.edtTime);
        edtTimeEnd = findViewById(R.id.edtTimeEnd);
        edtNote = findViewById(R.id.edtNote);
        btnSave = findViewById(R.id.btnSaveSchedule);
        btnViewSchedule = findViewById(R.id.btnViewSchedules);
    }

    private void showDatePicker(EditText target) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(this, (view, y, m, d) -> {
            target.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // chặn ngày quá khứ
        datePicker.show();
    }

    private void showTimePicker(EditText target) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, (view, h, m) -> {
            target.setText(String.format(Locale.getDefault(), "%02d:%02d", h, m));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void saveSchedule() {
        String activity = edtActivityName.getText().toString().trim();
        String date = edtDate.getText().toString();
        String timeStart = edtTime.getText().toString();
        String timeEnd = edtTimeEnd.getText().toString();

        if (activity.isEmpty() || date.isEmpty() || timeStart.isEmpty() || timeEnd.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate ngày không quá khứ
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date selectedDate = dateFormat.parse(date);
            Date today = dateFormat.parse(dateFormat.format(new Date()));
            if (selectedDate != null && selectedDate.before(today)) {
                Toast.makeText(this, "⛔ Không được chọn ngày trong quá khứ", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            Toast.makeText(this, "❌ Định dạng ngày không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate thời gian
        try {
            Date start = timeFormat.parse(timeStart);
            Date end = timeFormat.parse(timeEnd);

            if (start != null && end != null && !start.before(end)) {
                Toast.makeText(this, "Giờ bắt đầu phải trước giờ kết thúc", Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (ParseException e) {
            Toast.makeText(this, "Định dạng giờ không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        Schedule schedule = new Schedule(
                UUID.randomUUID().toString(),
                activity,
                timeStart,
                timeEnd,
                date
        );

        ScheduleDao scheduleDao = new ScheduleDao(this);
        long result = scheduleDao.insert(schedule);

        if (result != -1) {
            Toast.makeText(this, "Đã lưu lịch học thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi lưu lịch học", Toast.LENGTH_SHORT).show();
        }
    }
}
