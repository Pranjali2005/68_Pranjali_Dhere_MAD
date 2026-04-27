package com.example.final_project.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;

public class HomeActivity extends AppCompatActivity {

    String email;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_home);

        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        email = sp.getString("email", "");

        TextView userEmail = findViewById(R.id.userEmail);
        userEmail.setText(email);

        // LOGOUT
        findViewById(R.id.logoutBtn).setOnClickListener(v -> logout());

        findViewById(R.id.addComplaint).setOnClickListener(v ->
                startActivity(new Intent(this, AddComplaintActivity.class)));

        findViewById(R.id.viewComplaint).setOnClickListener(v ->
                startActivity(new Intent(this, ViewComplaintActivity.class)));
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to login
        moveTaskToBack(true);
    }

    private void logout() {
        getSharedPreferences("user", MODE_PRIVATE).edit().clear().apply();
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}