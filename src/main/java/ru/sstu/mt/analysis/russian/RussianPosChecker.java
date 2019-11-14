package ru.sstu.mt.analysis.russian;

import ru.sstu.mt.sklonyator.enums.RussianPos;

/**
 * Интерфейс получения части речи для слова на русском языке
 */
public interface RussianPosChecker {
    RussianPos getPos(String word);
}
