package com.example.sensorapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SensorInfoDialog extends DialogFragment {

    private String vendor;
    private float maximumRange;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String message = "Vendor: " + vendor + "\nMaximum Range: " + maximumRange;

        builder.setMessage(message)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }


    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setMaximumRange(float maximumRange) {
        this.maximumRange = maximumRange;
    }
}
