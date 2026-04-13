package com.example.registration_form;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etAddress;
    Button btnRegister, btnSubmit;

    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> phones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);

        btnRegister = findViewById(R.id.btnRegister);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnRegister.setOnClickListener(v -> {

            String name = etName.getText().toString();
            String phone = etPhone.getText().toString();

            names.add(name);
            phones.add(phone);

            etName.setText("");
            etEmail.setText("");
            etPhone.setText("");
            etAddress.setText("");
        });

        btnSubmit.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, SecondActivity.class);

            intent.putStringArrayListExtra("names", names);
            intent.putStringArrayListExtra("phones", phones);

            startActivity(intent);
        });
    }
}