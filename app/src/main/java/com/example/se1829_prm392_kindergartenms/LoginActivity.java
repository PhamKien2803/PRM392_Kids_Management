package com.example.se1829_prm392_kindergartenms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se1829_prm392_kindergartenms.DAO.AccountDao;
import com.example.se1829_prm392_kindergartenms.Entity.Account;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edtUsername, edtPassword;
    private Button btnLogin;
    private TextView  forgotPassword;

    private AccountDao accountDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
//        btnSignUp = findViewById(R.id.btnSignUp);
        forgotPassword = findViewById(R.id.forgotPassword);

        accountDao = new AccountDao(this);

        btnLogin.setOnClickListener(view -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isValid = accountDao.validate(username, password);
            if (isValid) {
                Account account = accountDao.getByUsername(username);

                if (account != null) {
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                    Intent intent;
                    int role = account.isRole();

                    if (role == 1) {
                        intent = new Intent(this, TeacherHomeManagerActivity.class);
                        if (account.getTeacherId() != null && account.getTeacherId().getTeacherId() != null) {
                            intent.putExtra("teacherId", account.getTeacherId().getTeacherId()); // giữ kiểu String
                        } else {
                            Toast.makeText(this, "Missing teacher ID for role 1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else if (role == 2) {
                        intent = new Intent(this, ParentHomeActivity.class);
                        if (account.getParentId() != null && account.getParentId().getParentId() != null) {
                            intent.putExtra("parentId", account.getParentId().getParentId()); // không parseInt
                        } else {
                            Toast.makeText(this, "Missing parent ID", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        intent = new Intent(this, TeacherClassManagerActivity.class);
                        if (account.getTeacherId() != null && account.getTeacherId().getTeacherId() != null) {
                            intent.putExtra("teacherId", account.getTeacherId().getTeacherId()); // giữ kiểu String
                        } else {
                            Toast.makeText(this, "Missing teacher ID for default role", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }


                    startActivity(intent);

                    finish();
                } else {
                    Toast.makeText(this, "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        });

        forgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }
}