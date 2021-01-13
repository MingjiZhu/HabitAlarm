package com.gm.habitalarm.ui.createOrEdit;

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
import com.gm.habitalarm.utils.BooleanConverters;

import java.time.LocalTime;

public class CreateOrEditViewModel extends ViewModel {

    private HabitRepository habitRepository;
    private Context context;

    private final MutableLiveData<Long> habitId = new MutableLiveData<>();
    private final LiveData<Habit> habit =
            Transformations.switchMap(
                    habitId,
                    habitId -> habitRepository.getHabitById(habitId)
            );
    private final MutableLiveData<Boolean> isEdited = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean[]> days =
            new MutableLiveData<>(new Boolean[]{false, false, false, false, false, false, false});
    private final LiveData<Boolean> areAllDaysOn =
            Transformations.map(
                    days,
                    (days) -> {
                        boolean isADayOn = false;
                        for (Boolean day : days) {
                            if(day) {
                                isADayOn = true;
                                break;
                            }
                        }
                        return !isADayOn;
                    });
    private final MutableLiveData<String> habitName = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isNotificationOn = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> hour = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> minute = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isSaved = new MutableLiveData<>(false);

    @ViewModelInject
    public CreateOrEditViewModel(@ApplicationContext Context applicationContext, HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
        context = applicationContext;
    }

    public void setHabitId(long habitId) {
        this.habitId.setValue(habitId);
    }

    public LiveData<Habit> getHabit() {
        return habit;
    }

    public MutableLiveData<Boolean[]> getDays() {
        return days;
    }

    public LiveData<Boolean> areAllDaysOn() {
        return areAllDaysOn;
    }

    public MutableLiveData<String> getHabitName() {
        return habitName;
    }

    public MutableLiveData<Boolean> isNotificationOn() {
        return isNotificationOn;
    }

    public MutableLiveData<Integer> getHour() {
        return hour;
    }

    public MutableLiveData<Integer> getMinute() {
        return minute;
    }

    public LiveData<Boolean> isSaved() {
        return isSaved;
    }

    public void initializeValuesWithHabit(Habit habit) {
        habitName.setValue(habit.name);

        Boolean[] days = BooleanConverters.primitiveBooleanArrayToBooleanArray(habit.days);
        if(checkAllDaysOn(days)) {
            changeAllDaysValuesTo(false);
        } else {
            this.days.setValue(days);
        }

        if(habit.notifyingTime != null) {
            isNotificationOn.setValue(true);
            hour.setValue(habit.notifyingTime.getHour());
            minute.setValue(habit.notifyingTime.getMinute());
        }

        isEdited.setValue(true);
    }

    public void onDayClick(int day) {
        Boolean[] days = this.days.getValue();
        days[day] = !days[day];

        if(checkAllDaysOn(days)) {
            changeAllDaysValuesTo(false);
        } else {
            this.days.setValue(days);
        }
        MainApplication app = (MainApplication)(context.getApplicationContext());
        app.soundOn();
    }

    public void onAllDaysClick() {
        if(!areAllDaysOn.getValue()) {
            changeAllDaysValuesTo(false);
        }
        MainApplication app = (MainApplication)(context.getApplicationContext());
        app.soundOn();
    }

    public void onNotificationClick(boolean isOn) {
        isNotificationOn.setValue(isOn);
        MainApplication app = (MainApplication)(context.getApplicationContext());
        app.soundOn();
    }

    public void onSaveClick() {
        MainApplication app = (MainApplication)(context.getApplicationContext());
        app.soundOn();

        if(habitName.getValue().isEmpty()) {
            return;
        }

        Habit habit = new Habit();
        habit.name = habitName.getValue();

        if(areAllDaysOn.getValue()) {
            changeAllDaysValuesTo(true);
        }

        habit.days = BooleanConverters.booleanArrayToPrimitiveArray(days.getValue());
        if(isNotificationOn.getValue()) {
            habit.notifyingTime = LocalTime.of(hour.getValue(), minute.getValue());
        }

        if(isEdited.getValue()) {
            habit.id = this.habit.getValue().id;
            habitRepository.updateHabit(habit);
        } else {
            habitRepository.addHabit(habit);
        }

        isSaved.setValue(true);
    }

    private void changeAllDaysValuesTo(boolean value) {
        Boolean[] days = this.days.getValue();
        for(int i = 0; i < days.length; i++) {
            days[i] = value;
        }

        this.days.setValue(days);
    }

    private boolean checkAllDaysOn(Boolean[] days) {
        boolean areAllDaysOn = true;
        for (Boolean day : days) {
            if(!day) {
                areAllDaysOn = false;
                break;
            }
        }
        return areAllDaysOn;
    }
}
