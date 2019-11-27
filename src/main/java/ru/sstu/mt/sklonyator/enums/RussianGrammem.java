package ru.sstu.mt.sklonyator.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.sstu.mt.sklonyator.enums.GrammemCategory.*;

public enum RussianGrammem {
    MASCULINE(GENDER, "мр", "мужской род"),
    FEMININE(GENDER, "жр", "женский род"),
    MID(GENDER, "ср", "средний род"),
    COMMON(GENDER, "мр-жр", "общий род (сирота, пьяница)"),
    SINGULAR(QUANTITY, "ед", "единственное число"),
    PLURAL(QUANTITY, "мн", "множественное число"),
    NOMINATIVE(CASE, "им", "именительный"),
    GENITIVE(CASE, "рд", "родительный"),
    DATIVE(CASE, "дт", "дательный"),
    ACCUSATIVE(CASE, "вн", "винительный"),
    INSTRUMENTAL(CASE, "тв", "творительный"),
    PREPOSITIONAL(CASE, "пр", "предложный"),
    FIRST_PERSON(PERSON, "1л", "первое лицо"),
    SECOND_PERSON(PERSON, "2л", "второе лицо"),
    THIRD_PERSON(PERSON, "3л", "третье лицо"),
    PAST(TENSE, "прш", "прошедшее время"),
    PRESENT(TENSE, "нст", "настоящее время"),
    FUTURE(TENSE, "буд", "будущее время"),
    PERFECT(TYPE, "св", "совершенный вид"),
    IMPERFECTIVE(TYPE, "нс", "несовершенный вид"),
    IMPERATIVE(DEGREE, "пвл", "повелительное наклонение"),
    SUPERLATIVE(DEGREE, "прев", "превосходная степень"),
    COMPARATIVE(DEGREE, "сравн", "сравнительная  степень"),
    QUALITY(DEGREE, "кач", "качественное прилагательное");


    public final GrammemCategory category;
    public final String systemName;
    public final String description;


    RussianGrammem(GrammemCategory category, String systemName, String description) {
        this.category = category;
        this.systemName = systemName;
        this.description = description;
    }

    public GrammemCategory getCategory() {
        return category;
    }

    public String getSystemName() {
        return systemName;
    }

    public String getDescription() {
        return description;
    }

    public static RussianGrammem getBySystemName(String systemName) {
        return Arrays.stream(values())
                .filter(grammem -> grammem.systemName.equals(systemName.toLowerCase()))
                .findFirst().orElse(null);
    }

    public static RussianGrammem getByDescription(String description) {
        return Arrays.stream(values())
                .filter(grammem -> grammem.description.equals(description.toLowerCase()))
                .findFirst().orElse(null);
    }

    public static List<RussianGrammem> getOfCategory(GrammemCategory category) {
        return Arrays.stream(values())
                .filter(grammem -> grammem.category.equals(category))
                .collect(Collectors.toList());
    }
}
