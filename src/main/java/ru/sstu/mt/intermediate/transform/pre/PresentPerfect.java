package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;

import java.util.Map;

public class PresentPerfect extends AbstractTransform {
    public PresentPerfect() {
        super(null, new NodeCriteria()
                .named("root")
                .withOtherConditions((node, vars) -> node.getType().equals("VP") || node.getType().equals("SQ"))
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VBP")
                                .withEngInfinitive("have")
                                .named("have"),
                        new NodeCriteria()
                                .withType("VP")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("VBN")
                                                .named("verb"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("have").doNotTranslate();
        queryResults.get("verb").addGrammems(RussianGrammem.PAST);
    }
}
