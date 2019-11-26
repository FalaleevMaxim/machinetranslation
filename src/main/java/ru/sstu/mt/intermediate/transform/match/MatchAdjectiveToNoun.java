package ru.sstu.mt.intermediate.transform.match;

import ru.sstu.mt.intermediate.model.GrammemsHolder;
import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractMatch;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.SklonyatorApi;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;
import ru.sstu.mt.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static ru.sstu.mt.sklonyator.enums.GrammemCategory.*;

public class MatchAdjectiveToNoun extends AbstractMatch {
    public MatchAdjectiveToNoun(SklonyatorApi sklonyator) {
        super(sklonyator, "Согласование прилагательного с существительным",
                new NodeCriteria()
                        .withType("NP")
                        .withInnerNodeCriterias(
                                new NodeCriteria()
                                        .withType("JJ", "JJR", "JJS", "RB", "RBR", "RBS")
                                        .named("adjective"),
                                new NodeCriteria()
                                        .withType("NN, NNS")
                                        .named("noun")));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        IRNode noun = queryResults.get("noun");
        IRNode adjective = queryResults.get("adjective");
        if (StringUtils.isNullOrEmpty(noun.getRusInfinitive()) || StringUtils.isNullOrEmpty(adjective.getRusInfinitive())) {
            return;
        }
        GrammemsHolder holder = new GrammemsHolder();
        noun.getGrammemsCollection().forEach(holder::add);
        try {
            List<RussianGrammem> grammems = sklonyator.getGrammems(noun.getRusInfinitive());
            grammems.forEach(holder::addIfNone);
            adjective.addGrammemsIfNone(holder.get(QUANTITY), holder.get(CASE), holder.get(GENDER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
