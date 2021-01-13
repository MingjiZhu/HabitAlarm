package com.gm.habitalarm.data;

import org.junit.Test;

import java.time.DayOfWeek;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConvertersTest {

    @Test
    public void BooleanArrayToInt_booleansWith3Trues_11() throws Exception {
        boolean[] provided = new boolean[]{
                true, true, false, true, false, false, false
        };
        int expected = 11;

        int result = Converters.booleanArrayToInt(provided);

        assertThat(result, is(expected));
    }

    @Test
    public void IntToBooleanArray_11_booleansWith3Trues() throws Exception {
        int provided = 11;
        boolean[] expected = new boolean[]{
                true, true, false, true, false, false, false
        };
        boolean[] result = Converters.intToBooleanArray(provided);

        assertThat(result, is(expected));
    }
}