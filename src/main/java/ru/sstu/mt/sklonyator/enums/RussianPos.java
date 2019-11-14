package ru.sstu.mt.sklonyator.enums;

import java.util.Arrays;

public enum RussianPos {
    NOUN("С", "существительное", ""),
    ADJ("П", "прилагательное", ""),
    SHORT_ADJ("КР_ПРИЛ", "краткое прилагательное", ""),
    VERB_INF("ИНФИНИТИВ", "инфинитив", "идти"),
    VERB_PERS("Г", "глагол в личной форме", "идет"),
    PARTICIPLE("ПРИЧАСТИЕ", "причастие", "идущий"),
    DEE_PARTICIPLE("ДЕЕПРИЧАСТИЕ", "деепричастие", "идя"),
    NUM("ЧИСЛ", "числительное (количественное)", "восемь"),
    ORD("ЧИСЛ-П", "порядковое числительное", "восьмой"),
    PRONOUN_N("МС", "местоимение-существительное", "он"),
    PRONOUN_PR("МС-ПРЕДК", "местоимение-предикатив", "нечего"),
    PRONOUN_ADJ("МС-П", "местоименное прилагательное", "всякий"),
    ADVERB("Н", "наречие", ""),
    PREDICATE("ПРЕДК", "предикатив", "интересно"),
    PREP("ПРЕДЛ", "предлог", ""),
    CONJ("СОЮЗ", "союз", "и"),
    INTERJ("МЕЖД", "междометие", ""),
    PART("ЧАСТ", "частица", ""),
    PHRASE("ФРАЗ", "фраза", "");

    public final String systemName;
    public final String description;
    public final String example;

    RussianPos(String systemName, String description, String example) {
        this.systemName = systemName;
        this.description = description;
        this.example = example;
    }

    public static RussianPos getByDescription(String description) {
        return Arrays.stream(values())
                .filter(pos -> pos.description.equalsIgnoreCase(description))
                .findFirst().orElse(null);
    }

    public static RussianPos getBySystemName(String systemName) {
        return Arrays.stream(values())
                .filter(pos -> pos.systemName.equalsIgnoreCase(systemName.toUpperCase()))
                .findFirst().orElse(null);
    }
}
