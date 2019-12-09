package ru.sstu.mt.intermediate.transform.post;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;

import java.util.Map;

public class PluralNNS extends AbstractTransform {
    public PluralNNS() {
        super(null,
                new NodeCriteria()
                        .withType("NNS", "NNPS")
                        .withOtherConditions(
                                // Слово dacha неверно определяется как NNS
                                (node, vars) -> !node.getEngOriginal().equals("dacha")));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        //
        if (ir.getEngOriginal().equals("dacha")) {
            return;
        }
        ir.addGrammems(RussianGrammem.PLURAL);
    }
}
