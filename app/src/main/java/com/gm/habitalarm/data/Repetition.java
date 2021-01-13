package com.gm.habitalarm.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity
public class Repetition {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "repetition_id")
    public long id;
    @NonNull
    @ColumnInfo(name = "repetition_habit_id")
    public long habitId;
    @NonNull
    public LocalDate date;
    public int state;
}
