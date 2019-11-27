package ru.sstu.mt.intermediate.transform.match;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractMatch;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.SklonyatorApi;

import java.util.Map;

public class MatchAdjectiveAfterNoun extends AbstractMatch {
    public MatchAdjectiveAfterNoun(SklonyatorApi sklonyator) {
        super(sklonyator, "Прилагательное после существительного",
                new NodeCriteria());
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {

    }
}
