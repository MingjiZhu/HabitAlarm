package com.gm.habitalarm.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;

import com.gm.habitalarm.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Executors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class HabitDaoTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private HabitAlarmDatabase db;
    private HabitDao habitDao;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();

        db = Room.inMemoryDatabaseBuilder(context, HabitAlarmDatabase.class)
                .build();
        habitDao = db.habitDao();
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void getHabits_habit_habitWithSameValues() throws Exception {

        LocalTime localTime = LocalTime.now();
        LocalTime expected1 =
                LocalTime.of(localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        String expected2 = "reading a book";
        boolean expected3 = true;

        Habit provided = new Habit();
        provided.notifyingTime = expected1;
        provided.name = expected2;
        provided.days[DayOfWeek.MONDAY.getValue()] = expected3;

        habitDao.insert(provided);
        List<Habit> habits = LiveDataTestUtil.getOrAwaitValue(habitDao.getHabits());

        Habit returnedHabit = habits.get(0);
        LocalTime result1 = returnedHabit.notifyingTime;
        String result2 = returnedHabit.name;
        boolean result3 = returnedHabit.days[DayOfWeek.MONDAY.getValue()];

        assertThat(result1, is(expected1));
        assertThat(result2, is(expected2));
        assertThat(result3, is(expected3));
    }

    @Test
    public void getHabitById_habit_habitWithSameValues() throws Exception {

        LocalTime localTime = LocalTime.now();
        LocalTime expected1 =
                LocalTime.of(localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        String expected2 = "reading a book";
        boolean expected3 = true;

        Habit provided = new Habit();
        provided.id = 1;
        provided.notifyingTime = expected1;
        provided.name = expected2;
        provided.days[DayOfWeek.MONDAY.getValue()] = expected3;

        habitDao.insert(provided);
        Habit returnedHabit = LiveDataTestUtil.getOrAwaitValue(habitDao.getHabitById(1));

        LocalTime result1 = returnedHabit.notifyingTime;
        String result2 = returnedHabit.name;
        boolean result3 = returnedHabit.days[DayOfWeek.MONDAY.getValue()];

        assertThat(result1, is(expected1));
        assertThat(result2, is(expected2));
        assertThat(result3, is(expected3));
    }

    @Test
    public void getHabitByName_habit_habitWithSameValues() throws Exception {

        LocalTime localTime = LocalTime.now();
        LocalTime expected1 =
                LocalTime.of(localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        String expected2 = "reading a book";
        boolean expected3 = true;

        Habit provided = new Habit();
        provided.notifyingTime = expected1;
        provided.name = expected2;
        provided.days[DayOfWeek.MONDAY.getValue()] = expected3;

        habitDao.insert(provided);
        Habit returnedHabit = LiveDataTestUtil.getOrAwaitValue(habitDao.getHabitByName(expected2));

        LocalTime result1 = returnedHabit.notifyingTime;
        String result2 = returnedHabit.name;
        boolean result3 = returnedHabit.days[1];

        assertThat(result1, is(expected1));
        assertThat(result2, is(expected2));
        assertThat(result3, is(expected3));
    }

    @Test
    public void getHabitByDayOfWeek_habit_habitWithSameValues() throws Exception {

        LocalTime localTime = LocalTime.now();
        LocalTime expected1 =
                LocalTime.of(localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        String expected2 = "reading a book";
        boolean expected3 = true;

        Habit provided = new Habit();
        provided.notifyingTime = expected1;
        provided.name = expected2;
        provided.days[DayOfWeek.MONDAY.getValue()] = expected3;

        habitDao.insert(provided);
        List<Habit> habits =
                LiveDataTestUtil.getOrAwaitValue(habitDao.getHabitsByDay(DayOfWeek.MONDAY.getValue()));

        Habit returnedHabit = habits.get(0);
        LocalTime result1 = returnedHabit.notifyingTime;
        String result2 = returnedHabit.name;
        boolean result3 = returnedHabit.days[DayOfWeek.MONDAY.getValue()];

        assertThat(result1, is(expected1));
        assertThat(result2, is(expected2));
        assertThat(result3, is(expected3));
    }

    @Test
    public void getHabitsWithRepetitions_habitAndRepetition_habitAndRepetitionWithSameValues()
            throws Exception {

        LocalTime localTime = LocalTime.now();
        LocalTime expected1 =
                LocalTime.of(localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        String expected2 = "reading a book";
        boolean expected3 = true;
        LocalDate expected4 = LocalDate.now();
        long expected5 = 3;

        Habit provided1 = new Habit();
        provided1.id = expected5;
        provided1.notifyingTime = expected1;
        provided1.name = expected2;
        provided1.days[DayOfWeek.MONDAY.getValue()] = expected3;

        Repetition provided2 = new Repetition();
        provided2.habitId = expected5;
        provided2.date = expected4;

        habitDao.insert(provided1);
        db.repetitionDao().insert(provided2);
        List<HabitWithRepetitions> habits =
                LiveDataTestUtil.getOrAwaitValue(habitDao.getHabitsWithRepetitions());

        Habit returnedHabit = habits.get(0).habit;
        Repetition returnedRepetition = habits.get(0).repetitions.get(0);

        LocalTime result1 = returnedHabit.notifyingTime;
        String result2 = returnedHabit.name;
        boolean result3 = returnedHabit.days[DayOfWeek.MONDAY.getValue()];
        LocalDate result4 = returnedRepetition.date;
        long result5 = returnedRepetition.habitId;

        assertThat(result1, is(expected1));
        assertThat(result2, is(expected2));
        assertThat(result3, is(expected3));
        assertThat(result4, is(expected4));
        assertThat(result5, is(expected5));
    }

    @Test
    public void getHabitsWithRepetitions_habitsAndRepetitions_habitAndRepetitionWithSameValues()
            throws Exception {

        LocalTime localTime = LocalTime.now();
        LocalTime expected1 =
                LocalTime.of(localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        String expected2 = "reading a book";
        boolean expected3 = true;
        LocalDate expected4 = LocalDate.now();
        long expected5 = 3;

        Habit provided1 = new Habit();
        provided1.id = expected5;
        provided1.notifyingTime = expected1;
        provided1.name = expected2;
        provided1.days[DayOfWeek.MONDAY.getValue()] = expected3;

        Habit provided2 = new Habit();
        provided2.id = 5;
        provided2.notifyingTime = expected1.plusHours(3);
        provided2.name = "workout";
        provided2.days[DayOfWeek.MONDAY.getValue()] = expected3;

        Repetition provided3 = new Repetition();
        provided3.habitId = expected5;
        provided3.date = expected4;
        provided3.state = 2;

        Repetition provided4 = new Repetition();
        provided4.habitId = expected5;
        provided4.date = expected4.plusDays(2);
        provided4.state = 3;

        habitDao.insert(provided1);
        habitDao.insert(provided2);
        db.repetitionDao().insert(provided3);
        db.repetitionDao().insert(provided4);

        List<HabitWithRepetition> habits =
                LiveDataTestUtil.getOrAwaitValue(
                        habitDao.getHabitsWithRepetitionsByDayAndDate(
                                DayOfWeek.MONDAY.getValue(), expected4.toEpochDay()));

//        List<HabitWithRepetitions> habits =
//                LiveDataTestUtil.getOrAwaitValue(habitDao.getHabitsWithRepetitions());
//
//        Habit returnedHabit = habits.get(0).habit;
//        Repetition returnedRepetition = habits.get(0).repetitions.get(0);
//
//        LocalTime result1 = returnedHabit.notifyingTime;
//        String result2 = returnedHabit.name;
//        boolean result3 = returnedHabit.days[DayOfWeek.MONDAY.getValue()];
//        LocalDate result4 = returnedRepetition.date;
//        long result5 = returnedRepetition.habitId;
//
//        assertThat(result1, is(expected1));
//        assertThat(result2, is(expected2));
//        assertThat(result3, is(expected3));
//        assertThat(result4, is(expected4));
//        assertThat(result5, is(expected5));
    }
}
