package ru.sstu.mt.intermediate.transform.match;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractMatch;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.SklonyatorApi;
import ru.sstu.mt.util.GrammemsUtils;

import java.util.List;
import java.util.Map;

public class MatchVerbToNoun extends AbstractMatch {
    public MatchVerbToNoun(SklonyatorApi sklonyator) {
        super(sklonyator, "Согласование формы грагола с подлежащим",
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
                                                        .withType("VB", "VBP", "VBD", "VBZ")
                                                        .named("verb")))
                        // Условие добавлено чтобы не было пересечений с MatchVerbToSecondNoun
                        .withOtherConditions((node, vars) -> {
                            List<IRNode> children = node.getChildren();
                            int nounInd = children.indexOf(vars.get("noun"));
                            int verbInd = children.indexOf(vars.get("verb"));
                            for (int i = nounInd + 1; i < verbInd; i++) {
                                if (children.get(i).getType().equals("NP")) {
                                    return false;
                                }
                            }
                            return true;
                        }));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        IRNode noun = queryResults.get("noun");
        IRNode verb = queryResults.get("verb");
        GrammemsUtils.matchVerbToNoun(verb, noun, sklonyator);
    }
}
