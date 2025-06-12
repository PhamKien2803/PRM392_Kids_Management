package com.example.project_prm392_kidmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    Button btnLogout;
    Button btnManageUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btnLogout = findViewById(R.id.btnLogout);
        btnManageUsers = findViewById(R.id.btnManage_user);

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnManageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, UserManagerActivity.class);
            startActivity(intent);
        });
    }
}

