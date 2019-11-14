package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

public class EmptyDT extends AbstractTransform {
    public EmptyDT() {
        super(new NodeCriteria().withType("DT"));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        ir.doNotTranslate();
    }
}
