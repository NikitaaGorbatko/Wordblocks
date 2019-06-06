package com.example.wordblocks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, TestService.class);
        //context.startActivity(intent);
        System.out.println("asdlfj;asdjfk;askdjf;alksdf\n\n\n\nasdfjasdlfjkaflkajsdhflkasjf\n\n\n\n");
        context.startService(startServiceIntent);
    }

}