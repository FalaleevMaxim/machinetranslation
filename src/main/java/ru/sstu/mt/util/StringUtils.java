package ru.sstu.mt.util;

public class StringUtils {
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String emptyIfNull(String s) {
        return s == null ? "" : s;
    }

    public static String capitalize(String s) {
        if (s == null || s.isEmpty()) return "";
        if (s.length() == 1) return s.toUpperCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
