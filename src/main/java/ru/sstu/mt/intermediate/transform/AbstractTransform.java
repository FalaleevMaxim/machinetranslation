package ru.sstu.mt.intermediate.transform;

import ru.sstu.mt.intermediate.model.IRNode;

import java.util.Map;

public abstract class AbstractTransform implements IRTransform {
    private final NodeCriteria criteria;

    public AbstractTransform(NodeCriteria criteria) {
        this.criteria = criteria;
    }

    /**
     * Проверяет условия для трансформации и производит трансформации над деревом
     *
     * @param ir Узел дерева
     * @return true ли условия выполнены, и трансформация произведена; иначе false
     */
    public boolean performIfPossible(IRNode ir) {
        Map<String, IRNode> result = criteria.query(ir);
        if(result==null) return false;
        perform(ir, result);
        return true;
    }

    /**
     * Производит трансформацию после успешного выполнения условий в критериях.
     *
     * @param ir Текущий узел промежуточного представления
     * @param queryResults Узлы, найденные в результате запроса критериев
     */
    public abstract void perform(IRNode ir, Map<String, IRNode> queryResults);
}
