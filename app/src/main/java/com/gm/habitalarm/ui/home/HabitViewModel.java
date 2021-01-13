package com.gm.habitalarm.ui.home;

import android.content.Context;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

import com.gm.habitalarm.MainApplication;
import com.gm.habitalarm.data.Habit;
import com.gm.habitalarm.data.HabitRepository;
import com.gm.habitalarm.data.HabitWithRepetition;
import com.gm.habitalarm.data.HabitWithRepetitions;
import com.gm.habitalarm.data.Repetition;
import com.gm.habitalarm.data.RepetitionRepository;
import com.gm.habitalarm.utils.RepetitionState;

import java.time.LocalDate;
import java.util.List;

public class HabitViewModel extends ViewModel {

    private Context context;
    private HabitRepository habitRepository;
    private RepetitionRepository repetitionRepository;

    private final MutableLiveData<LocalDate> date = new MutableLiveData<>(LocalDate.now());

    public LiveData<LocalDate> getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date.setValue(date);
    }

    public final LiveData<List<HabitWithRepetition>> habitsWithRepetitions =
            Transformations.switchMap(
                    date,
                    (date) -> habitRepository.getHabitsWithRepetitionsByDate(date));

    @ViewModelInject
    public HabitViewModel(
            @ApplicationContext Context applicationContext,
            HabitRepository habitRepository,
            RepetitionRepository repetitionRepository) {
        context = applicationContext;
        this.habitRepository = habitRepository;
        this.repetitionRepository = repetitionRepository;
    }

    public void changeToPreviousDate() {
        LocalDate previous = date.getValue().minusDays(1);
        MainApplication app = (MainApplication)(context.getApplicationContext());
        app.soundOn();
        date.setValue(previous);
    }

    public void changeToNextDate() {
        LocalDate next = date.getValue().plusDays(1);
        MainApplication app = (MainApplication)(context.getApplicationContext());
        app.soundOn();
        date.setValue(next);
    }

    public void saveOrUpdateRepetition(Repetition repetition, long habitId, boolean isChecked) {

        if(isChecked) {
            int state = RepetitionState.DONE;

            repetition = new Repetition();
            repetition.habitId = habitId;
            repetition.date = date.getValue();
            repetition.state = state;
            repetitionRepository.addRepetition(repetition);
        } else {
            repetitionRepository.removeRepetition(repetition);
        }

        repetitionRepository.updateRepetition(repetition);
    }
}
