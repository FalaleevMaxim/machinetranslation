package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

public class DoNot extends AbstractTransform {
    public DoNot() {
        super(null, new NodeCriteria()
                .withType("VP")
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VB", "VBP", "VBD", "VBG", "VBN", "VBZ")
                                .withEngInfinitive("do")
                                .named("do"),
                        new NodeCriteria()
                                .withEngInfinitive("not")));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("do").doNotTranslate();
    }
}
