package com.example.bai10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String valueStr = intent.getStringExtra("new_value");
        long value = 0;
        try {
            value = Long.parseLong(valueStr);
        } catch (NumberFormatException e) {
            value = -10; // hoặc xử lý theo ý bạn
        }

        Toast.makeText(
                context,
                "Broadcast Receiver catch an Intent\nThe value is: " + value,
                Toast.LENGTH_LONG
        ).show();
    }
}