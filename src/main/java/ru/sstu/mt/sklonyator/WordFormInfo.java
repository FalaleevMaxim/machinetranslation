package ru.sstu.mt.sklonyator;


import ru.sstu.mt.sklonyator.enums.RussianGrammem;
import ru.sstu.mt.sklonyator.enums.RussianPos;

import java.util.List;

public class WordFormInfo {
    private String word;
    private RussianPos pos;
    private List<RussianGrammem> grammems;

    public WordFormInfo() {
    }

    public WordFormInfo(String word, RussianPos pos, List<RussianGrammem> grammems) {
        this.word = word;
        this.pos = pos;
        this.grammems = grammems;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public RussianPos getPos() {
        return pos;
    }

    public void setPos(RussianPos pos) {
        this.pos = pos;
    }

    public List<RussianGrammem> getGrammems() {
        return grammems;
    }

    public void setGrammems(List<RussianGrammem> grammems) {
        this.grammems = grammems;
    }
}
