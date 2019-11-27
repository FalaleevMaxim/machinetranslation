package ru.sstu.mt.intermediate.transform.pre.phrases.verbs;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

public class LookAt extends AbstractTransform {
    public LookAt() {
        super(null, new NodeCriteria()
                .withType("VP")
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VB", "VBD", "VBZ")
                                .withEngInfinitive("look")
                                .named("look"),
                        new NodeCriteria()
                                .withType("PP")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("IN")
                                                .withEngInfinitive("at")
                                                .named("at"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("at").setRusInfinitive("на");
        queryResults.get("look").setRusInfinitive("смотреть");
    }
}
