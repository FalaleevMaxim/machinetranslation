package ru.sstu.mt.util.enums;

import java.util.Arrays;

public enum RussianPoS {
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
    PART("ЧАСТ", "частица", "");

    public final String systemName;
    public final String description;
    public final String example;

    RussianPoS(String systemName, String description, String example) {
        this.systemName = systemName;
        this.description = description;
        this.example = example;
    }

    public static RussianPoS getByDescription(String description) {
        return Arrays.stream(values())
                .filter(pos -> pos.description.equalsIgnoreCase(description))
                .findFirst().orElse(null);
    }

    public static RussianPoS getBySystemName(String systemName) {
        return Arrays.stream(values())
                .filter(pos -> pos.systemName.equalsIgnoreCase(systemName))
                .findFirst().orElse(null);
    }
}
