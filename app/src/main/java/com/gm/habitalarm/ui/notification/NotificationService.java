package com.gm.habitalarm.ui.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gm.habitalarm.R;
import com.gm.habitalarm.ui.home.HabitActivity;

public class NotificationService extends Service {

    private static final String CHANNEL_ID = "HabitAlarm";
    private static final String GROUP_KEY_NOTIFICATION = "com.gm.habitalarm.ui.notification.group_key";
    private static final String INTENT_KEY_ID = "com.gm.habitalarm.ui.notification.notification_id";
    private static final String INTENT_KEY_NAME = "com.gm.habitalarm.ui.notification.notification_name";
    private static final String INTENT_KEY_DAYS = "com.gm.habitalarm.ui.notification.notification_days";
    private static final String INTENT_KEY_HOUR = "com.gm.habitalarm.ui.notification.notification_hour";
    private static final String INTENT_KEY_MINUTE = "com.gm.habitalarm.ui.notification.notification_minute";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long habitId = intent.getLongExtra(INTENT_KEY_ID, 0l);
        String habitName = intent.getStringExtra(INTENT_KEY_NAME);

        Intent newIntent = new Intent(this, HabitActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, newIntent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(habitName)
                        .setContentText(getString(R.string.notification_content))
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setGroup(GROUP_KEY_NOTIFICATION)
                        .setAutoCancel(true)
                        // For Android 7.1 and lower
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(
                (int)habitId,
                builder.build());

        return super.onStartCommand(intent, flags, startId);
    }

    public static Intent createIntent(Context packageContext, long id, String name) {
        return new Intent(packageContext, NotificationService.class)
                .putExtra(INTENT_KEY_ID, id)
                .putExtra(INTENT_KEY_NAME, name);
    }

    public static Intent createIntent(
            Context packageContext,
            long id,
            String name,
            boolean[] days,
            int hour,
            int minute
            ) {
        return new Intent(packageContext, NotificationService.class)
                .putExtra(INTENT_KEY_ID, id)
                .putExtra(INTENT_KEY_NAME, name)
                .putExtra(INTENT_KEY_DAYS, days)
                .putExtra(INTENT_KEY_HOUR, hour)
                .putExtra(INTENT_KEY_MINUTE, minute);
    }
}