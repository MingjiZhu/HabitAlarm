package com.gm.habitalarm.ui.home;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.gm.habitalarm.data.HabitRepository;
import com.gm.habitalarm.data.HabitWithRepetitions;

import java.time.LocalDate;
import java.util.List;

public class HabitListViewModel extends ViewModel {

    private final HabitRepository habitRepository;

    public LiveData<List<HabitWithRepetitions>> getHabitsWithRepetitionsByDate(LocalDate date) {
        return habitRepository.getHabitsWithRepetitionsByDayOfWeek(date.getDayOfWeek());
    }

    @ViewModelInject
    public HabitListViewModel(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }
}
