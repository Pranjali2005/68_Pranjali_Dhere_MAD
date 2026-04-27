package com.example.final_project.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.adapter.ComplaintAdapter;
import com.example.final_project.database.DBHelper;
import com.example.final_project.model.ComplaintModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AuthorityActivity extends AppCompatActivity {

    RecyclerView rv;
    DBHelper db;
    ArrayList<ComplaintModel> list;
    ComplaintAdapter adapter;

    int pendingComplaintId = -1;
    File photoFile;
    Uri photoUri;

    ActivityResultLauncher<Uri> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_authority);

        rv = findViewById(R.id.recycler);
        db = new DBHelper(this);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // LOGOUT
        findViewById(R.id.logoutBtn).setOnClickListener(v -> logout());

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result && photoFile != null) {
                        db.updateProof(pendingComplaintId, photoFile.getAbsolutePath());
                        pendingComplaintId = -1;
                        loadData();
                    }
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null && pendingComplaintId != -1) {
                        String path = copyUriToFile(uri);
                        if (!path.isEmpty()) {
                            db.updateProof(pendingComplaintId, path);
                            pendingComplaintId = -1;
                            loadData();
                        }
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void loadData() {
        list = new ArrayList<>();
        Cursor c = db.getAllComplaints();
        if (c != null && c.moveToFirst()) {
            do {
                String status = c.getString(4);
                if (!status.equalsIgnoreCase("RESOLVED")
                        && !status.equalsIgnoreCase("REJECTED")) {
                    list.add(new ComplaintModel(
                            c.getInt(0), c.getString(1), c.getString(2),
                            c.getString(3), c.getString(4), c.getString(5), c.getString(6)
                    ));
                }
            } while (c.moveToNext());
            c.close();
        }
        adapter = new ComplaintAdapter(this, list, false, true, db, null);
        rv.setAdapter(adapter);
    }

    public void startProofUpload(int complaintId) {
        pendingComplaintId = complaintId;
        new android.app.AlertDialog.Builder(this)
                .setTitle("Upload Proof")
                .setMessage("Choose proof photo source")
                .setPositiveButton("📷 Camera", (d, w) -> {
                    try {
                        photoFile = createImageFile();
                        photoUri = FileProvider.getUriForFile(this,
                                "com.example.final_project.fileprovider", photoFile);
                        cameraLauncher.launch(photoUri);
                    } catch (Exception e) {
                        android.widget.Toast.makeText(this,
                                "Camera error", android.widget.Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("🖼 Gallery", (d, w) -> galleryLauncher.launch("image/*"))
                .setNeutralButton("Cancel", null)
                .show();
    }

    private String copyUriToFile(Uri uri) {
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            File file = new File(getExternalFilesDir(null),
                    "proof_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            out.close(); in.close();
            return file.getAbsolutePath();
        } catch (Exception e) { e.printStackTrace(); }
        return "";
    }

    private File createImageFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return new File(dir, "PROOF_" + name + ".jpg");
    }

    private void logout() {
        getSharedPreferences("user", MODE_PRIVATE).edit().clear().apply();
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}