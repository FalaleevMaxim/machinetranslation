package ru.sstu.mt.intermediate.transform.post;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.enums.RussianGrammems;

import java.util.Map;

public class Comparative extends AbstractTransform {
    public Comparative() {
        super(null, new NodeCriteria().withType("JJR", "RBR"));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        ir.addGrammems(RussianGrammems.COMPARATIVE);
    }
}
