package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

public class MakeSureThat extends AbstractTransform {
    public MakeSureThat() {
        super(new NodeCriteria()
                .withType("VP")
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VB")
                                .withEngInfinitive("make")
                                .named("make")
                                .followedBy(new NodeCriteria()
                                        .withType("ADJP")
                                        .withInnerNodeCriterias(
                                                new NodeCriteria()
                                                        .withType("JJ")
                                                        .withEngInfinitive("sure")
                                                        .named("sure")
                                                        .followedBy(new NodeCriteria()
                                                                .withType("SBAR")
                                                                .withInnerNodeCriterias(
                                                                        new NodeCriteria()
                                                                                .withType("IN")
                                                                                .withEngInfinitive("that")
                                                                                .named("that")))))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("make").setRusInfinitive("убедиться");
        queryResults.get("sure").doNotTranslate();
        queryResults.get("that").setRusTransformed("что");
    }
}
