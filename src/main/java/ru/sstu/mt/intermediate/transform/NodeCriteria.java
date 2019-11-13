package ru.sstu.mt.intermediate.transform;

import ru.sstu.mt.intermediate.model.IRNode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class NodeCriteria {
    /**
     * Тип узла
     */
    private String type;

    /**
     * Оригинальный текст
     */
    private String engOriginal;

    /**
     * Начальная форма слова в английском
     */
    private String engInfinitive;

    /**
     * Начальная форма на русском
     */
    private String rusInfinitive;

    /**
     *  Проверки для узлов-потомков
     */
    private NodeCriteria[] innerNodeCriterias;

    /**
     * Предикат для дополнительных проверок, для которых недостаточно полей выше.
     * Принимает текущий узел и переменные, найденные при обходе
     */
    private BiPredicate<IRNode, Map<String, IRNode>> otherConditions;

    private String name;

    /**
     * Запрос по критериям, начиная с текущего узла
     *
     * @return Переменные, определённые критериями узлов или {code null} если узел не соответствует запросу.
     */
    public Map<String, IRNode> query(IRNode node) {
        if(!match(type, node.getType())) return null;
        if(!match(engOriginal, node.getEngOriginal())) return null;
        if(!match(engInfinitive, node.getEngInfinitive())) return null;
        if(!match(rusInfinitive, node.getRusInfinitive())) return null;

        Map<String, IRNode> result = new HashMap<>();
        if(innerNodeCriterias.length>0) {
            int i = 0;
            for (IRNode child : node.getChildren()) {
                Map<String, IRNode> temp = innerNodeCriterias[i].query(child);
                if(temp==null) continue;
                result.putAll(temp);
                if(++i==innerNodeCriterias.length) break;
            }
            if(i<innerNodeCriterias.length) return null;
        }
        result.put(name, node);
        if(otherConditions!=null) {
            if(!otherConditions.test(node, result)) return null;
        }
        return result;
    }

    private boolean match(Object criteria, Object node) {
        if(criteria==null) return true;
        return criteria.equals(node);
    }

    public NodeCriteria withType(String type) {
        this.type = type;
        return this;
    }

    public NodeCriteria withEngOriginal(String engOriginal) {
        this.engOriginal = engOriginal;
        return this;
    }

    public NodeCriteria withEngInfinitive(String engInfinitive) {
        this.engInfinitive = engInfinitive;
        return this;
    }

    public NodeCriteria withRusInfinitive(String rusInfinitive) {
        this.rusInfinitive = rusInfinitive;
        return this;
    }

    public NodeCriteria withOtherConditions(BiPredicate<IRNode, Map<String, IRNode>> otherConditions) {
        this.otherConditions = otherConditions;
        return this;
    }

    public NodeCriteria withInnerNodeCriterias(NodeCriteria... innerNodeCriterias) {
        this.innerNodeCriterias = innerNodeCriterias;
        return this;
    }

    public NodeCriteria setName(String name) {
        this.name = name;
        return this;
    }
}
