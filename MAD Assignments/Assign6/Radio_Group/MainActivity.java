package com.example.radio_group;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.r1) {
                Toast.makeText(this, "Android selected", Toast.LENGTH_SHORT).show();
            }
            else if (checkedId == R.id.r2) {
                Toast.makeText(this, "Java selected", Toast.LENGTH_SHORT).show();
            }
            else if (checkedId == R.id.r3) {
                Toast.makeText(this, "Kotlin selected", Toast.LENGTH_SHORT).show();
            }

        });
    }
}