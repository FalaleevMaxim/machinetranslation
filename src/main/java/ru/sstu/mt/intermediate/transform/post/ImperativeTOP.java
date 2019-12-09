package ru.sstu.mt.intermediate.transform.post;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

import static ru.sstu.mt.sklonyator.enums.RussianGrammem.*;

public class ImperativeTOP extends AbstractTransform {
    public ImperativeTOP() {
        super(null, new NodeCriteria()
                .withType("TOP")
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
        queryResults.get("verb").addGrammems(IMPERATIVE, PLURAL, SECOND_PERSON);
    }
}
