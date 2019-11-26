package ru.sstu.mt.intermediate.model;

import ru.sstu.mt.sklonyator.enums.GrammemCategory;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;

import java.util.Collection;
import java.util.EnumMap;

import static ru.sstu.mt.sklonyator.enums.GrammemCategory.PERSON;
import static ru.sstu.mt.sklonyator.enums.GrammemCategory.TENSE;
import static ru.sstu.mt.sklonyator.enums.RussianGrammem.*;

public class GrammemsHolder {
    private EnumMap<GrammemCategory, RussianGrammem> grammems = new EnumMap<>(GrammemCategory.class);

    /**
     * Добавляет граммему, перезаписывая граммему такого же типа
     */
    public void add(RussianGrammem grammem) {
        grammems.put(grammem.category, grammem);
    }

    /**
     * Добавляет граммему только если нет граммемы того же типа
     */
    public boolean addIfNone(RussianGrammem grammem) {
        if (grammems.containsKey(grammem.getCategory())) {
            return false;
        }
        grammems.put(grammem.category, grammem);
        return true;
    }

    public RussianGrammem get(GrammemCategory category) {
        return grammems.get(category);
    }

    public boolean contains(GrammemCategory category) {
        return grammems.containsKey(category);
    }

    public boolean contains(RussianGrammem grammem) {
        return grammems.containsValue(grammem);
    }

    public Collection<RussianGrammem> toCollection() {
        return grammems.values();
    }

    public void setDefaultsForNoun() {
        addIfNone(SINGULAR);
        addIfNone(NOMINATIVE);
    }

    public void setDefaultsForVerb() {
        addIfNone(SINGULAR);
        addIfNone(PRESENT);
        //Лицо и прошедшее время несовместимы. В прошедшем времени форма слова не зависит от лица.
        if (get(TENSE) == PAST) {
            grammems.remove(PERSON);
        } else {
            addIfNone(THIRDPERSON);
        }
    }
}
