package com.gm.habitalarm.data;

import androidx.room.Embedded;

public class HabitWithRepetition {
    @Embedded
    public Habit habit;
    @Embedded
    public Repetition repetition;
}
