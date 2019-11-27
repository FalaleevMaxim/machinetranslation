package ru.sstu.mt.intermediate.transform.match;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractMatch;
import ru.sstu.mt.intermediate.transform.NodeCriteria;
import ru.sstu.mt.sklonyator.SklonyatorApi;

import java.util.Map;

public class WillFuture extends AbstractMatch {
    public WillFuture(SklonyatorApi sklonyator) {
        super(sklonyator, "Will укзывает на будущее время",
                new NodeCriteria());
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {

    }
}
