package com.example.se1829_prm392_kindergartenms;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se1829_prm392_kindergartenms.DB.SqlDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TeacherStudentListActivity extends AppCompatActivity {

    private SqlDatabaseHelper dbHelper;
    private String teacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_view_studentlist);

        // Hiện nút back trên ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Danh sách học sinh");
        }

        // Xử lý nút back dưới giao diện
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Lấy teacherId từ Intent
        teacherId = getIntent().getStringExtra("teacherId");
        Log.d("DEBUG", "Teacher ID nhận được: " + teacherId);

        if (teacherId == null) {
            Toast.makeText(this, "Không tìm thấy mã giáo viên", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        dbHelper = new SqlDatabaseHelper(this);

        // Tải tên lớp và danh sách học sinh
        loadClassName();
        loadStudents();
    }

    private void loadClassName() {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT className FROM classes WHERE teacherId = ?";
            Cursor cursor = db.rawQuery(sql, new String[]{teacherId});

            if (cursor.moveToFirst()) {
                String className = cursor.getString(0);
                TextView tvClassName = findViewById(R.id.tvClassName);
                tvClassName.setText("📚 Danh sách học sinh lớp " + className);
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tải tên lớp: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadStudents() {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String sql = "SELECT s.fullName " +
                    "FROM students s " +
                    "JOIN studentToClass sc ON s.studentId = sc.studentId " +
                    "JOIN classes c ON sc.classId = c.classId " +
                    "WHERE c.teacherId = ?";

            Cursor cursor = db.rawQuery(sql, new String[]{teacherId});

            List<String> studentList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String studentName = cursor.getString(0);
                studentList.add("👶 " + studentName);
            }
            cursor.close();

            ListView listViewStudents = findViewById(R.id.listViewStudents);
            if (listViewStudents == null) {
                Toast.makeText(this, "Không tìm thấy ListView (listViewStudents)", Toast.LENGTH_LONG).show();
                return;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, studentList
            );
            listViewStudents.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tải danh sách: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
