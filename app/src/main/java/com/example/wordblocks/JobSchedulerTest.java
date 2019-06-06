package com.example.wordblocks;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.app.job.JobWorkItem;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.List;

public class JobSchedulerTest extends JobService {


    @Override
    public boolean onStartJob(JobParameters params) {
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "channel1")
                        .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
                        .setContentTitle("Words for today:")
                        //.setContentText("Hello World!\nsdgdgf\nsfdgsdfgsd\nsdfgsdfg\ndfgsd")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Much \nlonger \ntext that \ncannot \nfit one \nline..."))
                        .setWhen(3000)
                        //.setPriority(NotificationCompat.PRIORITY_HIGH)
                        // Set the intent that will fire when the user taps the notification
                        //.setContentIntent(pendingIntent)
                        .setAutoCancel(false);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(15567, builder.build());

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();



// notificationId is a unique int for each notification that you must define

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
