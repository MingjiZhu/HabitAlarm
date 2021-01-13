package com.gm.habitalarm.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HabitDao {

    @Query("SELECT * FROM habit WHERE habit_id = :id LIMIT 1")
    LiveData<Habit> getHabitById(long id);

    @Query("SELECT * FROM habit WHERE name = :name LIMIT 1")
    LiveData<Habit> getHabitByName(String name);

    @Transaction
    @Query("SELECT * FROM habit WHERE habit_id = :id LIMIT 1")
    LiveData<HabitWithRepetitions> getHabitWithRepetitionsById(long id);

    @Query("SELECT * FROM habit WHERE days & (1 << :day)")
    LiveData<List<Habit>> getHabitsByDay(int day);

    @Transaction
    @Query("SELECT * FROM habit WHERE days & (1 << :day)")
    LiveData<List<HabitWithRepetitions>> getHabitsWithRepetitionsByDay(int day);

    @Query("SELECT * FROM habit")
    LiveData<List<Habit>> getHabits();

    @Transaction
    @Query("SELECT * FROM habit")
    LiveData<List<HabitWithRepetitions>> getHabitsWithRepetitions();

    @Transaction
    @Query("SELECT * FROM habit " +
            "LEFT JOIN repetition ON repetition_habit_id = habit_id AND date = :date " +
            "WHERE days & (1 << :day)")
    LiveData<List<HabitWithRepetition>> getHabitsWithRepetitionsByDayAndDate(int day, long date);

    @Insert
    long insert(Habit habit);

    @Update
    int update(Habit habit);

    @Delete
    int delete(Habit habit);
}
