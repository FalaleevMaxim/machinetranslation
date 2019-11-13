package ru.sstu.mt.intermediate.transform;

import ru.sstu.mt.intermediate.model.IRNode;

/**
 * Преобрзование над промежуточным педставлением
 */
public interface IRTransform {



    /**
     * Проверяет условия для трансформации и производит трансформации над деревом
     *
     * @param ir Узел дерева
     * @return true ли условия выполнены, и трансформация произведена; иначе false
     */
    boolean performIfPossible(IRNode ir);


}
