package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

public class PredicateToBe extends AbstractTransform {
    public PredicateToBe() {
        super("Не переводить be в качестве сказуемого",
                new NodeCriteria()
                        .withType("S")
                        .withInnerNodeCriterias(
                                new NodeCriteria()
                                        .withType("NP"),
                                new NodeCriteria()
                                        .withType("VP")
                                        .withInnerNodeCriterias(
                                                new NodeCriteria()
                                                        .withType("VB", "VBP", "VBZ")
                                                        .withEngInfinitive("be")
                                                        .named("be"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("be").doNotTranslate();
    }
}
