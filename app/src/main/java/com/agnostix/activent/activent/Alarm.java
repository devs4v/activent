package com.agnostix.activent.activent;


    

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

    public class Alarm extends BroadcastReceiver 
    {    
         @Override
         public void onReceive(Context context, Intent intent) 
         {   
             PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
             PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
             wl.acquire();

             // Put here YOUR code.
             
             context.startService(new Intent(context, Background_gmail.class));
           //  Toast.makeText(context, "lol", Toast.LENGTH_SHORT).show();;
             
             
     		setResultCode(Activity.RESULT_OK);
           //  Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example

             wl.release();
         }

     public void SetAlarm(Context context)
     {
         AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
         Intent i = new Intent(context, Alarm.class);
         PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
       //  am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
         am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (long) (1000 * 60 * 0.5), pi);
   
     }

     public void CancelAlarm(Context context)
     {
         Intent intent = new Intent(context, Alarm.class);
         PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
         AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
         alarmManager.cancel(sender);
     }
 }