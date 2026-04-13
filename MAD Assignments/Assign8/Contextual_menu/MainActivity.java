package com.example.contextual_menu;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        // Register for context menu
        registerForContextMenu(textView);
    }

    // Create Context Menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.context_menu, menu);

        menu.setHeaderTitle("Select Action");

        // Force show icons (important)
        try {
            Field[] fields = menu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("mOptionalIconsVisible")) {
                    field.setAccessible(true);
                    field.setBoolean(menu, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle item click
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.edit) {
            Toast.makeText(this, "Edit Selected", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.delete) {
            Toast.makeText(this, "Delete Selected", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.share) {
            Toast.makeText(this, "Share Selected", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onContextItemSelected(item);
    }
}