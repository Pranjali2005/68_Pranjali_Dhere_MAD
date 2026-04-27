package com.example.final_project.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.adapter.ComplaintAdapter;
import com.example.final_project.database.DBHelper;
import com.example.final_project.model.ComplaintModel;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    RecyclerView rv;
    DBHelper db;
    ArrayList<ComplaintModel> list;

    TextView totalCount, pendingCount, resolvedCount, rejectedCount;

    private static final String CHANNEL_ID = "complaint_channel";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_admin);

        rv = findViewById(R.id.recycler);
        db = new DBHelper(this);
        rv.setLayoutManager(new LinearLayoutManager(this));

        totalCount = findViewById(R.id.totalCount);
        pendingCount = findViewById(R.id.pendingCount);
        resolvedCount = findViewById(R.id.resolvedCount);
        rejectedCount = findViewById(R.id.rejectedCount);

        createNotificationChannel();

        // LOGOUT
        findViewById(R.id.logoutBtn).setOnClickListener(v -> logout());
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

    private void loadData() {

        list = new ArrayList<>();
        int total = 0, pending = 0, resolved = 0, rejected = 0;

        Cursor c = db.getAllComplaints();

        if (c != null && c.moveToFirst()) {
            do {
                String status = c.getString(4);
                total++;
                switch (status.toLowerCase()) {
                    case "resolved": resolved++; break;
                    case "rejected": rejected++; break;
                    default: pending++; break;
                }
                list.add(new ComplaintModel(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getString(6)
                ));
            } while (c.moveToNext());
            c.close();
        }

        totalCount.setText(String.valueOf(total));
        pendingCount.setText(String.valueOf(pending));
        resolvedCount.setText(String.valueOf(resolved));
        rejectedCount.setText(String.valueOf(rejected));

        rv.setAdapter(new ComplaintAdapter(this, list, true, false, db, this));
    }

    // ================= SEND NOTIFICATION TO USER =================
    public void sendResolvedNotification(String userEmail) {
        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("✅ Complaint Resolved")
                .setContentText("Your complaint has been resolved by the admin.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        nm.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Complaint Updates",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for complaint status updates");
            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
    }

    private void logout() {
        getSharedPreferences("user", MODE_PRIVATE).edit().clear().apply();
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}