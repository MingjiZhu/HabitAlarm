package com.gm.habitalarm.workers;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class NotificationWorkerTest {

    private Context context;
    private Executor executor;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        executor = Executors.newSingleThreadExecutor();
    }

//    @Test
//    public void testSleepWorker() {
//        NotificationWorker worker =
//                (NotificationWorker) TestWorkerBuilder.from(context,
//                        SleepWorker.class,
//                        executor)
//                        .build();
//
//        Result result = worker.doWork();
//        assertThat(result, is(Result.success()));
//    }
}