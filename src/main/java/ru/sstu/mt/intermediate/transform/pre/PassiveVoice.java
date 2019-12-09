package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

import static ru.sstu.mt.sklonyator.enums.RussianGrammem.*;

public class PassiveVoice extends AbstractTransform {
    public PassiveVoice() {
        super(null, new NodeCriteria()
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
                                                .withType("VBP", "VB", "VBD")
                                                .withEngInfinitive("be")
                                                .named("be"),
                                        new NodeCriteria()
                                                .withType("VP")
                                                .withInnerNodeCriterias(
                                                        new NodeCriteria()
                                                                .withType("VBN")
                                                                .named("verb")
                                                ))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        IRNode noun = queryResults.get("noun");
        IRNode be = queryResults.get("be");
        IRNode verb = queryResults.get("verb");
        if (verb.getEngInfinitive().equals("tell")) {
            noun.addGrammems(DATIVE);
        } else {
            noun.addGrammems(ACCUSATIVE);
        }
        be.doNotTranslate();
        verb.addGrammems(PLURAL, PAST, ACTIVE); //ToDo попытаться перейти на PASSIVE вместо ACTIVE
    }
}
