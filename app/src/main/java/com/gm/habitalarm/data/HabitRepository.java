package com.gm.habitalarm.data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.gm.habitalarm.workers.NotificationWorker;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.Completable;
import io.reactivex.Notification;
import io.reactivex.Single;

@Singleton
public class HabitRepository {

    private Context context;
    private HabitDao habitDao;
    private ExecutorService writeExecutor;

    @Inject
    public HabitRepository(
            @ApplicationContext Context context,
            HabitDao habitDao,
            ExecutorService writeExecutor) {
        this.context = context;
        this.habitDao = habitDao;
        this.writeExecutor = writeExecutor;
    }

    public LiveData<Habit> getHabitById(long id) {
        return habitDao.getHabitById(id);
    }

    public LiveData<Habit> getHabitByName(String name) {
        return habitDao.getHabitByName(name);
    }

    public LiveData<HabitWithRepetitions> getHabitWithRepetitionsById(long id) {
        return habitDao.getHabitWithRepetitionsById(id);
    }

    public LiveData<List<Habit>> getHabitsByDayOfWeek(DayOfWeek day) {
        int value = day.getValue() % 7;
        return habitDao.getHabitsByDay(value);
    }

    public LiveData<List<HabitWithRepetitions>> getHabitsWithRepetitionsByDayOfWeek(DayOfWeek day) {
        int value = day.getValue() % 7;
        return habitDao.getHabitsWithRepetitionsByDay(value);
    }

    public LiveData<List<HabitWithRepetition>> getHabitsWithRepetitionsByDate(LocalDate date) {
        int day = date.getDayOfWeek().getValue() % 7;
        long epochDay = date.toEpochDay();
        return habitDao.getHabitsWithRepetitionsByDayAndDate(day, epochDay);
    }

    public LiveData<List<HabitWithRepetitions>> getHabitsWithRepetitions() {
        return habitDao.getHabitsWithRepetitions();
    }

    public LiveData<List<Habit>> getHabits() {
        return habitDao.getHabits();
    }

    public void addHabit(Habit habit) {
        writeExecutor.execute(() -> {
            long habitId = habitDao.insert(habit);
            if(habitId != 0 && habit.notifyingTime != null) {
                habit.id = habitId;
                NotificationWorker.enqueueWorkerWithHabit(context, habit);
            }
        });
    }

    public void updateHabit(Habit habit) {
        writeExecutor.execute(() -> {
            int updatedItemCount = habitDao.update(habit);
            if(updatedItemCount != 0 && habit.notifyingTime != null) {
                NotificationWorker.enqueueWorkerWithHabit(context, habit);
            }
        });
    }

    public void removeHabit(Habit habit) {
        writeExecutor.execute(() -> {
            int removedItemCount = habitDao.delete(habit);
            if(removedItemCount != 0 && habit.notifyingTime != null) {
                NotificationWorker.cancelWorkerWithHabit(context, habit.id);
            }
        });
    }
}
