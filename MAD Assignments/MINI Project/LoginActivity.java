package com.example.final_project.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;
import com.example.final_project.database.DBHelper;

public class LoginActivity extends AppCompatActivity {

    EditText email, pass;
    Button loginBtn;
    TextView signup;
    DBHelper db;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signup = findViewById(R.id.signup);

        // ================= LOGIN =================
        loginBtn.setOnClickListener(v -> {

            String e = email.getText().toString().trim();
            String p = pass.getText().toString().trim();

            if (e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean ok = db.login(e, p);

            if (ok) {

                // GET USER ROLE
                String role = db.getRole(e, p);

                // SAVE SESSION
                SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                sp.edit()
                        .putString("email", e)
                        .putString("role", role)
                        .apply();

                Toast.makeText(this, "Login Successful (" + role + ")", Toast.LENGTH_SHORT).show();

                Intent intent;

                // ================= ROLE NAVIGATION =================
                if (role.equalsIgnoreCase("admin")) {

                    intent = new Intent(this, AdminActivity.class);

                } else if (role.equalsIgnoreCase("authority")) {

                    intent = new Intent(this, AuthorityActivity.class);

                } else {

                    intent = new Intent(this, HomeActivity.class);
                }

                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Invalid Login", Toast.LENGTH_SHORT).show();
            }
        });

        // ================= SIGNUP =================
        signup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }
}