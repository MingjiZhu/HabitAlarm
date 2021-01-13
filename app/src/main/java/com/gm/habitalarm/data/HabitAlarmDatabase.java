package com.gm.habitalarm.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Habit.class, Repetition.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class HabitAlarmDatabase extends RoomDatabase {

    public abstract HabitDao habitDao();
    public abstract RepetitionDao repetitionDao();

    private static volatile HabitAlarmDatabase mINSTANCE;
    private static final int NUMBER_OF_THREADS = 2;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static HabitAlarmDatabase getInstance(final Context context) {
        if(mINSTANCE == null) {
            initialize(context);
        }
        return mINSTANCE;
    }

    private static void initialize(final Context context) {
        synchronized (HabitAlarmDatabase.class) {
            if (mINSTANCE == null) {
                mINSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        HabitAlarmDatabase.class,
                        "habit_alarm_db")
                        .allowMainThreadQueries()
                        .build();
            }
        }
    }
}
