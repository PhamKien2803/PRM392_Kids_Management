package com.example.project_prm392_kidmanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class OrderFormActivity extends AppCompatActivity {

    private EditText edtCustomerName, edtProductName, edtPhoneNumber;
    private Button btnSubmitOrder, btnCall, btnBack;

    private static final int REQUEST_CALL_PERMISSION = 1003;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);

        edtCustomerName = findViewById(R.id.edtCustomerName);
        edtProductName = findViewById(R.id.edtProductName);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);
        btnBack = findViewById(R.id.btnBack);
        btnCall = findViewById(R.id.btnCall);

        btnSubmitOrder.setOnClickListener(view -> submitOrder());
        btnCall.setOnClickListener(view -> makePhoneCall());

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(OrderFormActivity.this, WelcomeActivity.class);
            startActivity(intent);
            // finish();
        });
    }

    private void submitOrder() {
        String name = edtCustomerName.getText().toString().trim();
        String product = edtProductName.getText().toString().trim();
        String phone = edtPhoneNumber.getText().toString().trim();

        if (name.isEmpty() || product.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.length() < 3) {
            Toast.makeText(this, "Tên khách hàng quá ngắn", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPhone(phone)) {
            Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        String message = "Đã đặt hàng thành công!\n\nKhách: " + name +
                "\nSản phẩm: " + product +
                "\nSĐT: " + phone;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // Logic bổ sung sau khi đặt hàng thành công:
        // Ví dụ: Xóa nội dung các EditText
        // edtCustomerName.setText("");
        // edtProductName.setText("");
        // edtPhoneNumber.setText("");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("^(0[35789])([0-9]{8})$");
    }

    private void makePhoneCall() {
        String phone = edtPhoneNumber.getText().toString().trim();

        if (phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPhone(phone)) {
            Toast.makeText(this, "Số điện thoại không hợp lệ để gọi", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_PERMISSION);
        } else {
            try {
                startActivity(callIntent);
            } catch (SecurityException e) {
                Toast.makeText(this, "Không thể thực hiện cuộc gọi. Lỗi bảo mật.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để gọi điện", Toast.LENGTH_SHORT).show();
            }
        }
    }
}