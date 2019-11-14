package ru.sstu.mt.dictionary;

import ru.sstu.mt.sklonyator.enums.RussianPos;

import java.util.List;

public interface PosDetector {
    List<RussianPos> getPos(String word) throws Exception;
}
