package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

import static ru.sstu.mt.sklonyator.enums.RussianGrammem.*;

public class ImperativeS extends AbstractTransform {
    public ImperativeS() {
        super("Повелительное наклонение если нет подлежащего", new NodeCriteria()
                .withType("S")
                .withOtherConditions((node, vars) -> node.getChildren().stream().noneMatch(ch -> ch.getType().equals("NP")))
                .withInnerNodeCriterias(
                        new NodeCriteria()
                                .withType("VP")
                                .withInnerNodeCriterias(
                                        new NodeCriteria()
                                                .withType("VB")
                                                .named("verb"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        IRNode verb = queryResults.get("verb");

        verb.addGrammems(IMPERATIVE, PLURAL, SECOND_PERSON);
    }
}
