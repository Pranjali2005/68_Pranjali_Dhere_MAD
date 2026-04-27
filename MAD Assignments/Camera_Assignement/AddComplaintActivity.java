package com.example.final_project.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.final_project.database.DBHelper;
import com.example.final_project.R;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddComplaintActivity extends AppCompatActivity {

    EditText desc;
    ImageView img;
    LinearLayout imagePlaceholder;

    DBHelper db;
    String email;
    String imagePath = "";

    File photoFile;
    Uri photoUri;

    ActivityResultLauncher<Uri> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_add_complaint);

        desc = findViewById(R.id.desc);
        img = findViewById(R.id.imageView);
        imagePlaceholder = findViewById(R.id.imagePlaceholder);
        db = new DBHelper(this);

        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        email = sp.getString("email", "");

        // CAMERA
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {
                        imagePath = photoFile.getAbsolutePath();
                        showImage(Uri.fromFile(photoFile));
                    } else {
                        Toast.makeText(this, "Camera cancelled",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // GALLERY
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imagePath = copyUriToFile(uri);
                        if (!imagePath.isEmpty()) {
                            showImage(Uri.fromFile(new java.io.File(imagePath)));
                        }
                    }
                }
        );

        // CAMERA BUTTON
        findViewById(R.id.cameraBtn).setOnClickListener(v -> {
            try {
                photoFile = createImageFile();
                photoUri = FileProvider.getUriForFile(
                        this,
                        "com.example.final_project.fileprovider",
                        photoFile
                );
                cameraLauncher.launch(photoUri);
            } catch (Exception e) {
                Toast.makeText(this, "Camera error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // GALLERY BUTTON
        findViewById(R.id.galleryBtn).setOnClickListener(v ->
                galleryLauncher.launch("image/*")
        );

        // SUBMIT
        findViewById(R.id.submit).setOnClickListener(v -> {

            String d = desc.getText().toString().trim();

            if (d.isEmpty()) {
                Toast.makeText(this, "Enter complaint description",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (imagePath.isEmpty()) {
                Toast.makeText(this, "Please select a photo",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String date = new SimpleDateFormat("dd-MM-yyyy HH:mm",
                    Locale.getDefault()).format(new Date());

            db.addComplaint(email, d, imagePath, date, "user");

            Toast.makeText(this, "Complaint submitted!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(AddComplaintActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });
    }

    // ================= SHOW IMAGE, HIDE PLACEHOLDER =================
    private void showImage(Uri uri) {
        imagePlaceholder.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);
        img.setImageURI(uri);
    }

    // ================= COPY GALLERY IMAGE =================
    private String copyUriToFile(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getExternalFilesDir(null),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            inputStream.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // ================= CREATE CAMERA FILE =================
    private File createImageFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        return new File(dir, "IMG_" + name + ".jpg");
    }
}