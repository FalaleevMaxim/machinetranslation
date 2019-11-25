package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

public class Possessive extends AbstractTransform {
    public Possessive() {
        super(null, new NodeCriteria().withType("PRP$"));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        ir.setEngInfinitive(ir.getEngOriginal());
    }
}
