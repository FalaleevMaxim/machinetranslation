package ru.sstu.mt.intermediate.transform.match;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractMatch;
import ru.sstu.mt.sklonyator.SklonyatorApi;

import java.util.Map;

public class MatchAdjectiveAfterNoun extends AbstractMatch {
    public MatchAdjectiveAfterNoun(SklonyatorApi sklonyator) {
        super(sklonyator, "рилагательное после существительного", criteria);
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {

    }
}
