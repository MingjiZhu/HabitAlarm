package com.gm.habitalarm.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.gm.habitalarm.R;
import com.gm.habitalarm.data.Habit;
import com.gm.habitalarm.data.HabitWithRepetitions;
import com.gm.habitalarm.data.Repetition;
import com.gm.habitalarm.utils.MultiLineXAxisRenderer;
import com.gm.habitalarm.utils.PercentAxisValueFormatter;
import com.gm.habitalarm.utils.WeekAxisValueFormatter;
import com.gm.habitalarm.utils.MonthAxisFormatter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class HabitLineChart extends LineChart {

    private static final DayOfWeek DEFAULT_LAST_DAY_OF_WEEK = DayOfWeek.SATURDAY;
    private static final int MAX_NUM_WEEKS_IN_MONTH = 6;
    private static final int LABEL_COUNT = 12;
    private static final int DEFAULT_TEXT_COLOR_ID = R.color.colorDarkGray;

    private boolean isMonth = false;

    @Nullable
    private Habit habit;
    @Nullable
    private List<Repetition> repetitions;
    private LineData lineDataMonth;
    private LineData lineDataWeek;

    public HabitLineChart(Context context) {
        super(context);
    }

    public HabitLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HabitLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setIsMonth(boolean isMonth) {
        this.isMonth = isMonth;
        compute();
    }

    public void setHabitWithRepetitions(HabitWithRepetitions habitWithRepetitions) {
        habit = habitWithRepetitions.habit;
        repetitions = habitWithRepetitions.repetitions;
        compute();
    }

    public void compute() {

        if(isMonth) {
            computeEntryMonths(YearMonth.now());
        } else {
            computeEntryWeeks(LocalDate.now());
        }
    }

    @Override
    protected void init() {
        super.init();

        setXAxisRenderer(new MultiLineXAxisRenderer(
                getViewPortHandler(),
                getXAxis(),
                getTransformer(YAxis.AxisDependency.LEFT)));
        mDescription.setEnabled(false);
        setTouchEnabled(false);
        setDrawGridBackground(false);

        mXAxis.setLabelCount(LABEL_COUNT);
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mXAxis.setDrawGridLines(false);
        mXAxis.setTextColor(getResources().getColor(DEFAULT_TEXT_COLOR_ID));
        mXAxis.setGranularity(1f);

        mAxisLeft.setAxisMaximum(100f);
        mAxisLeft.setAxisMinimum(0f);
        mAxisLeft.setValueFormatter(new PercentAxisValueFormatter());
        mAxisLeft.setTextColor(getResources().getColor(DEFAULT_TEXT_COLOR_ID));

        mAxisRight.setEnabled(false);
    }

    private void computeEntryWeeks(LocalDate date) {

        if(lineDataWeek == null) {
            List<Entry> entries = new ArrayList<>();

            List<DayOfWeek> dayOfWeeks = new ArrayList<>();
            for(int i = 1; i < 8; i++) {
                if(habit.days[i % 7]) {
                    dayOfWeeks.add(DayOfWeek.of(i));
                }
            }

            // find days of week in each week
            for (int week = LABEL_COUNT; week > 0; week--) {
                LocalDate localDate = date.minusWeeks(week);
                List<LocalDate> datesInWeek =
                        findDatesInWeek(dayOfWeeks, localDate, null, true);

                float percent = 0f;
                if(repetitions != null) {
                    int repetitionCountInWeek = 0;
                    for (int i = 0; i < repetitions.size(); i++) {
                        Repetition repetition = repetitions.get(i);

                        if(datesInWeek.contains(repetition.date)) {
                            repetitionCountInWeek++;
                        }
                    }

                    percent = (float) repetitionCountInWeek / datesInWeek.size() * 100;
                }

                float xValue = (float)LABEL_COUNT - week;
                Entry entryWeek = new Entry(xValue, percent);
                entries.add(entryWeek);
            }

            lineDataWeek = createDefaultLineData(entries);
        }
        setData(lineDataWeek);

        LocalDate today = LocalDate.now();
        List<LocalDate> dates = new ArrayList<>();
        for (int week = LABEL_COUNT; week > 0; week--) {
            LocalDate localDates = today.minusWeeks(week).with(DEFAULT_LAST_DAY_OF_WEEK);
            dates.add(localDates);
        }

        mXAxis.setValueFormatter(new WeekAxisValueFormatter(dates));

        invalidate(); //refresh
    }

    private void computeEntryMonths(YearMonth yearMonth) {

        if(lineDataMonth == null) {
            List<Entry> entries = new ArrayList<>();

            List<DayOfWeek> dayOfWeeks = new ArrayList<>();
            for(int i = 1; i < 8; i++) {
                if(habit.days[i % 7]) {
                    dayOfWeeks.add(DayOfWeek.of(i));
                }
            }

            // find days of week in each month
            int year = yearMonth.getYear();
            for (int month = 1; month < 13; month++) {
                YearMonth monthInYear = YearMonth.of(year, Month.of(month));
                List<LocalDate> datesInMonth = findDatesInMonth(monthInYear, dayOfWeeks);

                float percent = 0f;
                if(repetitions != null) {
                    int repetitionCountInMonth = 0;
                    for (int i = 0; i < repetitions.size(); i++) {
                        Repetition repetition = repetitions.get(i);

                        if(datesInMonth.contains(repetition.date)) {
                            repetitionCountInMonth++;
                        }
                    }

                    percent = (float) repetitionCountInMonth / datesInMonth.size() * 100;
                }

                float xValue = (float)month - 1f;
                Entry entryMonth = new Entry(xValue, percent);
                entries.add(entryMonth);
            }

            lineDataMonth = createDefaultLineData(entries);
        }
        setData(lineDataMonth);

        mXAxis.setValueFormatter(new MonthAxisFormatter());

        invalidate(); //refresh
    }

    private List<LocalDate> findDatesInMonth(YearMonth monthYear, List<DayOfWeek> dayOfWeeks) {

        List<LocalDate> datesInMonth = new ArrayList<>();
        Month month = monthYear.getMonth();
        for(int week = 1; week <= MAX_NUM_WEEKS_IN_MONTH; week++) {
            LocalDate randomDateInWeek = monthYear.atDay(1).plusWeeks(week - 1);
            List<LocalDate> datesInWeek =
                    findDatesInWeek(dayOfWeeks, randomDateInWeek, month, false);
            datesInMonth.addAll(datesInWeek);
        }

        return datesInMonth;
    }

    private List<LocalDate> findDatesInWeek(
            List<DayOfWeek> dayOfWeeks,
            LocalDate dateInWeek,
            @Nullable Month month,
            boolean isDateInDifferentMonthIncluded) {

        List<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i < dayOfWeeks.size(); i++) {
            LocalDate date = dateInWeek.with(dayOfWeeks.get(i));
            if(!isDateInDifferentMonthIncluded && date.getMonth() != month) {
                continue;
            }

            dates.add(date);
        }

        return dates;
    }

    private LineData createDefaultLineData(List<Entry> entries) {

        LineDataSet lineDataSet = new LineDataSet(entries, "Line Chart");
        lineDataSet.setColor(getResources().getColor(R.color.colorHotPink));
        lineDataSet.setLineWidth(3f);
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setCircleColor(getResources().getColor(R.color.colorHotPink));
        lineDataSet.setDrawValues(false);
        lineDataSet.setLabel("");
        lineDataSet.setForm(Legend.LegendForm.NONE);
        return new LineData(lineDataSet);
    }
}
