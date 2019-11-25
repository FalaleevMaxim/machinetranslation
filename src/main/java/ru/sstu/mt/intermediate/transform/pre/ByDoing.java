package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.enums.RussianPos;

import java.util.Map;

public class ByDoing extends AbstractTransform {
    public ByDoing() {
        super("By + глагол с окончанием ing - by не переводится, глагол переводится в деепричастие",
                new NodeCriteria()
                        .withType("PP")
                        .withInnerNodeCriterias(
                                new NodeCriteria()
                                        .withType("IN")
                                        .withEngOriginal("by")
                                        .named("by")
                                        .followedBy(
                                                new NodeCriteria()
                                                        .withType("S")
                                                        .withInnerNodeCriterias(
                                                                new NodeCriteria()
                                                                        .withType("VP")
                                                                        .withInnerNodeCriterias(
                                                                                new NodeCriteria()
                                                                                        .withType("VBG").named("ing"))))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("by").doNotTranslate();
        queryResults.get("ing").setPos(RussianPos.DEE_PARTICIPLE);
    }
}
