package com.example.final_project.utils;

import android.app.*;
import android.content.*;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    public static void show(Context c,String msg){

        NotificationManager nm=(NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification n=new NotificationCompat.Builder(c,"ch")
                .setContentTitle("Update")
                .setContentText(msg)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();

//        nm.notify(1,n);
    }
}