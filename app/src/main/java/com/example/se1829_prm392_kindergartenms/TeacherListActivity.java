package com.example.se1829_prm392_kindergartenms;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1829_prm392_kindergartenms.DAO.TeacherDao;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;

import java.util.List;

public class TeacherListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TeacherDao teacherDao;
    private List<Teacher> teacherList;
    private RecyclerView.Adapter<TeacherViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_teacher);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerTeacher);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        teacherDao = new TeacherDao(this);
        loadTeachers();
    }

    private void loadTeachers() {
        teacherList = teacherDao.getAll();

        adapter = new RecyclerView.Adapter<TeacherViewHolder>() {
            @NonNull
            @Override
            public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LinearLayout layout = new LinearLayout(parent.getContext());
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setPadding(16, 16, 16, 16);

                TextView tvInfo = new TextView(parent.getContext());
                tvInfo.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));
                tvInfo.setTextSize(16);

                Button btnEdit = new Button(parent.getContext());
                btnEdit.setText("Sửa");
                btnEdit.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                Button btnDelete = new Button(parent.getContext());
                btnDelete.setText("Xóa");
                btnDelete.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                layout.addView(tvInfo);
                layout.addView(btnEdit);
                layout.addView(btnDelete);

                return new TeacherViewHolder(layout, tvInfo, btnEdit, btnDelete);
            }

            @Override
            public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
                Teacher t = teacherList.get(position);
                holder.tvInfo.setText(t.getFullName() + " - " + t.getPhone());

                // Nút sửa
                holder.btnEdit.setOnClickListener(v -> {
                    Intent intent = new Intent(TeacherListActivity.this, EditTeacherActivity.class);
                    intent.putExtra("teacherId", t.getTeacherId());
                    startActivity(intent);
                });

                // Nút xóa với xác nhận
                holder.btnDelete.setOnClickListener(v -> {
                    new AlertDialog.Builder(TeacherListActivity.this)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa giáo viên này?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                teacherDao.delete(t.getTeacherId());
                                loadTeachers(); // cập nhật danh sách
                                Toast.makeText(TeacherListActivity.this, "Đã xóa giáo viên", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                });
            }

            @Override
            public int getItemCount() {
                return teacherList.size();
            }
        };

        recyclerView.setAdapter(adapter);
    }

    static class TeacherViewHolder extends RecyclerView.ViewHolder {
        TextView tvInfo;
        Button btnEdit, btnDelete;

        public TeacherViewHolder(@NonNull View itemView, TextView tvInfo, Button btnEdit, Button btnDelete) {
            super(itemView);
            this.tvInfo = tvInfo;
            this.btnEdit = btnEdit;
            this.btnDelete = btnDelete;
        }
    }
}

