package com.example.final_project.database;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context c) {
        super(c, "ComplaintDB", null, 6); // VERSION UPDATED to 3
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // USERS TABLE
        db.execSQL(
                "CREATE TABLE users(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "email TEXT UNIQUE," +
                        "password TEXT," +
                        "role TEXT)"
        );

        // COMPLAINTS TABLE
        db.execSQL(
                "CREATE TABLE complaints(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "email TEXT," +
                        "description TEXT," +
                        "image TEXT," +
                        "status TEXT," +
                        "date TEXT," +
                        "proof_image TEXT," +
                        "created_by_role TEXT" +
                        ")"
        );

        // ✅ PRE-INSERT ADMIN ACCOUNT
        ContentValues admin = new ContentValues();
        admin.put("email", "admin@gmail.com");
        admin.put("password", "admin123");
        admin.put("role", "admin");
        db.insert("users", null, admin);

        // ✅ PRE-INSERT AUTHORITY ACCOUNT
        ContentValues authority = new ContentValues();
        authority.put("email", "authority@gmail.com");
        authority.put("password", "authority123");
        authority.put("role", "authority");
        db.insert("users", null, authority);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS complaints");
        onCreate(db);
    }

    // ================= REGISTER =================
    public boolean register(String email, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("password", password);
        cv.put("role", "user");

        return db.insert("users", null, cv) != -1;
    }

    // ================= LOGIN =================
    public boolean login(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT id FROM users WHERE email=? AND password=?",
                new String[]{email, password}
        );

        boolean ok = c.moveToFirst();
        c.close();
        return ok;
    }

    // ================= ROLE =================
    public String getRole(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT role FROM users WHERE email=? AND password=?",
                new String[]{email, password}
        );

        if (c.moveToFirst()) {
            String role = c.getString(0);
            c.close();
            return role;
        }

        c.close();
        return "user";
    }

    // ================= ADD COMPLAINT =================
    public void addComplaint(String email, String desc, String img, String date, String role) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("description", desc);
        cv.put("image", img);
        cv.put("status", "Pending");
        cv.put("date", date);
        cv.put("proof_image", "");
        cv.put("created_by_role", role);

        db.insert("complaints", null, cv);
    }

    // ================= USER COMPLAINTS =================
    public Cursor getUserComplaints(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM complaints WHERE email=? ORDER BY id DESC",
                new String[]{email}
        );
    }

    // ================= ALL COMPLAINTS =================
    public Cursor getAllComplaints() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM complaints ORDER BY id DESC",
                null
        );
    }

    // ================= AUTHORITY: ADD PROOF IMAGE =================
    public void updateProof(int id, String proofPath) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("proof_image", proofPath);
        cv.put("status", "In Review");

        db.update("complaints", cv, "id=?",
                new String[]{String.valueOf(id)});
    }

    // ================= ADMIN: MARK RESOLVED =================
    public void markResolved(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("status", "Resolved");

        db.update("complaints", cv, "id=?",
                new String[]{String.valueOf(id)});
    }

    // ================= ADMIN: REJECT =================
    public void rejectComplaint(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("status", "Rejected");

        db.update("complaints", cv, "id=?",
                new String[]{String.valueOf(id)});
    }
}