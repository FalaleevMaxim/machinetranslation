package ru.sstu.mt.intermediate.transform.pre.phrases.verbs;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

public class ComeIn extends AbstractTransform {
    public ComeIn() {
        super(null, new NodeCriteria()
                .withType("VP")
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VBP", "VB", "VBZ")
                                .withEngInfinitive("come")
                                .named("come"),
                        new NodeCriteria()
                                .withType("ADVP")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("IN")
                                                .withEngInfinitive("in")
                                                .named("in"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("come").setRusInfinitive("входить");
        queryResults.get("in").doNotTranslate();
    }
}
