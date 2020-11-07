package com.example.broadrec_vlad_udrescu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private PowerConnectionReceiver powerConnectionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        powerConnectionReceiver = new PowerConnectionReceiver();

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        String chargingMode = usbCharge ? " by USB" : acCharge ? " by AC" : "";

        TextView textViewBattery = findViewById(R.id.BatteryStatus);
        String batteryStatusM = powerConnectionReceiver.getChargingStatus() + powerConnectionReceiver.getCapacityStatus() + chargingMode;
        textViewBattery.setText(batteryStatusM);
    }
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channelID", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    protected void onStart() {
        registerReceiver(powerConnectionReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();
    }


    @Override
    protected void onStop() {
        unregisterReceiver(powerConnectionReceiver);
        super.onStop();
    }
}