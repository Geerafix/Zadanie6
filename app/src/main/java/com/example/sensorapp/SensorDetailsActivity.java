package com.example.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.contentcapture.ContentCaptureManager;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        sensorTextView = findViewById(R.id.sensor_name);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(getIntent().getIntExtra("SENSOR_TYPE", -1));

        if (sensor == null) {
            sensorTextView.setText(R.string.missing_sensor);
        }
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onSensorChanged (SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];
        float[] sensorValues = sensorEvent.values;
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                float xA = sensorValues[0];
                float yA = sensorValues[1];
                float zA = sensorValues[2];
                sensorTextView.setText(getResources().getString(R.string.accelerometer_label,  xA, yA, zA));
                break;
            case Sensor.TYPE_LIGHT:
                sensorTextView.setText(getResources().getString(R.string.light_label, currentValue));
                break;
            default:
                sensorTextView.setText(R.string.missing_sensor);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        sensorManager.unregisterListener(this);
    }
}
