package ru.sstu.mt.intermediate.transform.pre.phrases;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;
import ru.sstu.mt.sklonyator.enums.RussianPos;

import java.util.Map;

public class DativeForNeed extends AbstractTransform {
    public DativeForNeed() {
        super("Дательный падеж для подлежащего если сказуемое need",
                new NodeCriteria()
                        .withType("S")
                        .withInnerNodeCriterias(
                                new NodeCriteria().withType("NP")
                                        .withInnerNodeCriterias(
                                                new NodeCriteria()
                                                        .withType("NN", "NNS", "NNP", "NNPS", "PRP")
                                                        .named("noun")),
                                new NodeCriteria()
                                        .withType("VP")
                                        .withInnerNodeCriterias(
                                                new NodeCriteria()
                                                        .withType("VBP")
                                                        .withEngInfinitive("need")
                                                        .named("need"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("noun").addGrammems(RussianGrammem.DATIVE);
        IRNode need = queryResults.get("need");
        need.setType("RB");
        need.setPos(RussianPos.SHORT_ADJ);
        need.addPosForDictionary(RussianPos.SHORT_ADJ);
    }
}
