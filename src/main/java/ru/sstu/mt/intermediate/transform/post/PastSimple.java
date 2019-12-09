package ru.sstu.mt.intermediate.transform.post;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

import static ru.sstu.mt.sklonyator.enums.RussianGrammem.PAST;

public class PastSimple extends AbstractTransform {
    public PastSimple() {
        super(null, new NodeCriteria().withType("VBD"));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        ir.addGrammemsIfNone(PAST);
    }
}
