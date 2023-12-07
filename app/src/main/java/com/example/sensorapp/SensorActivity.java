package com.example.sensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private SensorAdapter adapter;
    private MenuItem subtitleItem;
    private boolean subtitleVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if (adapter == null) {
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_panel, menu);
        subtitleItem = menu.findItem(R.id.show_subtitle);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateSubtitle() {
        if (subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
            getSupportActionBar().setSubtitle(getString(R.string.sensors_count, sensorList.size()));
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
            getSupportActionBar().setSubtitle(null);
        }
    }


    public static class SensorHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public ImageView sensorIcon;
        public TextView sensorName;
        private Context context;
        private Sensor sensor;

        public SensorHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            itemView.setOnLongClickListener(this);

            sensorIcon = itemView.findViewById(R.id.sensor_icon);
            sensorName = itemView.findViewById(R.id.sensor_name);
        }

        private void showSensorInfoDialog() {
            SensorInfoDialog dialog = new SensorInfoDialog();
            dialog.setVendor(sensor.getVendor());
            dialog.setMaximumRange(sensor.getMaximumRange());
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "SensorInfoDialog");
        }

        @Override
        public boolean onLongClick(View v) {
            showSensorInfoDialog();
            return true;
        }

        public void setSensor(Sensor sensor) {
            this.sensor = sensor;
        }
    }

    public class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {

        private List<Sensor> sensorList;

        public SensorAdapter(List<Sensor> sensorList) {
            this.sensorList = sensorList;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_list_item, parent, false);
            return new SensorHolder(itemView, parent.getContext());
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensorList.get(position);
            holder.sensorName.setText(sensor.getName());
            holder.sensorIcon.setImageResource(R.drawable.ic_sensor);
            holder.sensorName.setTextColor(Color.GRAY);
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER || sensor.getType() == Sensor.TYPE_LIGHT) {
                holder.sensorIcon.setImageResource(R.drawable.ic_sensor_on);
                holder.sensorName.setTextColor(Color.GREEN);
            }
            holder.setSensor(sensor);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                        Intent intent = new Intent(view.getContext(), LocationActivity.class);
                        intent.putExtra("SENSOR_TYPE", sensor.getType());
                        view.getContext().startActivity(intent);
                    } else {
                        Intent intent = new Intent(view.getContext(), SensorDetailsActivity.class);
                        intent.putExtra("SENSOR_TYPE", sensor.getType());
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return sensorList.size();
        }
    }
}