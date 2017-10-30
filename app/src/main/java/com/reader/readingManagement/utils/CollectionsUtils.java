package com.reader.readingManagement.utils;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naver on 2017. 1. 29..
 */

public class CollectionsUtils {

    public static boolean isEmpty(Map<?, ?> map) {
        if (map == null) {
            return true;
        }
        return map.isEmpty();
    }

    public static boolean isEmpty(List<?> list) {
        if (list == null) {
            return true;
        }
        return list.isEmpty();
    }


    public static <E> HashMap<Integer, E> convertToMap(SparseArray<E> source) {
        HashMap<Integer, E> map = new HashMap<Integer, E>();
        if (source == null) {
            return map;
        }
        int length = source.size();
        for (int i = 0; i < length; i++) {
            map.put(source.keyAt(i), source.valueAt(i));
        }
        return map;
    }

    public static int size(List<?>... listArray) {
        int sum = 0;
        for (int i = 0; i < listArray.length; i++) {
            List<?> list = listArray[i];
            if (list != null) {
                sum = sum + list.size();
            }
        }
        return sum;
    }
}
