package com.gm.habitalarm.di;

import android.content.Context;

import com.gm.habitalarm.data.HabitAlarmDatabase;
import com.gm.habitalarm.data.HabitDao;
import com.gm.habitalarm.data.RepetitionDao;

import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ApplicationComponent.class)
public class DatabaseModule {

    @Singleton
    @Provides
    public HabitAlarmDatabase provideDatabase(@ApplicationContext Context context) {
        return HabitAlarmDatabase.getInstance(context);
    }

    @Provides
    public HabitDao provideHabitDao(HabitAlarmDatabase database) {
        return database.habitDao();
    }

    @Provides
    public RepetitionDao provideRepetitionDao(HabitAlarmDatabase database) {
        return database.repetitionDao();
    }

    @Provides
    public ExecutorService provideWriteExecutor(HabitAlarmDatabase database) {
        return database.databaseWriteExecutor;
    }
}
