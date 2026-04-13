package com.example.toggle_button;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ToggleButton toggleButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton = findViewById(R.id.toggleButton);
        textView = findViewById(R.id.textView);
    }

    // Toggle Button Click Event
    public void onToggleClick(View view) {
        if (toggleButton.isChecked()) {
            textView.setText("Toggle is ON");
        } else {
            textView.setText("Toggle is OFF");
        }
    }
}