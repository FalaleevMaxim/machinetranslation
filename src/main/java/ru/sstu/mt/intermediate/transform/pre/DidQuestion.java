package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;

import java.util.Map;

public class DidQuestion extends AbstractTransform {
    public DidQuestion() {
        super(null, new NodeCriteria()
                .withType("SQ")
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VBD")
                                .withEngOriginal("did")
                                .named("did"),
                        new NodeCriteria()
                                .withType("VP")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("VB")
                                                .named("verb"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("did").doNotTranslate();
        queryResults.get("verb").addGrammems(RussianGrammem.PAST);
    }
}
