package ru.sstu.mt.intermediate.transform.post;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;

import java.util.Map;

/**
 * Пример
 * I am a student
 */
public class ToBeFormAndNoun extends AbstractTransform {
    public ToBeFormAndNoun() {
        super("To be + существительное в именительном падеже", new NodeCriteria()
                .withType("S")
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("NP"),
                        new NodeCriteria()
                                .withType("VP")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("VB", "VBP", "VBD", "VBZ")
                                                .withEngInfinitive("be")
                                                .named("be"),
                                        new NodeCriteria()
                                                .withType("NP")
                                                .withInnerNodeCriterias(
                                                        new NodeCriteria()
                                                                .withType("NN", "NNS", "NNP", "NNPS", "PRP")
                                                                .named("noun")))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        queryResults.get("noun").addGrammems(RussianGrammem.NOMINATIVE);
        queryResults.get("be").doNotTranslate();
    }
}
