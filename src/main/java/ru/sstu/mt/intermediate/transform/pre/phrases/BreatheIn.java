package ru.sstu.mt.intermediate.transform.pre.phrases;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

public class BreatheIn extends AbstractTransform {
    public BreatheIn() {
        super(null, new NodeCriteria()
                .withType("VP")
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VBP", "VB", "VBZ", "VBG")
                                .withEngInfinitive("breathe", "breathing")
                                .named("breathe"),
                        new NodeCriteria()
                                .withType("PP")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("IN")
                                                .withEngInfinitive("in")
                                                .named("in"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("breathe").setRusInfinitive("вдыхать");
        queryResults.get("in").doNotTranslate();
    }
}
