package ru.sstu.mt.intermediate.transform.pre.phrases.verbs;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.enums.RussianPos;

import java.util.Map;

public class HaveTo extends AbstractTransform {
    public HaveTo() {
        super(null, new NodeCriteria()
                .withType("VP")
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VBP", "VB", "VBZ")
                                .withEngInfinitive("have")
                                .named("have"),
                        new NodeCriteria()
                                .withType("S")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("VP")
                                                .withInnerNodeCriterias(
                                                        new NodeCriteria()
                                                                .withType("TO")
                                                                .withEngInfinitive("to")
                                                                .named("to"),
                                                        new NodeCriteria()
                                                                .withType("VP")))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        IRNode have = queryResults.get("have");
        IRNode to = queryResults.get("to");
        have.setRusInfinitive("должен");
        have.setType("RB");
        have.setPos(RussianPos.SHORT_ADJ);
        have.addPosForDictionary(RussianPos.SHORT_ADJ);
        to.doNotTranslate();
    }
}
