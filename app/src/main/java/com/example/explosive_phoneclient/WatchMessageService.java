package com.example.explosive_phoneclient;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WatchMessageService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        Log.d("PHONE_SERVICE", "Message received: " + messageEvent.getPath());

        if ("/punch_data".equals(messageEvent.getPath())) {

            String data = new String(messageEvent.getData());

            Log.d("PHONE_SERVICE", "DATA: " + data);
        }
    }
}