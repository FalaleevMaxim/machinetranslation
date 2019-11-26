package ru.sstu.mt.intermediate.transform.post;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;

import java.util.Map;

public class AccusativeForDependentNouns extends AbstractTransform {
    public AccusativeForDependentNouns() {
        super("Зависимым от глаголов существительным проставлять по умолчанию винительный падеж",
                new NodeCriteria().withType("VP").withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VB", "VBP", "VBD", "VBG", "VBN", "VBZ")
                                .named("verb"),
                        new NodeCriteria()
                                .withType("NN", "NNS", "PRP")
                                .named("noun")
                ));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("noun").addGrammemsIfNone(RussianGrammem.ACCUSATIVE);
    }
}
