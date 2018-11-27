package com.example.restatrunservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "khoi dong", Toast.LENGTH_SHORT).show();
        Intent service = new Intent(context, MsgPushService.class);
        context.startService(service);

    }
}
