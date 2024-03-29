package ru.sstu.mt.intermediate.transform.post;


import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;

import java.util.Map;

/*
Примеры:
It seems cool
Be happy
 */
public class SeemAdjectiveInstrumental extends AbstractTransform {
    public SeemAdjectiveInstrumental() {
        super("Творительный падеж для прилагательного после seem или look",
                new NodeCriteria()
                        .withType("S")
                        .withInnerNodeCriterias(
                                new NodeCriteria()
                                        .withType("NP")
                                        .withInnerNodeCriterias(
                                                new NodeCriteria()
                                                        .withType("NN", "NNS", "NNP", "NNPS", "PRP")
                                                        .named("noun")),
                                new NodeCriteria()
                                        .withType("VP")
                                        .withInnerNodeCriterias(
                                                new NodeCriteria()
                                                        .withType("VB", "VBP", "VBZ")
                                                        .withEngInfinitive("look", "seem")
                                                        .named("be"),
                                                new NodeCriteria()
                                                        .withType("ADJP")
                                                        .withInnerNodeCriterias(
                                                                new NodeCriteria()
                                                                        .withType("JJ", "RB")
                                                                        .named("adjective")))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        IRNode adjective = queryResults.get("adjective");
        adjective.addGrammems(RussianGrammem.INSTRUMENTAL);
    }
}
