package com.example.backgroundchange;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout mainLayout;
    Button btnRed, btnGreen, btnBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.mainLayout);

        btnRed = findViewById(R.id.btnRed);
        btnGreen = findViewById(R.id.btnGreen);
        btnBlue = findViewById(R.id.btnBlue);

        btnRed.setOnClickListener(v ->
                mainLayout.setBackgroundColor(Color.RED));

        btnGreen.setOnClickListener(v ->
                mainLayout.setBackgroundColor(Color.GREEN));

        btnBlue.setOnClickListener(v ->
                mainLayout.setBackgroundColor(Color.BLUE));
    }
}