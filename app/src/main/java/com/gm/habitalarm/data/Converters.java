package com.gm.habitalarm.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalTime;

public class Converters {

    @TypeConverter
    public static Long localDateToLong(@Nullable LocalDate date) {
        if(date != null) {
            return date.toEpochDay();
        }
        return null;
    }

    @TypeConverter
    public static LocalDate longToLocalDate(@Nullable Long epochDay) {
        if(epochDay != null) {
            return LocalDate.ofEpochDay(epochDay);
        }
        return null;
    }

    @TypeConverter
    public static Integer localTimeToLong(@Nullable LocalTime time) {
        if(time != null) {
            return time.toSecondOfDay();
        }
        return null;
    }

    @TypeConverter
    public static LocalTime longToLocalTime(@Nullable Integer epochTime) {
        if(epochTime != null) {
            return LocalTime.ofSecondOfDay(epochTime);
        }
        return null;
    }

    @TypeConverter
    public static int booleanArrayToInt(@NonNull boolean[] booleans) {
        int packedValue = 0;
        int current = 1;
        for (boolean b : booleans) {
            if(b) {
                packedValue |= current;
            }
            current <<= 1;
        }
        return packedValue;
    }

    @TypeConverter
    public static boolean[] intToBooleanArray(int packedValue) {
        boolean[] booleans = new boolean[7];
        int current = 1;
        for (int i = 0; i < 7; i++) {
            if((packedValue & current) != 0) {
                booleans[i] = true;
            }
            current <<= 1;
        }
        return booleans;
    }
}
