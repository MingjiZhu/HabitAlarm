package com.gm.habitalarm.ui.detail;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.gm.habitalarm.data.Habit;
import com.gm.habitalarm.data.HabitRepository;
import com.gm.habitalarm.data.HabitWithRepetitions;

import java.time.DayOfWeek;

public class HabitDetailViewModel extends ViewModel {

    private HabitRepository habitRepository;
    private MutableLiveData<Long> habitId = new MutableLiveData<>();
    private LiveData<HabitWithRepetitions> habitWithRepetitions =
            Transformations.switchMap(
                    habitId,
                    habitId -> habitRepository.getHabitWithRepetitionsById(habitId)
            );
    private LiveData<Habit> habit =
            Transformations.map(
                    habitWithRepetitions,
                    habitWithRepetitions -> {
                        Habit habit = null;
                        if(habitWithRepetitions != null) {
                            habit = habitWithRepetitions.habit;
                        }
                        return habit;
                    }
            );
    private LiveData<String> notifyingTime =
            Transformations.map(
                    habit,
                    habit -> {
                        if(habit != null) {
                            return habit.notifyingTime != null ?
                                    habit.notifyingTime.toString() : "NONE";
                        }
                        return null;
                    }
            );
    private LiveData<String> days =
            Transformations.map(
                    habit,
                    habit -> {
                        if(habit != null) {
                            boolean[] days = habit.days;
                            String result = days[0] ? "SUN " : "";
                            boolean isEverydayOn = true;
                            for(int i = 1; i < days.length; i++) {
                                if(days[i]) {
                                    result += DayOfWeek.of(i).toString().substring(0, 3) + ' ';
                                } else {
                                    isEverydayOn = false;
                                }
                            }
                            return isEverydayOn ? "EVERYDAY" : result;
                        }
                        return null;
                    }
            );

    @ViewModelInject
    public HabitDetailViewModel(
            HabitRepository habitRepository,
            @Assisted SavedStateHandle savedStateHandle
    ) {
        this.habitRepository = habitRepository;
    }

    public void setHabitId(long habitId) {
        this.habitId.setValue(habitId);
    }

    public LiveData<Habit> getHabit() {
        return habit;
    }

    public LiveData<String> getDays() {
        return days;
    }

    public LiveData<String> getNotifyingTime() {
        return notifyingTime;
    }

    public LiveData<HabitWithRepetitions> getHabitWithRepetitions() {
        return habitWithRepetitions;
    }

    public void removeHabit() {
        habitRepository.removeHabit(habit.getValue());
    }
}
