package ru.sstu.mt.intermediate.transform.pre.phrases;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

public class MakeSureTo extends AbstractTransform {
    public MakeSureTo() {
        super(null, new NodeCriteria()
                .withType("VP")
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VB")
                                .withEngInfinitive("make")
                                .named("make")
                                .followedBy(new NodeCriteria()
                                        .withType("ADJP")
                                        .withInnerNodeCriterias(
                                                new NodeCriteria()
                                                        .withType("JJ")
                                                        .withEngInfinitive("sure")
                                                        .named("sure")
                                                        .followedBy(new NodeCriteria()
                                                                .withType("S")
                                                                .withInnerNodeCriterias(
                                                                        new NodeCriteria()
                                                                                .withType("VP")
                                                                                .withInnerNodeCriterias(
                                                                                        new NodeCriteria()
                                                                                                .withType("TO")
                                                                                                .withEngInfinitive("to")
                                                                                                .named("to")
                                                                                                .followedBy(new NodeCriteria()
                                                                                                        .withType("VP")
                                                                                                        .withInnerNodeCriterias(
                                                                                                                new NodeCriteria()
                                                                                                                        .withType("VB")
                                                                                                                        .named("vb"))))))))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("make").setRusInfinitive("убедиться");
        queryResults.get("sure").doNotTranslate();
        queryResults.get("to").setRusInfinitive("что");
        //ToDo перевести vb в прошедшее время, совершенный вид (?)
    }
}
