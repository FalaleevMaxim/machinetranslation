package ru.sstu.mt.intermediate.transform.pre.phrases;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

public class StandUp extends AbstractTransform {
    public StandUp() {
        super(null, new NodeCriteria()
                .withType("VP")
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VB", "VBD", "VBZ")
                                .withEngInfinitive("stand")
                                .named("stand"),
                        new NodeCriteria()
                                .withType("PRT")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("RP")
                                                .withEngInfinitive("up")
                                                .named("up"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("stand").setRusInfinitive("встать");
        queryResults.get("up").doNotTranslate();
    }
}
