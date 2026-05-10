package com.example.explosive;

import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WatchDataListenerService
        extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        super.onMessageReceived(messageEvent);

        if (messageEvent.getPath().equals("/punch_data")) {

            String data =
                    new String(messageEvent.getData());

            Log.d("PHONE_RECEIVED", data);

            Intent intent =
                    new Intent("PUNCH_DATA");

            intent.putExtra("data", data);

            LocalBroadcastManager
                    .getInstance(this)
                    .sendBroadcast(intent);
        }
    }
}