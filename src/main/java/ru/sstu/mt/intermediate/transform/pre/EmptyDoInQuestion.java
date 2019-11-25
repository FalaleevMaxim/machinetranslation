package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

public class EmptyDoInQuestion extends AbstractTransform {
    public EmptyDoInQuestion() {
        super(null, new NodeCriteria().withType("SQ").withInnerNodeCriterias(
                new NodeCriteria().withType("VBP").withEngInfinitive("do").named("do")
        ));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("do").doNotTranslate();
    }
}
