package com.example.explosive;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class PhoneMainActivity extends AppCompatActivity {

    private TextView txtPunchData;
    private LineChart phoneChart;

    private List<Entry> entries = new ArrayList<>();
    private int index = 0;

    private DataClient.OnDataChangedListener dataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPunchData = findViewById(R.id.txtPunchData);
        phoneChart = findViewById(R.id.phoneChart);

        txtPunchData.setText("Waiting for Watch Data...");

        // DATA LAYER LISTENER (FIXED + RELIABLE)
        dataListener = new DataClient.OnDataChangedListener() {

            @Override
            public void onDataChanged(DataEventBuffer dataEvents) {

                Log.d("PHONE_DEBUG", "Listener triggered. Events: " + dataEvents.getCount());

                for (DataEvent event : dataEvents) {

                    String path = event.getDataItem().getUri().getPath();

                    Log.d("PHONE_DEBUG", "Path received: " + path);

                    if ("/punch_data".equals(path)) {

                        DataMapItem mapItem =
                                DataMapItem.fromDataItem(event.getDataItem());

                        String data = mapItem.getDataMap().getString("punch");

                        Log.d("PHONE_DEBUG", "DATA RECEIVED: " + data);

                        runOnUiThread(() -> {

                            txtPunchData.setText(data);

                            try {
                                String clean = data.replace("Punch Force: ", "");
                                float value = Float.parseFloat(clean);

                                updateGraph(value);

                            } catch (Exception e) {
                                Log.e("PHONE_DEBUG", "Parse error", e);
                            }
                        });
                    }
                }
            }
        };

        // 🔴 REGISTER LISTENER
        Wearable.getDataClient(this)
                .addListener(dataListener);

        Log.d("PHONE_DEBUG", "Listener REGISTERED");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Wearable.getDataClient(this)
                .removeListener(dataListener);

        Log.d("PHONE_DEBUG", "Listener REMOVED");
    }

    private void updateGraph(float value) {

        entries.add(new Entry(index++, value));

        LineDataSet dataSet = new LineDataSet(entries, "Punch Force");

        phoneChart.setData(new LineData(dataSet));
        phoneChart.invalidate();
    }
}