package ru.sstu.mt.intermediate.transform.post;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;
import ru.sstu.mt.sklonyator.enums.RussianPos;

import java.util.Map;

/**
 * Пример
 * I want to be a programmer
 */
public class ToBeNoun extends AbstractTransform {
    public ToBeNoun() {
        super("To be + существительное в творительном падеже", new NodeCriteria()
                .withType("VP")
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("TO")
                                .withEngInfinitive("to")
                                .named("to"),
                        new NodeCriteria()
                                .withType("VP")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("VB")
                                                .withEngOriginal("be")
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
        IRNode to = queryResults.get("to");
        IRNode be = queryResults.get("be");
        IRNode noun = queryResults.get("noun");
        to.doNotTranslate();
        be.setPos(RussianPos.VERB_INF);
        be.getGrammems().clear();
        noun.addGrammems(RussianGrammem.INSTRUMENTAL);
    }
}
