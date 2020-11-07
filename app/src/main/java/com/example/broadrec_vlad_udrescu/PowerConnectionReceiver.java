package com.example.broadrec_vlad_udrescu;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PowerConnectionReceiver extends BroadcastReceiver {

    String chargingStatus, capacityStatus, chargingMode;

    public String getChargingStatus(){
        return chargingStatus.toString();
    }
    public String getCapacityStatus(){
        return capacityStatus.toString();
    }
    public String getChargingMode(){
        return chargingMode.toString();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("@charging_or_not", "Incoming action:" + intent.getAction());

        /** param defaultValue the value to be returned if no value of the desired
         * type is stored with the given name. **/

        int status_charging = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int status_capacity = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        boolean isCharging = status_charging == BatteryManager.BATTERY_STATUS_CHARGING;
//                status_charging == BatteryManager.BATTERY_STATUS_NOT_CHARGING;
        boolean isFull = status_capacity == BatteryManager.BATTERY_STATUS_FULL;
//                status_capacity == BatteryManager.BATTERY_STATUS_DISCHARGING;

//        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
//        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
//        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

//        chargingMode = usbCharge ? " by USB" : acCharge ? " by AC" : "";
        capacityStatus = isFull ? "Full" : "Not full";
        chargingStatus = isCharging ? "Charging" : "Not charging";

        Intent newIntent = new Intent(context, MainActivity.class);
        newIntent.putExtra("status", chargingStatus);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "channelID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Charging status changed!")
                .setContentText(chargingStatus)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, mBuilder.build());
    }
}
