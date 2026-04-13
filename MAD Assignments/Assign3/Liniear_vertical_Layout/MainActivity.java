package com.example.liniear_vertical_layout;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etAge;
    RadioGroup radioGroup;
    Button btnRegister, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etAge = findViewById(R.id.etAge);
        radioGroup = findViewById(R.id.radioGroup);
        btnRegister = findViewById(R.id.btnRegister);
        btnClear = findViewById(R.id.btnClear);

        // Register Button
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String age = etAge.getText().toString();

            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton rb = findViewById(selectedId);

            String gender = (rb != null) ? rb.getText().toString() : "Not Selected";

            Toast.makeText(this,
                    "Name: " + name + "\nEmail: " + email + "\nAge: " + age + "\nGender: " + gender,
                    Toast.LENGTH_LONG).show();
        });

        // Clear Button
        btnClear.setOnClickListener(v -> {
            etName.setText("");
            etEmail.setText("");
            etPassword.setText("");
            etAge.setText("");
            radioGroup.clearCheck();
        });
    }
}