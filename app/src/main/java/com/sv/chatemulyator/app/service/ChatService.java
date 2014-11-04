package com.sv.chatemulyator.app.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import com.sv.chatemulyator.app.R;
import com.sv.chatemulyator.app.activity.MainActivity;
import com.sv.chatemulyator.app.db.MessageDBHelper;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Created by IntelliJ IDEA.
 * User: SV
 * Date: 04.11.2014
 * Time: 1:27
 * For the ChatEmulyator project.
 */
public class ChatService extends Service {

    public static String NOTIFICATION = "NOTIFICATION";
    public static String HASH_TAG = "#awesome_app";
    public static String RESULT_TAG_EXTRA = "result";
    public final int DELAY_PERIOD = 20;
    public final int FLAG_SERVER_MESSAGE = 1;

    private MessageDBHelper databaseHelper;
    private NotificationManager mNotificationManager;
    private Timer mTimer = new Timer();
    private int counter = 0, incrementBy = 1;
    private int delay = 0;
    private static boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper = new MessageDBHelper(this);
        setDelayPeriod();
        mTimer.scheduleAtFixedRate(new MessageTask(), 0, 1000);
        isRunning = true;
    }

    /**
     * Display a notification in the notification bar.
     */
    private void showNotification(String message, int soundFlag) {

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                MainActivity.OPEN_ACTIVITY_FROM_NOTIFICATION, new Intent(this, MainActivity.class), 0);

        Notification notification;

        if(soundFlag == FLAG_SERVER_MESSAGE) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification = new NotificationCompat.Builder(this).setContentTitle("Message")
                    .setContentText(message).setSmallIcon(R.drawable.ic_launcher)
                    .setSound(soundUri)
                    .setContentIntent(contentIntent).build();
        } else {
            notification = new NotificationCompat.Builder(this).setContentTitle("Message")
                    .setContentText(message).setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(contentIntent).build();
        }
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(0, notification);
    }

    MyBinder binder = new MyBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        public ChatService getService() {
            return ChatService.this;
        }
    }

    public static boolean isRunning()
    {
        return isRunning;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {mTimer.cancel();}
        counter = 0;
        mNotificationManager.cancelAll();
        isRunning = false;
    }

    private class MessageTask extends TimerTask {
        @Override
        public void run() {
            try {
                counter += incrementBy;
                if(counter > delay) {
                    publishResults(getString(R.string.server_message) +counter, FLAG_SERVER_MESSAGE);
                    setDelayPeriod();
                    counter = 0;
                }
            } catch (Throwable t) {}
        }
    }

    private void setDelayPeriod() {
        this.delay = new Random().nextInt(DELAY_PERIOD);
    }

    public void publishResults(String result, int flagMessageAuth) {
        databaseHelper.insertData(result, flagMessageAuth);
        showNotification(result, flagMessageAuth);
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT_TAG_EXTRA, new Random().nextBoolean() ? result : result +HASH_TAG);
        sendBroadcast(intent);
    }
}