package com.example.project_prm392_kidmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project_prm392_kidmanagement.Entity.User;
import com.example.project_prm392_kidmanagement.Repo.UserRepository;
import java.util.List;

public class UserManagerActivity extends AppCompatActivity {

    EditText edtUserId, edtUsername, edtPassword;
    Button btnAdd, btnUpdate, btnDelete, btnShow, btnGoToOrderPage;
    TextView txtUserList;
    UserRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);

        repo = new UserRepository(this);

        edtUserId = findViewById(R.id.edtUserId);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        txtUserList = findViewById(R.id.txtUserList);
        btnAdd = findViewById(R.id.btnAddUser);
        btnUpdate = findViewById(R.id.btnUpdateUser);
        btnDelete = findViewById(R.id.btnDeleteUser);
        btnShow = findViewById(R.id.btnShowAll);
        btnGoToOrderPage = findViewById(R.id.btnGoToOrderPage);

        btnGoToOrderPage.setOnClickListener(v -> {
            Intent intent = new Intent(UserManagerActivity.this, OrderFormActivity.class);
            startActivity(intent);
        });

        btnAdd.setOnClickListener(v -> {
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username and Password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            repo.register(new User(0, username, password));
            Toast.makeText(this, "Đã thêm", Toast.LENGTH_SHORT).show();
            clearInputFields();
            loadUserList();
        });

        btnUpdate.setOnClickListener(v -> {
            String idStr = edtUserId.getText().toString();
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();

            if (idStr.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "User ID and Username cannot be empty for update", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int id = Integer.parseInt(idStr);
                repo.update(new User(id, username, password));
                Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                clearInputFields();
                loadUserList();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid User ID format", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            String idStr = edtUserId.getText().toString();
            if (idStr.isEmpty()) {
                Toast.makeText(this, "Please enter User ID to delete", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                int id = Integer.parseInt(idStr);
                repo.delete(id);
                Toast.makeText(this, "Đã xoá", Toast.LENGTH_SHORT).show();
                clearInputFields();
                loadUserList();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid User ID format", Toast.LENGTH_SHORT).show();
            }
        });

        btnShow.setOnClickListener(v -> {
            loadUserList();
        });

        loadUserList();
    }

    private void loadUserList() {
        List<User> users = repo.getAllUsers();
        if (users == null) {
            txtUserList.setText("No users found or error loading users.");
            return;
        }
        StringBuilder b = new StringBuilder();
        for (User u : users) {
            b.append("ID: ").append(u.getId())
                    .append(" - Username: ").append(u.getUsername()).append("\n");
        }
        txtUserList.setText(b.toString());
    }

    private void clearInputFields() {
        edtUserId.setText("");
        edtUsername.setText("");
        edtPassword.setText("");
    }
}