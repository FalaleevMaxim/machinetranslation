package ru.sstu.mt.intermediate.transform.match;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractMatch;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.SklonyatorApi;
import ru.sstu.mt.util.GrammemsUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static ru.sstu.mt.sklonyator.enums.RussianGrammem.FUTURE;
import static ru.sstu.mt.sklonyator.enums.RussianPos.VERB_INF;

public class WillFuture extends AbstractMatch {
    public WillFuture(SklonyatorApi sklonyator) {
        super(sklonyator, "Will укзывает на будущее время",
                new NodeCriteria().withType("S").withInnerNodeCriterias(
                        new NodeCriteria().withType("NP")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("NN", "NNS", "NNP", "NNPS", "PRP")
                                                .named("noun")),
                        new NodeCriteria().withType("VP")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("MD")
                                                .withEngOriginal("will")
                                                .named("will"),
                                        new NodeCriteria()
                                                .withType("VP")
                                                .withInnerNodeCriterias(
                                                        new NodeCriteria()
                                                                .withType("VB")
                                                                .named("verb")))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        IRNode noun = queryResults.get("noun");
        IRNode verb = queryResults.get("verb");
        IRNode will = queryResults.get("will");

        try {
            if (sklonyator.transform(verb.getRusInfinitive(), Collections.singletonList(FUTURE)).isEmpty()) {
                verb.setPos(VERB_INF);
                verb.getGrammems().clear();
                will.addGrammems(FUTURE);
                GrammemsUtils.matchVerbToNoun(will, noun, sklonyator);
            } else {
                will.doNotTranslate();
                verb.addGrammems(FUTURE);
                GrammemsUtils.matchVerbToNoun(verb, noun, sklonyator);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
