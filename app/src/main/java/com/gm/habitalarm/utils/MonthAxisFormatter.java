package com.gm.habitalarm.utils;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class MonthAxisFormatter extends ValueFormatter {

    private final String[] months = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    public MonthAxisFormatter() {
    }

    @Override
    public String getFormattedValue(float value) {
        return months[(int)value];
    }
}
