package com.gm.habitalarm.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HabitRepositoryTest {

    private HabitRepository repository;
    private HabitDao dao = mock(HabitDao.class);

    @Before
    public void setUp() {
        HabitAlarmDatabase db = mock(HabitAlarmDatabase.class);
        when(db.habitDao()).thenReturn(dao);
        when(db.runInTransaction((Callable<Object>) ArgumentMatchers.any())).thenCallRealMethod();
        repository = new HabitRepository(db.habitDao(), HabitAlarmDatabase.databaseWriteExecutor);
    }

    @Test
    public void getHabitByName_habit_habitWIthSameValues() {
        Habit expected = new Habit();
        expected.name = "reading";
        expected.days[0] = true;

        MutableLiveData<Habit> data = new MutableLiveData<Habit>(expected);
        when(dao.getHabitByName("reading")).thenReturn(data);

        Habit result = repository.getHabitByName("reading").getValue();
        verify(dao).getHabitByName("reading");

        assertThat(result, is(expected));
    }

    @Test
    public void getHabitsWithRepetitionsByDayOfWeek_habitAndRepetition_habitAndRepetitionWithSameValues() {
        Habit expected1 = new Habit();
        expected1.id = 1;
        expected1.name = "reading";
        expected1.days[0] = true;
        expected1.notifyingTime = LocalTime.now();

        Repetition expected2 = new Repetition();
        expected2.habitId = 1;
        expected2.date = LocalDate.of(2020, 11, 22);

        HabitWithRepetitions providedHabitWithRepetitions = new HabitWithRepetitions();
        providedHabitWithRepetitions.habit = expected1;
        providedHabitWithRepetitions.repetitions = Arrays.asList(expected2);

        MutableLiveData<List<HabitWithRepetitions>> data =
                new MutableLiveData<>(Arrays.asList(providedHabitWithRepetitions));
        when(dao.getHabitsWithRepetitionsByDay(0)).thenReturn(data);

        LiveData<List<HabitWithRepetitions>> result =
                repository.getHabitsWithRepetitionsByDayOfWeek(DayOfWeek.SUNDAY);
        verify(dao).getHabitsWithRepetitionsByDay(0);

        Habit result1 = result.getValue().get(0).habit;
        Repetition result2 = result.getValue().get(0).repetitions.get(0);

        assertThat(result1, is(expected1));
        assertThat(result2, is(expected2));
    }
}