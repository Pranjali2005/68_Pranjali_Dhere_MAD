package com.example.all_assign;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] ids = {
                R.id.ass1, R.id.ass2, R.id.ass3, R.id.ass4, R.id.ass5,
                R.id.ass6, R.id.ass7, R.id.ass8, R.id.ass9, R.id.ass10, R.id.ass11
        };

        for (int i = 0; i < ids.length; i++) {
            int num = i + 1;
            TextView tv = findViewById(ids[i]);
            tv.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, Assign_Activity.class);
                intent.putExtra("title", "Assignment " + num);
                startActivity(intent);
            });
        }
    }
}