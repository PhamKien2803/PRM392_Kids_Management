package com.example.se1829_prm392_kindergartenms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.se1829_prm392_kindergartenms.DAO.AccountDao;
import com.example.se1829_prm392_kindergartenms.Entity.Account;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edtUsername, edtPassword;
    private Button btnLogin;
    private TextView forgotPassword;

    private AccountDao accountDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        forgotPassword = findViewById(R.id.forgotPassword);

        accountDao = new AccountDao(this);

        btnLogin.setOnClickListener(view -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showNotification("Thông báo", "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            boolean isValid = accountDao.validate(username, password);
            if (isValid) {
                Account account = accountDao.getByUsername(username);

                if (account != null) {
                    showNotification("Thành công", "Đăng nhập thành công");

                    Intent intent;
                    int role = account.isRole();

                    if (role == 1) {
                        intent = new Intent(this, TeacherHomeManagerActivity.class);
                        if (account.getTeacherId() != null && account.getTeacherId().getTeacherId() != null) {
                            intent.putExtra("teacherId", account.getTeacherId().getTeacherId());
                        } else {
                            showNotification("Lỗi", "Missing teacher ID for role 1");
                            return;
                        }
                    } else if (role == 2) {
                        intent = new Intent(this, ParentHomeActivity.class);
                        if (account.getParentId() != null && account.getParentId().getParentId() != null) {
                            intent.putExtra("parentId", account.getParentId().getParentId());
                        } else {
                            showNotification("Lỗi", "Missing parent ID");
                            return;
                        }
                    } else {
                        intent = new Intent(this, TeacherClassManagerActivity.class);
                        if (account.getTeacherId() != null && account.getTeacherId().getTeacherId() != null) {
                            intent.putExtra("teacherId", account.getTeacherId().getTeacherId());
                        } else {
                            showNotification("Lỗi", "Missing teacher ID for default role");
                            return;
                        }
                    }

                    startActivity(intent);
                    finish();
                } else {
                    showNotification("Lỗi", "Không tìm thấy tài khoản");
                }
            } else {
                showNotification("Lỗi", "Tên đăng nhập hoặc mật khẩu không đúng");
            }
        });

        forgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void showNotification(String title, String message) {
        String channelId = "login_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Thông báo đăng nhập",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}