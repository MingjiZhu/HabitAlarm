package com.gm.habitalarm.utils;

public class BooleanConverters {

    public static boolean[] booleanArrayToPrimitiveArray(Boolean[] array) {
        int elementCount = array.length;
        boolean[] result = new boolean[elementCount];
        for (int i = 0; i < elementCount; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static Boolean[] primitiveBooleanArrayToBooleanArray(boolean[] array) {
        int elementCount = array.length;
        Boolean[] result = new Boolean[elementCount];
        for (int i = 0; i < elementCount; i++) {
            result[i] = Boolean.valueOf(array[i]);
        }
        return result;
    }
}
