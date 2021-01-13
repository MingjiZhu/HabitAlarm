package com.gm.habitalarm.utils;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class PercentAxisValueFormatter extends ValueFormatter {

    public PercentAxisValueFormatter() {
    }

    @Override
    public String getFormattedValue(float value) {
        return (int)value + "%";
    }
}