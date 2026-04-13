package com.example.progress_bar;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView textView;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);
    }

    // Button Click Method
    public void startProgress(View view) {

        Handler handler = new Handler();

        progress = 0; // Reset before starting (important)

        new Thread(() -> {
            while (progress <= 100) {
                int current = progress;

                handler.post(() -> {
                    progressBar.setProgress(current);
                    textView.setText("Progress: " + current + "%");
                });

                progress++;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Reset to 0 after completion
            handler.post(() -> {
                progressBar.setProgress(0);
                textView.setText("Progress: 0%");
            });

            progress = 0; // reset variable also
        }).start();
    }

}