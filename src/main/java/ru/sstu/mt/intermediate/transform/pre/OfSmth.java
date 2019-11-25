package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.enums.RussianGrammems;

import java.util.Map;

public class OfSmth extends AbstractTransform {
    public OfSmth() {
        super(null, new NodeCriteria()
                .withType("NP")
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("NP")
                                .followedBy(
                                        new NodeCriteria()
                                                .withType("PP")
                                                .withInnerNodeCriterias(
                                                        new NodeCriteria()
                                                                .withType("IN")
                                                                .withEngInfinitive("of")
                                                                .named("of")
                                                                .followedBy(
                                                                        new NodeCriteria()
                                                                                .withType("NP")
                                                                                .withInnerNodeCriterias(
                                                                                        new NodeCriteria()
                                                                                                .withType("NN", "NNS", "NNP", "NNPS")
                                                                                                .named("smth")))))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("of").doNotTranslate();
        queryResults.get("smth").addGrammems(RussianGrammems.GENITIVE);

    }
}
