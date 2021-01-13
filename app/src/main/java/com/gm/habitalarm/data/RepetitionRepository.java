package com.gm.habitalarm.data;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RepetitionRepository {

    private RepetitionDao repetitionDao;
    private ExecutorService writeExecutor;

    @Inject
    public RepetitionRepository(RepetitionDao repetitionDao, ExecutorService writeExecutor) {
        this.repetitionDao = repetitionDao;
        this.writeExecutor = writeExecutor;
    }

    public LiveData<List<Repetition>> getRepetitionsByHabitId(long id) {
        return repetitionDao.getRepetitionsByHabitId(id);
    }

    public LiveData<Repetition> getRepetitionByHabitIdAndLocalDate(long id, LocalDate date) {
        long epochDay = date.toEpochDay();
        return repetitionDao.getRepetitionByHabitIdAndDate(id, epochDay);
    }

    public void addRepetition(Repetition repetition) {
        writeExecutor.execute(() -> {
            repetitionDao.insert(repetition);
        });
    }

    public void updateRepetition(Repetition repetition) {
        writeExecutor.execute(() -> {
            repetitionDao.update(repetition);
        });
    }

    public void removeRepetition(Repetition repetition) {
        writeExecutor.execute(() -> {
            repetitionDao.delete(repetition);
        });
    }
}
