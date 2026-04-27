package com.example.final_project.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;
import com.example.final_project.database.DBHelper;

public class SignupActivity extends AppCompatActivity {

    EditText e, p;
    Button signupBtn;
    DBHelper db;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_signup);

        db = new DBHelper(this);

        e = findViewById(R.id.email);
        p = findViewById(R.id.password);
        signupBtn = findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(v -> {

            String email = e.getText().toString().trim();
            String pass = p.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ check insert result (VERY IMPORTANT)
            boolean inserted = db.register(email, pass);

            if (!inserted) {
                Toast.makeText(this, "Signup Failed (Email may already exist)", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
            sp.edit()
                    .putString("email", email)
                    .putBoolean("isLoggedIn", true)
                    .apply();

            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(SignupActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });
    }
}