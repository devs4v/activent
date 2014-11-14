package com.agnostix.activent.activent;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Shivam on 11/11/2014.
 */
public class WifiChangeReceiver extends BroadcastReceiver {

    private static final int NOTIF_ID = 0;
    private static DBHelper dbCon;
    private static String lastMAC = null;

    public static String[] getMacAddress(Context context, Intent intent){
        String[] pInfo = new String[5];
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(intent != null) {
            if (!intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                return null;
            }
        }

        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if(wifiInfo == null){
            return null;
        }

        String macAddress = wifiInfo.getBSSID();
        if(macAddress == null){
            return null;
        }

        dbCon = new DBHelper(context);
        String[] placeInfo = dbCon.getPlaceFromMac(macAddress);
        pInfo[0] = placeInfo[0];
        pInfo[1] = placeInfo[1];
        pInfo[2] = placeInfo[2];
        pInfo[3] = placeInfo[3];
        pInfo[4] = macAddress;
        return pInfo;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "AP changed!", Toast.LENGTH_SHORT);

        String[] placeInfo = getMacAddress(context, intent);

        String macAddress = placeInfo[4];
        if(lastMAC == null){
            lastMAC = macAddress;
        }else{
            if(macAddress.equals(lastMAC)){
                return;
            }
        }

        generateNotification(context, macAddress);
    }

    public void generateNotification(Context context, String macAddress){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.activent_logo_small)
                .setContentTitle("Connected to new AP!")
                .setContentText("Currently at: "+ macAddress);

        Intent resultIntent = new Intent(context, HomeScreen.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(EventListActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIF_ID, notificationBuilder.build());
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
    }
}
