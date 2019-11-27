package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;

import java.util.Map;

public class ImperativeS extends AbstractTransform {
    public ImperativeS() {
        super(null, new NodeCriteria()
                .withType("S")
                .withOtherConditions((node, vars) -> node.getChildren().stream().noneMatch(ch -> ch.getType().equals("NP")))
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VP")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("VB")
                                                .named("verb"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("verb").addGrammems(RussianGrammem.IMPERATIVE);
        queryResults.get("verb").addGrammems(RussianGrammem.PLURAL);
    }
}
