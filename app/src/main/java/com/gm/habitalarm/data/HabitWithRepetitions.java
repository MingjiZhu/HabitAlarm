package com.gm.habitalarm.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class HabitWithRepetitions {
    @Embedded
    public Habit habit;

    @Relation(parentColumn = "habit_id",
            entityColumn = "repetition_habit_id")
    public List<Repetition> repetitions;
}
