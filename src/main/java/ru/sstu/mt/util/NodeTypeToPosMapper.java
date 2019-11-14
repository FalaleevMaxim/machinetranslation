package ru.sstu.mt.util;

import ru.sstu.mt.sklonyator.enums.RussianPos;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static ru.sstu.mt.sklonyator.enums.RussianPos.*;

public class NodeTypeToPosMapper {
    public static Collection<RussianPos> getPosForNodeType(String type) {
        switch (type) {
            case "NN":
                return Arrays.asList(NOUN);
            case "VB":
                return Arrays.asList(VERB_PERS, VERB_INF);
            case "VBZ":
            case "VBD":
            case "VBN":
            case "VBG":
            case "VBP":
                return Collections.singletonList(VERB_PERS);
            case "JJ":
                return Arrays.asList(ADJ, ADVERB);
            default:
                return null;
        }
    }
}
