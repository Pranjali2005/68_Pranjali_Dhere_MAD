package com.example.final_project.activities;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.adapter.ComplaintAdapter;
import com.example.final_project.database.DBHelper;
import com.example.final_project.model.ComplaintModel;

import java.util.ArrayList;

public class ViewComplaintActivity extends AppCompatActivity {

    RecyclerView rv;
    DBHelper db;
    ArrayList<ComplaintModel> list;
    ComplaintAdapter adapter;
    String email;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_view_complaint);

        rv = findViewById(R.id.recycler);
        db = new DBHelper(this);

        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        email = sp.getString("email", "");

        list = new ArrayList<>();

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        // ✅ CORRECT ADAPTER CALL (IMPORTANT FIX)
        adapter = new ComplaintAdapter(this, list, false, false, db,null);
        rv.setAdapter(adapter);

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {

        list.clear();

        Cursor c = db.getUserComplaints(email);

        if (c != null) {

            while (c.moveToNext()) {
                list.add(new ComplaintModel(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getString(6)
                ));
            }
            c.close();
        }

        adapter.notifyDataSetChanged();
    }
}