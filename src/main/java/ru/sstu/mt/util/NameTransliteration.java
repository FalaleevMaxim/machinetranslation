package ru.sstu.mt.util;

public class NameTransliteration {
    public static String translit(String eng) {
        return eng.replaceAll("ch", "ч")
                .replaceAll("sh", "ш")
                .replaceAll("zj", "ж")
                .replaceAll("ya", "я")
                .replaceAll("yo", "ё")
                .replaceAll("yu", "ю")
                .replaceAll("ye", "у")
                .replaceAll("a", "а")
                .replaceAll("b", "б")
                .replaceAll("c", "ц")
                .replaceAll("d", "д")
                .replaceAll("e", "е")
                .replaceAll("f", "ф")
                .replaceAll("g", "г")
                .replaceAll("h", "х")
                .replaceAll("i", "и")
                .replaceAll("j", "дж")
                .replaceAll("k", "к")
                .replaceAll("l", "л")
                .replaceAll("m", "м")
                .replaceAll("n", "н")
                .replaceAll("o", "о")
                .replaceAll("p", "п")
                .replaceAll("q", "к")
                .replaceAll("r", "р")
                .replaceAll("s", "с")
                .replaceAll("t", "т")
                .replaceAll("u", "у")
                .replaceAll("v", "в")
                .replaceAll("w", "в")
                .replaceAll("x", "кс")
                .replaceAll("y", "и")
                .replaceAll("z", "з");
    }
}
