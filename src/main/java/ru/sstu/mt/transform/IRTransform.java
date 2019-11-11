package ru.sstu.mt.transform;

import ru.sstu.mt.intermediate.model.IRNode;

/**
 * Преобрзование над промежуточным педставлением
 */
public interface IRTransform {
    /**
     * Проверяет, чтоузел соответствует условиям для трансформации
     * @param ir узел промежуточного представления, с котторого нужно начать определение шаблона
     * @return true если можно произвести трансформацию
     */
    boolean detect(IRNode ir);

    /**
     * Произвести трансформацию дерева
     * @param ir узел дерева
     */
    void perform(IRNode ir);

    default boolean performIfPossible(IRNode ir) {
        if(detect(ir)) {
            perform(ir);
            return true;
        }
        return false;
    }
}
