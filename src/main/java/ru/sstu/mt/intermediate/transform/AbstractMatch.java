package ru.sstu.mt.intermediate.transform;

import ru.sstu.mt.sklonyator.SklonyatorApi;

/**
 * Базовый класс для правил, согласующих формы слов.
 * Для этих правил нужен склонятор, чтобы брать форму одного слова, и передавать другому слову.
 */
public abstract class AbstractMatch extends AbstractTransform {
    protected final SklonyatorApi sklonyator;

    public AbstractMatch(SklonyatorApi sklonyator, String desc, NodeCriteria criteria) {
        super(desc, criteria);
        this.sklonyator = sklonyator;
    }
}
