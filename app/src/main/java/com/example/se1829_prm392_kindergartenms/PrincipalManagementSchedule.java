package com.example.se1829_prm392_kindergartenms;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se1829_prm392_kindergartenms.DAO.ScheduleDao;
import com.example.se1829_prm392_kindergartenms.Entity.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrincipalManagementSchedule extends AppCompatActivity {

    private LinearLayout scheduleListContainer;
    private ScheduleDao scheduleDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        scheduleListContainer = findViewById(R.id.scheduleListContainer);
        scheduleDao = new ScheduleDao(this);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        loadSchedules();
    }

    private void loadSchedules() {
        List<Schedule> schedules = scheduleDao.getAll();
        scheduleListContainer.removeAllViews();

        LayoutInflater inflater = getLayoutInflater();
        for (Schedule schedule : schedules) {
            View itemView = inflater.inflate(R.layout.item_schedule, scheduleListContainer, false);

            TextView txtInfo = itemView.findViewById(R.id.txtScheduleInfo);
            Button btnDelete = itemView.findViewById(R.id.btnDelete);
            Button btnUpdate = itemView.findViewById(R.id.btnUpdate);

            txtInfo.setText("\uD83D\uDCC5 " + schedule.getActivityName()
                    + "\n\uD83D\uDCC6 " + schedule.getTimeDate()
                    + "  ⏰ " + schedule.getTimeStart() + " - " + schedule.getTimeEnd());

            btnUpdate.setOnClickListener(v -> showUpdateDialog(schedule));

            btnDelete.setOnClickListener(v -> confirmDelete(schedule));

            scheduleListContainer.addView(itemView);
        }
    }

    private void showUpdateDialog(Schedule schedule) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_update_schedule, null);
        builder.setView(view);

        EditText edtActivity = view.findViewById(R.id.edtUpdateActivity);
        EditText edtDate = view.findViewById(R.id.edtUpdateDate);
        EditText edtStart = view.findViewById(R.id.edtUpdateTimeStart);
        EditText edtEnd = view.findViewById(R.id.edtUpdateTimeEnd);
        Button btnSave = view.findViewById(R.id.btnUpdateConfirm);

        // Gán dữ liệu cũ
        edtActivity.setText(schedule.getActivityName());
        edtDate.setText(schedule.getTimeDate());
        edtStart.setText(schedule.getTimeStart());
        edtEnd.setText(schedule.getTimeEnd());

        // Mở DatePicker - ngăn chọn ngày quá khứ
        edtDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(this, (view1, y, m, d) -> {
                String formatted = String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y);
                edtDate.setText(formatted);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // chặn ngày quá khứ
            datePicker.show();
        });

        // Mở TimePicker
        View.OnClickListener timePicker = v -> {
            EditText target = (EditText) v;
            Calendar cal = Calendar.getInstance();
            new TimePickerDialog(this, (view12, h, m) -> {
                String time = String.format(Locale.getDefault(), "%02d:%02d", h, m);
                target.setText(time);
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        };

        edtStart.setOnClickListener(timePicker);
        edtEnd.setOnClickListener(timePicker);

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String act = edtActivity.getText().toString().trim();
            String date = edtDate.getText().toString().trim();
            String start = edtStart.getText().toString().trim();
            String end = edtEnd.getText().toString().trim();

            if (act.isEmpty() || date.isEmpty() || start.isEmpty() || end.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            // Validate ngày không quá khứ
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

            // Validate giờ bắt đầu < giờ kết thúc
            try {
                Date startTime = timeFormat.parse(start);
                Date endTime = timeFormat.parse(end);
                if (startTime != null && endTime != null && !startTime.before(endTime)) {
                    Toast.makeText(this, "⛔ Giờ bắt đầu phải trước giờ kết thúc", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (ParseException e) {
                Toast.makeText(this, "❌ Định dạng giờ không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gán và cập nhật
            schedule.setActivityName(act);
            schedule.setTimeDate(date);
            schedule.setTimeStart(start);
            schedule.setTimeEnd(end);

            boolean updated = scheduleDao.update(schedule);
            if (updated) {
                Toast.makeText(this, "✅ Cập nhật thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadSchedules();
            } else {
                Toast.makeText(this, "❌ Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }



    private void confirmDelete(Schedule schedule) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xoá")
                .setMessage("Bạn có chắc chắn muốn xoá lịch học này?")
                .setPositiveButton("Xoá", (dialog, which) -> {
                    boolean deleted = scheduleDao.delete(schedule.getScheduleId());
                    if (deleted) {
                        Toast.makeText(this, "Đã xoá thành công", Toast.LENGTH_SHORT).show();
                        loadSchedules();
                    } else {
                        Toast.makeText(this, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}
