package ru.sstu.mt.intermediate.transform.match;

import ru.sstu.mt.intermediate.model.GrammemsHolder;
import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractMatch;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.SklonyatorApi;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;
import ru.sstu.mt.util.GrammemsUtils;
import ru.sstu.mt.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.sstu.mt.sklonyator.enums.GrammemCategory.*;
import static ru.sstu.mt.sklonyator.enums.RussianGrammem.*;

public class MatchAdjectiveAfterNoun extends AbstractMatch {
    public MatchAdjectiveAfterNoun(SklonyatorApi sklonyator) {
        super(sklonyator, "Прилагательное после существительного",
                new NodeCriteria()
                        .withType("S")
                        .withInnerNodeCriterias(
                                new NodeCriteria()
                                        .withType("NP")
                                        .withInnerNodeCriterias(
                                                new NodeCriteria()
                                                        .withType("NN", "NNP", "NNS", "NNPS", "PRP")
                                                        .named("noun")),
                                new NodeCriteria()
                                        .withType("VP")
                                        .withInnerNodeCriterias(
                                                new NodeCriteria()
                                                        .withType("VB", "VBZ", "VBP")
                                                        .withEngInfinitive("be")
                                                        .named("be"),
                                                new NodeCriteria()
                                                        .withType("ADJP")
                                                        .withInnerNodeCriterias(
                                                                new NodeCriteria()
                                                                        .withType("JJ", "RB", "JJR", "RBR", "JJS", "RBS", "PRP$")
                                                                        .named("adjective")))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        IRNode noun = queryResults.get("noun");
        IRNode adjective = queryResults.get("adjective");
        if (StringUtils.isNullOrEmpty(noun.getRusInfinitive()) || StringUtils.isNullOrEmpty(adjective.getRusInfinitive())) {
            return;
        }
        try {
            if (adjective.getType().equals("JJR") || adjective.getType().equals("RBR")) {
                if (sklonyator.transform(adjective.getRusInfinitive(), Collections.singletonList(COMPARATIVE)).size() == 0) {
                    adjective.setType(adjective.getType().equals("JJR") ? "JJ" : "RB");
                    adjective.setRusTransformedPrefix("более");
                    adjective.getGrammems().removeGrammem(COMPARATIVE);
                    return;
                }
            } else if (adjective.getType().equals("JJS") || adjective.getType().equals("RBS")) {
                if (sklonyator.transform(adjective.getRusInfinitive(), Collections.singletonList(COMPARATIVE)).size() == 0) {
                    adjective.setRusTransformedPrefix("самый");
                    adjective.getGrammems().removeGrammem(SUPERLATIVE);
                }
            }

            GrammemsHolder holder = new GrammemsHolder();
            if (noun.getType().equals("PRP")) {
                GrammemsUtils.getGrammemsForPRP(noun.getRusInfinitive()).forEach(holder::add);
            } else {
                noun.getGrammemsCollection().forEach(holder::add);
            }

            List<RussianGrammem> grammems = sklonyator.getGrammems(noun.getRusInfinitive());
            grammems.stream()
                    .filter(grammem -> Arrays.asList(QUANTITY, GENDER).contains(grammem.category))
                    .forEach(holder::addIfNone);
            if (holder.get(QUANTITY) == PLURAL) {
                adjective.addGrammemsIfNone(holder.get(QUANTITY), holder.get(CASE));
            } else {
                adjective.addGrammemsIfNone(holder.get(QUANTITY), holder.get(CASE), holder.get(GENDER));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
