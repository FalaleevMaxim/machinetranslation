package ru.sstu.mt.util;

import ru.sstu.mt.sklonyator.WordFormInfo;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;
import ru.sstu.mt.sklonyator.enums.RussianPos;

import java.util.*;

import static ru.sstu.mt.sklonyator.enums.RussianGrammem.*;

public class GrammemsUtils {
    private static Map<String, List<WordFormInfo>> possessive = new HashMap<>();

    static {
        possessive.put("my", Arrays.asList(
                new WordFormInfo("мой", RussianPos.PRONOUN_N, Arrays.asList(NOMINATIVE, SINGULAR, MASCULINE)),
                new WordFormInfo("моя", RussianPos.PRONOUN_N, Arrays.asList(NOMINATIVE, SINGULAR, FEMININE)),
                new WordFormInfo("моё", RussianPos.PRONOUN_N, Arrays.asList(NOMINATIVE, SINGULAR, MID)),
                new WordFormInfo("мои", RussianPos.PRONOUN_N, Arrays.asList(NOMINATIVE, PLURAL)),

                new WordFormInfo("моего", RussianPos.PRONOUN_N, Arrays.asList(GENITIVE, SINGULAR, MASCULINE)),
                new WordFormInfo("моей", RussianPos.PRONOUN_N, Arrays.asList(GENITIVE, SINGULAR, FEMININE)),
                new WordFormInfo("моего", RussianPos.PRONOUN_N, Arrays.asList(GENITIVE, SINGULAR, MID)),
                new WordFormInfo("моих", RussianPos.PRONOUN_N, Arrays.asList(GENITIVE, PLURAL)),

                new WordFormInfo("мой", RussianPos.PRONOUN_N, Arrays.asList(ACCUSATIVE, SINGULAR, MASCULINE)),
                new WordFormInfo("мою", RussianPos.PRONOUN_N, Arrays.asList(ACCUSATIVE, SINGULAR, FEMININE)),
                new WordFormInfo("моё", RussianPos.PRONOUN_N, Arrays.asList(ACCUSATIVE, SINGULAR, MID)),
                new WordFormInfo("мои", RussianPos.PRONOUN_N, Arrays.asList(ACCUSATIVE, PLURAL)),

                new WordFormInfo("моему", RussianPos.PRONOUN_N, Arrays.asList(DATIVE, SINGULAR, MASCULINE)),
                new WordFormInfo("моей", RussianPos.PRONOUN_N, Arrays.asList(DATIVE, SINGULAR, FEMININE)),
                new WordFormInfo("моему", RussianPos.PRONOUN_N, Arrays.asList(DATIVE, SINGULAR, MID)),
                new WordFormInfo("моим", RussianPos.PRONOUN_N, Arrays.asList(DATIVE, PLURAL)),

                new WordFormInfo("моим", RussianPos.PRONOUN_N, Arrays.asList(INSTRUMENTAL, SINGULAR, MASCULINE)),
                new WordFormInfo("моей", RussianPos.PRONOUN_N, Arrays.asList(INSTRUMENTAL, SINGULAR, FEMININE)),
                new WordFormInfo("моим", RussianPos.PRONOUN_N, Arrays.asList(INSTRUMENTAL, SINGULAR, MID)),
                new WordFormInfo("моими", RussianPos.PRONOUN_N, Arrays.asList(INSTRUMENTAL, PLURAL)),

                new WordFormInfo("моём", RussianPos.PRONOUN_N, Arrays.asList(PREPOSITIONAL, SINGULAR, MASCULINE)),
                new WordFormInfo("моей", RussianPos.PRONOUN_N, Arrays.asList(PREPOSITIONAL, SINGULAR, FEMININE)),
                new WordFormInfo("моём", RussianPos.PRONOUN_N, Arrays.asList(PREPOSITIONAL, SINGULAR, MID)),
                new WordFormInfo("моих", RussianPos.PRONOUN_N, Arrays.asList(PREPOSITIONAL, PLURAL))
        ));
        possessive.put("their", Arrays.asList(
                new WordFormInfo("их", RussianPos.PRONOUN_N, Arrays.asList(NOMINATIVE, SINGULAR, MASCULINE))));
        //ToDo Дозаполнить для остальных: his, her, its, their
    }

    public static List<RussianGrammem> getGrammemsForPRP(String prp) {
        prp = prp.toLowerCase();
        switch (prp) {
            case "i":
                return Arrays.asList(FIRST_PERSON, SINGULAR);
            case "he":
                return Arrays.asList(THIRD_PERSON, MASCULINE, SINGULAR);
            case "she":
                return Arrays.asList(THIRD_PERSON, FEMININE, SINGULAR);
            case "it":
                return Arrays.asList(THIRD_PERSON, MID, SINGULAR);
            case "you":
                return Arrays.asList(SECOND_PERSON, PLURAL);
            case "we":
                return Arrays.asList(FIRST_PERSON, PLURAL);
            case "they":
                return Arrays.asList(THIRD_PERSON, PLURAL);
            default:
                return Collections.emptyList();
        }
    }
}
