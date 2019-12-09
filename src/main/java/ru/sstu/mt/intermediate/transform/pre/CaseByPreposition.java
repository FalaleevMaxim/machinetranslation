package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ru.sstu.mt.sklonyator.enums.RussianGrammem.*;

public class CaseByPreposition extends AbstractTransform {

    public static final List<String> WEEKDAYS = Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday");
    public static final List<String> PLACES = Arrays.asList("school", "university", "cafe", "shop", "building", "house", "room", "restaurant", "hospital", "clinik", "zoo", "cinema", "basement", "toilet", "city", "village", "hotel", "police", "park");

    public CaseByPreposition() {
        super("Определение падежа по предлогу",
                new NodeCriteria()
                        .withType("PP")
                        .withInnerNodeCriterias(
                                new NodeCriteria()
                                        .withType("IN", "TO")
                                        .named("prep"),
                                new NodeCriteria()
                                        .withType("NP")
                                        .withInnerNodeCriterias(
                                                new NodeCriteria()
                                                        .withType("NN", "NNS", "NNP", "NNPS", "PRP")
                                                        .named("noun"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        IRNode prep = queryResults.get("prep");
        IRNode noun = queryResults.get("noun");

        switch (prep.getEngInfinitive()) {
            case "on":
                if (WEEKDAYS.contains(noun.getEngInfinitive().toLowerCase())) {
                    prep.setRusInfinitive("в");
                    prep.setRusTransformed("в");
                    noun.addGrammemsIfNone(ACCUSATIVE);
                } else {
                    prep.setRusInfinitive("на");
                    prep.setRusTransformed("на");
                    noun.addGrammemsIfNone(PREPOSITIONAL);
                }
                break;
            case "to":
                if (PLACES.contains(noun.getEngInfinitive())) {
                    noun.addGrammemsIfNone(ACCUSATIVE);
                    prep.setRusInfinitive("в");
                    prep.setRusTransformed("в");
                } else if (Arrays.asList("roof", "dacha", "storage").contains(noun.getEngInfinitive())) {
                    prep.setRusInfinitive("на");
                    prep.setRusTransformed("на");
                    noun.addGrammemsIfNone(ACCUSATIVE);
                } else {
                    prep.setRusInfinitive("к");
                    prep.setRusTransformed("к");
                    noun.addGrammemsIfNone(DATIVE);
                }
                break;
            case "at":
                noun.addGrammemsIfNone(INSTRUMENTAL);
                break;
            case "in":
                noun.addGrammemsIfNone(PREPOSITIONAL);
                break;
            case "with":
                noun.addGrammemsIfNone(INSTRUMENTAL);
                break;
            case "about":
                noun.addGrammemsIfNone(PREPOSITIONAL);
                break;
            default:
                noun.addGrammemsIfNone(ACCUSATIVE);
        }
    }
}
