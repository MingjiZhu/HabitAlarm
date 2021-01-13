package com.gm.habitalarm.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalTime;

@Entity
public class Habit {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "habit_id")
    public long id;
    @NonNull
    public String name;
    @NonNull
    public boolean[] days = new boolean[7];
    public LocalTime notifyingTime;
}
