package com.example.wordblocks;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class TestService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //CharSequence name = getString(R.string.channel_name);
            //String description = getString(R.string.channel_description);
            //int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel1", "name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
                .setContentTitle("Words for today:")
                //.setContentText("Hello World!\nsdgdgf\nsfdgsdfgsd\nsdfgsdfg\ndfgsd")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Much \nlonger \ntext that \ncannot \nfit one \nline..."))
                //.setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(15567, builder.build());
        //getApplicationContext().startActivity(new Intent(getApplicationContext(), MainActivity.class));
        return null;
    }
}
