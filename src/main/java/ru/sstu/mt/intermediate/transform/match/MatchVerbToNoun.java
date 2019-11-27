package ru.sstu.mt.intermediate.transform.match;

import ru.sstu.mt.intermediate.model.GrammemsHolder;
import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractMatch;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.SklonyatorApi;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;
import ru.sstu.mt.util.GrammemsUtils;
import ru.sstu.mt.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static ru.sstu.mt.sklonyator.enums.GrammemCategory.*;

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
                                                        .named("verb"))));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        IRNode noun = queryResults.get("noun");
        IRNode verb = queryResults.get("verb");
        if (StringUtils.isNullOrEmpty(noun.getRusInfinitive()) || StringUtils.isNullOrEmpty(verb.getRusInfinitive())) {
            return;
        }
        GrammemsHolder holder = new GrammemsHolder();
        noun.getGrammemsCollection().forEach(holder::add);
        if (!noun.getType().equals("PRP")) {
            try {
                List<RussianGrammem> grammems = sklonyator.getGrammems(noun.getRusInfinitive());
                grammems.forEach(holder::addIfNone);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            GrammemsUtils.getGrammemsForPRP(noun.getEngInfinitive()).forEach(holder::addIfNone);
        }

        verb.addGrammems(holder.get(QUANTITY), holder.get(PERSON));
        if (holder.get(QUANTITY) == RussianGrammem.SINGULAR && holder.get(PERSON) == RussianGrammem.THIRD_PERSON) {
            verb.addGrammems(holder.get(GENDER));
        }
    }
}
