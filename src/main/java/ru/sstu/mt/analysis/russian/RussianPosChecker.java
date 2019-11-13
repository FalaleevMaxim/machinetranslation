package ru.sstu.mt.analysis.russian;

import ru.sstu.mt.sklonyator.enums.RussianPoS;

/**
 * Интерфейс получения части речи для слова на русском языке
 */
public interface RussianPosChecker {
    RussianPoS getPos(String word);
}
