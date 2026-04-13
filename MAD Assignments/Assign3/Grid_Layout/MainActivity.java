package com.example.grid_layout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btn1, btn2, btn3, btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);

        btn1.setOnClickListener(v ->
                Toast.makeText(this, "Item 1 clicked", Toast.LENGTH_SHORT).show());

        btn2.setOnClickListener(v ->
                Toast.makeText(this, "Item 2 clicked", Toast.LENGTH_SHORT).show());

        btn3.setOnClickListener(v ->
                Toast.makeText(this, "Item 3 clicked", Toast.LENGTH_SHORT).show());

        btn4.setOnClickListener(v ->
                Toast.makeText(this, "Item 4 clicked", Toast.LENGTH_SHORT).show());
    }
}