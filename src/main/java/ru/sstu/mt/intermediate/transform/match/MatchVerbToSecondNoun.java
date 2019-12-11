package ru.sstu.mt.intermediate.transform.match;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractMatch;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.SklonyatorApi;
import ru.sstu.mt.util.GrammemsUtils;

import java.util.Map;

/**
 * Некоторые вводные слова считаются существительными. И, поскольку они первые в предложении, ошибочно считаются подлежащим.
 * В таких случаях это правило согласует глагол со вторым существительным перед ним.
 * Пример:
 * Yesterday I went to the concert
 */
public class MatchVerbToSecondNoun extends AbstractMatch {
    public MatchVerbToSecondNoun(SklonyatorApi sklonyator) {
        super(sklonyator, "Согласование формы грагола с подлежащим после вводного слова",
                new NodeCriteria()
                        .withType("S")
                        .withInnerNodeCriterias(
                                new NodeCriteria()
                                        .withType("NP")
                                        .withInnerNodeCriterias(
                                                new NodeCriteria()
                                                        .withType("NN", "NNS", "NNP", "NNPS", "PRP")
                                                        .named("noun")),
                                new NodeCriteria()
                                        .withType("NP")
                                        .withInnerNodeCriterias(
                                                new NodeCriteria()
                                                        .withType("NN", "NNS", "NNP", "NNPS", "PRP")
                                                        .named("noun2")),
                                new NodeCriteria()
                                        .withType("VP")
                                        .withInnerNodeCriterias(
                                                new NodeCriteria()
                                                        .withType("VB", "VBP", "VBD", "VBZ")
                                                        .named("verb"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        IRNode noun = queryResults.get("noun2");
        IRNode verb = queryResults.get("verb");
        GrammemsUtils.matchVerbToNoun(verb, noun, sklonyator);
    }
}
