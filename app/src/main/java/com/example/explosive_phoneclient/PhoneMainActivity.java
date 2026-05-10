package com.example.explosive_phoneclient;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;

public class PhoneMainActivity extends AppCompatActivity {

    private TextView txtPunchData;

    private LineChart phoneChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        txtPunchData =
                findViewById(R.id.txtPunchData);

        phoneChart =
                findViewById(R.id.phoneChart);

        txtPunchData.setText(
                "Waiting for Watch Data..."
        );
    }
}