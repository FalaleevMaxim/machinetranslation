package ru.sstu.mt.intermediate.transform;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.sklonyator.enums.RussianPos;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class NodeCriteria {
    /**
     * Тип узла
     */
    private String[] types;

    /**
     * Оригинальный текст
     */
    private String[] engOriginal;

    /**
     * Начальная форма слова в английском
     */
    private String[] engInfinitive;

    /**
     * Начальная форма на русском
     */
    private String[] rusInfinitive;

    /**
     * Часть речи
     */
    private RussianPos pos;

    /**
     *  Проверки для узлов-потомков
     */
    private NodeCriteria[] innerNodeCriterias;

    /**
     * Проверка узла, следующего сразу за данным.
     * Невозможно проверить, если эта проверка корневая
     */
    private NodeCriteria following;

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
        if (!match(types, node.getType())) return null;
        if(!match(engOriginal, node.getEngOriginal())) return null;
        if(!match(engInfinitive, node.getEngInfinitive())) return null;
        if(!match(rusInfinitive, node.getRusInfinitive())) return null;

        Map<String, IRNode> result = new HashMap<>();
        if(innerNodeCriterias!=null) {
            int criteriaIndex = 0;
            List<IRNode> children = node.getChildren();
            for (int i = 0; i < children.size(); i++) {
                IRNode child = children.get(i);
                Map<String, IRNode> temp = innerNodeCriterias[criteriaIndex].query(child);
                if (temp == null) continue;
                if(innerNodeCriterias[criteriaIndex].following!=null) {
                    Map<String, IRNode> checkFollowing = checkFollowing(children, i, innerNodeCriterias[criteriaIndex].following);
                    if(checkFollowing==null) continue;
                    temp.putAll(checkFollowing);
                }
                result.putAll(temp);
                if (++criteriaIndex == innerNodeCriterias.length) break;
            }
            if(criteriaIndex<innerNodeCriterias.length) return null;
        }
        result.put(name, node);
        if(otherConditions!=null) {
            if(!otherConditions.test(node, result)) return null;
        }
        return result;
    }

    private Map<String, IRNode> checkFollowing (List<IRNode> children, int index, NodeCriteria followingCriteria) {
        if (index == children.size() - 1) return null;
        return followingCriteria.query(children.get(index+1));
    }

    private boolean match(String[] criteria, String node) {
        if(criteria==null) return true;
        for (String option : criteria) {
            if (option.equalsIgnoreCase(node)) return true;
        }
        return false;
    }

    public NodeCriteria withType(String... types) {
        this.types = types;
        return this;
    }

    public NodeCriteria withEngOriginal(String... engOriginal) {
        this.engOriginal = engOriginal;
        return this;
    }

    public NodeCriteria withEngInfinitive(String... engInfinitive) {
        this.engInfinitive = engInfinitive;
        return this;
    }

    public NodeCriteria withRusInfinitive(String... rusInfinitive) {
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

    public NodeCriteria named(String name) {
        this.name = name;
        return this;
    }

    public NodeCriteria followedBy(NodeCriteria following) {
        this.following = following;
        return this;
    }

    public boolean valid() {
        List<String> vars = allVars();
        Set<String> distinct = new HashSet<>();
        for (String var : vars) {
            if(!distinct.add(var)) return false;
        }
        return true;
    }

    private List<String> allVars() {
        if(innerNodeCriterias==null){
            return name==null? Collections.emptyList():Collections.singletonList(name);
        }
        List<String> vars = Arrays.stream(innerNodeCriterias).map(NodeCriteria::allVars).flatMap(Collection::stream).collect(Collectors.toList());
        if(name!=null) vars.add(name);
        return vars;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendIfNotNull(sb, types, "types");
        appendIfNotNull(sb, engOriginal, "original");
        appendIfNotNull(sb, engInfinitive, "infinitive");
        appendIfNotNull(sb, engInfinitive, "translated");
        if(innerNodeCriterias!=null) sb.append("inner checks; ");
        if(following!=null) sb.append("following check; ");
        String s = sb.toString();
        return s.isEmpty()?"empty check":s;
    }

    private void appendIfNotNull(StringBuilder sb, String[] field, String display) {
        if (field != null) sb.append(display).append(" = ").append(Arrays.toString(field)).append("; ");
    }
}
