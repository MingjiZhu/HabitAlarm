package com.gm.habitalarm.workers;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.gm.habitalarm.data.Habit;
import com.gm.habitalarm.ui.notification.NotificationService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class NotificationWorker extends Worker {

    public static final String REQUEST_TAG_NAME = "habitAlarm";
    public static final String DATA_KEY_ID = "habitId";
    public static final String DATA_KEY_NAME = "habitName";
    public static final String DATA_KEY_DAYS = "habitDays";
    public static final String DATA_KEY_HOUR = "habitHour";
    public static final String DATA_KEY_MINUTE = "habitMinute";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        boolean[] days = data.getBooleanArray(DATA_KEY_DAYS);
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        if(days[today.getValue() % 7]) {
            Context context = getApplicationContext();

            long habitId = data.getLong(DATA_KEY_ID, 0l);
            String habitName = data.getString(DATA_KEY_NAME);
            Intent intent =
                    NotificationService.createIntent(
                            context,
                            data.getLong(DATA_KEY_ID, 0l),
                            data.getString(DATA_KEY_NAME));

//            Intent intent = NotificationService.createIntent(
//                    context,
//                    data.getLong(DATA_KEY_ID, 0),
//                    data.getString(DATA_KEY_NAME),
//                    days,
//                    data.getInt(DATA_KEY_HOUR, 0),
//                    data.getInt(DATA_KEY_MINUTE, 0));
            context.startService(intent);
        }

        return Result.success();
    }

    public static void enqueueWorkerWithHabit(Context context, Habit habit) {
        LocalTime notifyingTime = habit.notifyingTime;
        LocalTime time = LocalTime.now();
        if(time.isAfter(notifyingTime)) {
            time = time.minus(notifyingTime.toSecondOfDay(), ChronoUnit.SECONDS);
        } else {
            time = notifyingTime.minus(time.toSecondOfDay(), ChronoUnit.SECONDS);
        }
        time = LocalTime.of(time.getHour(), time.getMinute());

        PeriodicWorkRequest notificationRequest =
                new PeriodicWorkRequest
                        .Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
                        .addTag(REQUEST_TAG_NAME)
                        .setInitialDelay(time.toSecondOfDay() / 60, TimeUnit.MINUTES)
                        .setInputData(new Data
                                .Builder()
                                .putLong(DATA_KEY_ID, habit.id)
                                .putString(DATA_KEY_NAME, habit.name)
                                .putBooleanArray(DATA_KEY_DAYS, habit.days)
                                .putInt(DATA_KEY_HOUR, habit.notifyingTime.getHour())
                                .putInt(DATA_KEY_MINUTE, habit.notifyingTime.getMinute())
                                .build()
                        )
                        .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                REQUEST_TAG_NAME + habit.id,
                ExistingPeriodicWorkPolicy.REPLACE,
                notificationRequest);
    }

    public static void cancelWorkerWithHabit(Context context, long habitId) {
        WorkManager.getInstance(context)
                .cancelUniqueWork(REQUEST_TAG_NAME + habitId);
    }

    public static void cancelAllWorkers(Context context) {
        WorkManager.getInstance(context).cancelAllWork();
    }
}
