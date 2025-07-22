package com.example.se1829_prm392_kindergartenms;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;

public class StudentDetailWithParentActivity extends AppCompatActivity {

    private static final String TAG = "StudentDetail";

    // View components
    private TextView tvStudentId, tvStudentName, tvClassName, tvDob;
    private LinearLayout parentsListContainer;

    // Database helper
    private SqlDatabaseHelper dbHelper;
    private String studentId;

    private Button btnBack; // Nút quay lại


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_detail_with_parents);

        initializeComponents();
        loadStudentData();
    }

    private void initializeComponents() {
        dbHelper = new SqlDatabaseHelper(this);

        tvStudentId = findViewById(R.id.tvStudentId);
        tvStudentName = findViewById(R.id.tvStudentName);
        tvClassName = findViewById(R.id.tvClassName);
        tvDob = findViewById(R.id.tvDob);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng activity hiện tại và quay lại màn hình trước
            }
        });
        parentsListContainer = findViewById(R.id.parentsListContainer);

        studentId = getIntent().getStringExtra("studentId");
        if (studentId == null || studentId.isEmpty()) {
            showToast("Không có thông tin học sinh");
            finish();
        }
    }

    private void loadStudentData() {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String query = "SELECT s.studentId, s.fullName, c.className, s.dob, " +
                    "p.parentId, p.fullName as parentName, p.phone, p.address " +
                    "FROM students s " +
                    "LEFT JOIN classes c ON s.classId = c.classId " +
                    "LEFT JOIN parents p ON s.parentId = p.parentId " +
                    "WHERE s.studentId = ?";

            try (Cursor cursor = db.rawQuery(query, new String[]{studentId})) {
                if (cursor.moveToFirst()) {
                    displayStudentData(cursor);
                    displayParentData(cursor);
                } else {
                    showToast("Không tìm thấy học sinh");
                    finish();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Lỗi tải dữ liệu", e);
            showToast("Lỗi tải thông tin học sinh");
        }
    }

    private void displayStudentData(@NonNull Cursor cursor) {
        tvStudentId.setText(cursor.getString(0));
        tvStudentName.setText(cursor.getString(1));
        tvClassName.setText(cursor.getString(2));
        tvDob.setText(cursor.getString(3));
    }

    private void displayParentData(@NonNull Cursor cursor) {
        parentsListContainer.removeAllViews();

        // Add header row
        addHeaderRow();

        String parentId = cursor.getString(4);
        if (parentId != null) {
            addParentRow(
                    cursor.getString(5), // parentName
                    cursor.getString(6), // phone
                    cursor.getString(7)  // address
            );
        } else {
            showNoParentMessage();
        }
    }

    private void addHeaderRow() {
        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        headerRow.setBackgroundColor(Color.parseColor("#FF9800"));
        headerRow.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));

        // Tên phụ huynh
        headerRow.addView(createHeaderTextView("Tên phụ huynh", 0, 4));

        // SĐT
        headerRow.addView(createHeaderTextView("Số điện thoại", 0, 3));

        // Địa chỉ
        headerRow.addView(createHeaderTextView("Địa chỉ", 0, 3));

        parentsListContainer.addView(headerRow);
    }

    private TextView createHeaderTextView(String text, int widthInDp, float weight) {
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                weight);

        params.setMargins(dpToPx(4), 0, dpToPx(4), 0);
        tv.setLayoutParams(params);
        tv.setText(text);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    private void addParentRow(String name, String phone, String address) {
        LinearLayout parentRow = createParentRowLayout();

        // Tên phụ huynh
        parentRow.addView(createParentInfoView(name));

        // SĐT
        parentRow.addView(createPhoneView(phone));

        // Địa chỉ
        parentRow.addView(createAddressView(address));

        parentsListContainer.addView(parentRow);
    }

    private LinearLayout createParentRowLayout() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        row.setBackgroundColor(Color.WHITE);
        row.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        row.setElevation(dpToPx(2));
        row.setMinimumHeight(dpToPx(48));
        return row;
    }

    private LinearLayout createParentInfoView(String name) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 4);
        params.setMargins(dpToPx(8), 0, dpToPx(8), 0);
        layout.setLayoutParams(params);

        TextView tvName = new TextView(this);
        tvName.setText(name != null ? name : "Không rõ");
        tvName.setTextColor(Color.BLACK);
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        tvName.setGravity(Gravity.START);
        layout.addView(tvName);

        return layout;
    }

    private TextView createPhoneView(String phone) {
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 3);
        params.setMargins(dpToPx(8), 0, dpToPx(8), 0);
        tv.setLayoutParams(params);
        tv.setText(phone != null ? phone : "Chưa có SĐT");
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setGravity(Gravity.START);
        return tv;
    }

    private TextView createAddressView(String address) {
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 3);
        params.setMargins(dpToPx(8), 0, dpToPx(8), 0);
        tv.setLayoutParams(params);
        tv.setText(address != null ? address : "Chưa có địa chỉ");
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setGravity(Gravity.START);
        return tv;
    }

    private void showNoParentMessage() {
        TextView tv = new TextView(this);
        tv.setText("Không có thông tin phụ huynh");
        tv.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setGravity(Gravity.CENTER);
        parentsListContainer.addView(tv);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}