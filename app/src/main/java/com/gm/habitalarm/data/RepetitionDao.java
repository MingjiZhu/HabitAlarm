package com.gm.habitalarm.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RepetitionDao {

    @Query("SELECT * FROM repetition WHERE repetition_habit_id = :habitId AND date = :date LIMIT 1")
    LiveData<Repetition> getRepetitionByHabitIdAndDate(long habitId, long date);

    @Query("SELECT * FROM repetition WHERE repetition_habit_id = :habitId")
    LiveData<List<Repetition>> getRepetitionsByHabitId(long habitId);

    @Insert
    void insert(Repetition repetition);

    @Update
    void update(Repetition repetition);

    @Delete
    void delete(Repetition repetition);
}
