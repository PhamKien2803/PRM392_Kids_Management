package com.example.project_prm392_kidmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.project_prm392_kidmanagement.DAO.ParentDao;
import com.example.project_prm392_kidmanagement.Entity.Parent;

public class ParentHomeManagerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnPrevWeek, btnNextWeek, btnLogout;
    private ParentDao parentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_schedule);


        String parentId = getIntent().getStringExtra("parentId");
        if (parentId == null) {
            Toast.makeText(this, "Không tìm thấy mã giáo viên", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        parentDao = new ParentDao(this);
        Parent parent = parentDao.getById(String.valueOf(parentId));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (parent != null) {
            toolbar.setSubtitle("Phụ huynh: " + parent.getFullName());
        } else {
            toolbar.setSubtitle("Phụ huynh: không rõ");
        }

        btnPrevWeek = findViewById(R.id.btnPrevWeek);
        btnNextWeek = findViewById(R.id.btnNextWeek);
        btnLogout = findViewById(R.id.btnLogout);

        btnPrevWeek.setOnClickListener(v -> {
            Toast.makeText(this, "⏪ Chuyển sang tuần trước", Toast.LENGTH_SHORT).show();
        });

        btnNextWeek.setOnClickListener(v -> {
            Toast.makeText(this, "⏩ Chuyển sang tuần sau", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(view -> {
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, AccountManagerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
