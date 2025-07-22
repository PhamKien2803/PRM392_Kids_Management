package com.example.se1829_prm392_kindergartenms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se1829_prm392_kindergartenms.DAO.AccountDao;
import com.example.se1829_prm392_kindergartenms.Service.EmailService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";

    private TextInputEditText editTextEmail;
    private TextInputLayout textInputEmail;
    private Button buttonSendReset;
    private ImageButton btnBack;
    private ProgressBar progressBar;
    private AccountDao accountDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();

        setupClickListeners();

        accountDao = new AccountDao(this);
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        textInputEmail = findViewById(R.id.textInputEmail);
        buttonSendReset = findViewById(R.id.buttonSendReset);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(view -> finish());

        buttonSendReset.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString().trim();

            if (validateEmail(email)) {
                sendNewPasswordEmail(email);
            }
        });
    }

    private boolean validateEmail(String email) {
        textInputEmail.setError(null);

        if (email.isEmpty()) {
            textInputEmail.setError("Vui lòng nhập email");
            editTextEmail.requestFocus();
            return false;
        }

        if (!isValidEmail(email)) {
            textInputEmail.setError("Email không đúng định dạng");
            editTextEmail.requestFocus();
            return false;
        }

        if (!accountDao.isEmailExists(email)) {
            textInputEmail.setError("Email không tồn tại trong hệ thống");
            editTextEmail.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendNewPasswordEmail(String email) {
        showLoading(true);

        EmailService.sendNewPasswordEmail(email, new EmailService.EmailCallback() {
            @Override
            public void onSuccess(String newPassword) {
                // Cập nhật mật khẩu mới vào database
                boolean updateSuccess = accountDao.updatePassword(email, newPassword);

                if (updateSuccess) {
                    Log.d(TAG, "Password updated successfully in database");
                    showSuccessDialog(email);
                } else {
                    Log.e(TAG, "Failed to update password in database");
                    showErrorDialog("Lỗi cập nhật mật khẩu trong hệ thống");
                }

                showLoading(false);
            }

            @Override
            public void onFailure(String error) {
                Log.e(TAG, "Failed to send email: " + error);
                showErrorDialog(error);
                showLoading(false);
            }
        });
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            buttonSendReset.setEnabled(false);
            buttonSendReset.setText("Đang gửi...");
        } else {
            progressBar.setVisibility(View.GONE);
            buttonSendReset.setEnabled(true);
            buttonSendReset.setText("Gửi liên kết đặt lại");
        }
    }

    private void showSuccessDialog(String email) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("✅ Thành công!")
                .setMessage("Mật khẩu mới đã được gửi tới email: " + email +
                        "\n\n📧 Vui lòng kiểm tra email (bao gồm cả thư mục spam)" +
                        "\n🔐 Đăng nhập bằng mật khẩu mới và thay đổi mật khẩu ngay lập tức")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    // Chuyển về màn hình đăng nhập
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setIcon(android.R.drawable.ic_dialog_email)
                .setCancelable(false)
                .show();
    }

    private void showErrorDialog(String errorMessage) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("❌ Lỗi")
                .setMessage(errorMessage + "\n\nVui lòng thử lại sau hoặc liên hệ hỗ trợ.")
                .setPositiveButton("Thử lại", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Quay lại", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showInfoDialog(String title, String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}