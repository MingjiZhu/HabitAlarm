package com.gm.habitalarm.utils;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class WeekAxisValueFormatter extends ValueFormatter {

    private final List<LocalDate> weeks;
    private Month previousMonth;
    private int previousYear;

    public WeekAxisValueFormatter(List<LocalDate> weeks) {
        this.weeks = weeks;
    }

    @Override
    public String getFormattedValue(float value) {

        String formattedValue;
        LocalDate date = weeks.get((int)value);
        if(value == 0f || previousYear != date.getYear()) {
            previousYear = date.getYear();
            previousMonth = date.getMonth();
            formattedValue = previousMonth.toString().substring(0, 3) + '\n' + date.getYear();
        } else if (previousMonth == date.getMonth()) {
            formattedValue = Integer.toString(date.getDayOfMonth());
        } else {
            previousMonth = date.getMonth();
            formattedValue = previousMonth.toString().substring(0, 3) + '\n' + date.getDayOfMonth();
        }

        return formattedValue;
    }
}
