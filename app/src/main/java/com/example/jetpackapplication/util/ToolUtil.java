package com.example.jetpackapplication.util;

import android.content.Context;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class ToolUtil {
    public static <T> boolean isEmptyCollection(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <K, V> boolean isEmptyMap(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    public static void closeStream(Closeable stream) {
        if (stream == null) {
            return;
        }

        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int dp2px(Context context, int size) {
        if (context == null) {
            return (int) (size * 1.5);
        }

        return (int) (context.getResources().getDisplayMetrics().density * size + 0.5f);
    }
}
