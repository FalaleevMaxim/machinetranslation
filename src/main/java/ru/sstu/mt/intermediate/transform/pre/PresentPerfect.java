package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

public class PresentPerfect extends AbstractTransform {
    public PresentPerfect() {
        super(new NodeCriteria()
                .named("root")
                .withOtherConditions((node, vars) -> node.getType().equals("VP") || node.getType().equals("SQ"))
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VBP")
                                .withEngInfinitive("have")
                                .named("have"),
                        new NodeCriteria()
                                .withType("VP")
                                .named("vp")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("VBN")
                                                .named("verb"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("have").doNotTranslate();
        //ToDo перевод VP или VBN в прошедшее время
        //ToDo перевод VBN в совершенный вид (?)
    }
}
