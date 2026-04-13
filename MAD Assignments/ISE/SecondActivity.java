package com.example.registration_form;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    LinearLayout layoutUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        layoutUsers = findViewById(R.id.layoutUsers);

        Intent intent = getIntent();

        ArrayList<String> names = intent.getStringArrayListExtra("names");
        ArrayList<String> phones = intent.getStringArrayListExtra("phones");

        for (int i = 0; i < names.size(); i++) {

            String name = names.get(i);
            String phone = phones.get(i);

            TextView tv = new TextView(this);
            tv.setText("Name: " + name + "\nPhone: " + phone);
            tv.setTextSize(20);

            Button btnCall = new Button(this);
            btnCall.setText("Call");
//            btnCall.setBackgroundTintList(getColorStateList(R.color.purple_500));

            String finalPhone = phone;

            btnCall.setOnClickListener(v -> {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + finalPhone));
                startActivity(callIntent);

            });

            layoutUsers.addView(tv);
            layoutUsers.addView(btnCall);
        }
    }
}