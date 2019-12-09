package ru.sstu.mt.util;

import ru.sstu.mt.intermediate.model.GrammemsHolder;
import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.sklonyator.SklonyatorApi;
import ru.sstu.mt.sklonyator.WordFormInfo;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;
import ru.sstu.mt.sklonyator.enums.RussianPos;

import java.io.IOException;
import java.util.*;

import static ru.sstu.mt.sklonyator.enums.GrammemCategory.*;
import static ru.sstu.mt.sklonyator.enums.RussianGrammem.*;
import static ru.sstu.mt.sklonyator.enums.RussianPos.PRONOUN_N;

public class GrammemsUtils {
    private static final Map<String, List<WordFormInfo>> possessive = new HashMap<>();

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
        possessive.put("their", Collections.singletonList(
                new WordFormInfo("их", PRONOUN_N, Collections.emptyList())));
        possessive.put("his", Collections.singletonList(
                new WordFormInfo("его", PRONOUN_N, Collections.emptyList())));
        possessive.put("her", Collections.singletonList(
                new WordFormInfo("её", PRONOUN_N, Collections.emptyList())));
        possessive.put("its", Collections.singletonList(
                new WordFormInfo("его", PRONOUN_N, Collections.emptyList())));
    }

    public static String formPossessivePRP(String engPossessivePRP, Collection<RussianGrammem> grammems) {
        engPossessivePRP = engPossessivePRP.toLowerCase();
        if (!possessive.containsKey(engPossessivePRP)) return engPossessivePRP;
        WordFormInfo formInfo = possessive.get(engPossessivePRP).stream().filter(form -> grammems.containsAll(form.getGrammems())).findFirst().orElse(null);
        if (formInfo != null) return formInfo.getWord();
        else return possessive.get(engPossessivePRP).get(0).getWord();
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
                return Arrays.asList(SECOND_PERSON, SINGULAR);
            case "we":
                return Arrays.asList(FIRST_PERSON, PLURAL);
            case "they":
                return Arrays.asList(THIRD_PERSON, PLURAL);
            default:
                return Collections.emptyList();
        }
    }

    public static void setDefaults(IRNode node) {
        GrammemsHolder grammems = node.getGrammems();
        switch (node.getType()) {
            case "JJ":
            case "RB":
            case "JJS":
            case "RBS":
            case "PRP$":
                node.addGrammemsIfNone(NOMINATIVE);
                if (grammems.get(QUANTITY) != RussianGrammem.PLURAL) {
                    node.addGrammemsIfNone(SINGULAR, MASCULINE);
                }
                break;
            case "CD":

                break;
            case "MD":

                break;
            case "NN":
            case "NNS":
            case "NNP":
            case "NNPS":
            case "PRP":
                node.addGrammemsIfNone(NOMINATIVE, SINGULAR);
                break;
            case "VB":
            case "VBD":
            case "VBG":
            case "VBN":
            case "VBP":
            case "VBZ":
                if (!grammems.contains(IMPERATIVE)) {
                    grammems.addIfNone(PRESENT);
                }
                /*node.addGrammemsIfNone(SINGULAR, THIRD_PERSON);
                if (grammems.get(QUANTITY) == SINGULAR && grammems.get(PERSON) == THIRD_PERSON) {
                    node.addGrammems(MASCULINE);
                }
                break;*/
            case "WP":
            case "WDT":
            case "WRB":
            case "WP$":

                break;
        }
    }

    public static void matchVerbToNoun(IRNode verb, IRNode noun, SklonyatorApi sklonyator) {
        if (StringUtils.isNullOrEmpty(noun.getRusInfinitive()) || StringUtils.isNullOrEmpty(verb.getRusInfinitive())) {
            return;
        }
        GrammemsHolder holder = new GrammemsHolder();
        noun.getGrammemsCollection().forEach(holder::add);
        verb.getGrammemsCollection().forEach(holder::add);
        if (!noun.getType().equals("PRP")) {
            try {
                List<RussianGrammem> grammems = sklonyator.getGrammems(noun.getRusInfinitive());
                grammems.forEach(holder::addIfNone);
                holder.addIfNone(THIRD_PERSON);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            GrammemsUtils.getGrammemsForPRP(noun.getEngInfinitive()).forEach(holder::addIfNone);
        }

        verb.addGrammems(holder.get(QUANTITY));
        verb.addGrammemsIfNone(SINGULAR);
        if (holder.get(TENSE) == PAST) {
            // В прошедшем времени и единственном числе форма глагола не зависит от лица, зависит только от рода.
            if (holder.get(QUANTITY) == SINGULAR) {
                verb.getGrammems().removeGrammem(PERSON);
                verb.addGrammems(holder.get(GENDER));
            }

            // В прошедшем времени и множественном числе форма глагола не зависит от лица и рода
            if (holder.get(QUANTITY) == PLURAL) {
                verb.getGrammems().removeGrammem(PERSON);
                verb.getGrammems().removeGrammem(GENDER);
            }
        } else {
            // В настоящем и будущем времени форма определяется числом и лицом, родов нет.
            verb.getGrammems().removeGrammem(GENDER);
            verb.addGrammems(holder.get(PERSON));
        }
    }
}
