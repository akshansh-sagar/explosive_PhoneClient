package com.example.explosive_phoneclient;

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
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class PhoneMainActivity extends AppCompatActivity {

    private TextView txtPunchData;
    private LineChart phoneChart;

    private List<Entry> entries = new ArrayList<>();
    private int index = 0;
    private com.google.android.gms.wearable.DataClient.OnDataChangedListener dataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPunchData = findViewById(R.id.txtPunchData);
        phoneChart = findViewById(R.id.phoneChart);

        txtPunchData.setText("Waiting for Watch Data...");

        // 🟢 DATA LAYER LISTENER (FIXED)
        dataListener = dataEvents -> {

            for (DataEvent event : dataEvents) {

                if (event.getType() == DataEvent.TYPE_CHANGED &&
                        "/punch_data".equals(event.getDataItem().getUri().getPath())) {

                    DataMapItem mapItem =
                            DataMapItem.fromDataItem(event.getDataItem());

                    String data = mapItem.getDataMap().getString("punch");

                    Log.d("PHONE_DATA", "RECEIVED: " + data);

                    runOnUiThread(() -> {

                        txtPunchData.setText(data);

                        try {
                            float value = Float.parseFloat(
                                    data.replace("Punch Force: ", "")
                            );

                            updateGraph(value);

                        } catch (Exception e) {
                            Log.e("PHONE_DATA", "Parse error", e);
                        }
                    });
                }
            }
        };

        Wearable.getDataClient(this)
                .addListener(dataListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Wearable.getDataClient(this)
                .removeListener(dataListener);
    }

    private void updateGraph(float value) {

        entries.add(new Entry(index++, value));

        LineDataSet dataSet = new LineDataSet(entries, "Punch Force");

        phoneChart.setData(new LineData(dataSet));
        phoneChart.invalidate();
    }
}