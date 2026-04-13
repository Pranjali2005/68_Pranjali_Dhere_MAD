package com.example.checkbox;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    CheckBox checkJava, checkPython, checkCpp;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkJava = findViewById(R.id.checkJava);
        checkPython = findViewById(R.id.checkPython);
        checkCpp = findViewById(R.id.checkCpp);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder result = new StringBuilder("Selected: ");

                if (checkJava.isChecked()) {
                    result.append("Java ");
                }
                if (checkPython.isChecked()) {
                    result.append("Python ");
                }
                if (checkCpp.isChecked()) {
                    result.append("C++ ");
                }

                Toast.makeText(MainActivity.this,
                        result.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}