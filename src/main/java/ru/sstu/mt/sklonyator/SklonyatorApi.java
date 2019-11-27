package ru.sstu.mt.sklonyator;

import ru.sstu.mt.dictionary.PosDetector;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;
import ru.sstu.mt.sklonyator.enums.RussianPos;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface SklonyatorApi extends PosDetector {
    /**
     * Получение части речи слова
     */
    List<RussianPos> getPos(String word) throws IOException;

    /**
     * Перевод слова к заданным формам
     *
     * @param word Слово в начальной форме, которое нужно привести к другой форме
     * @param grammems Список граммем, описывающих форму
     * @return список вариантов трансформации слова c дополнительной информацией.
     */
    List<String> transform(String word, Collection<RussianGrammem> grammems) throws IOException;

    List<WordFormInfo> transformWithInfo(String word, Collection<RussianGrammem> grammems) throws IOException;

    /**
     * Перевод слова к заданным формам
     *
     * @param word     Слово в начальной форме, которое нужно привести к другой форме
     * @param grammems Список граммем, описывающих форму
     * @param pos      Часть речи слова
     * @return список вариантов трансформации слова.
     */
    List<String> transform(String word, List<RussianGrammem> grammems, RussianPos pos) throws IOException;

    /**
     * Получение всех граммем (всей информации о форме слова)
     * @param word Слово в некоторой форме
     * @return Список всех граммем
     */
    List<RussianGrammem> getGrammems(String word) throws IOException;

    /**
     * Сколько осталось запросов после выполнения последнего запроса
     * @return limit из результата последнего запроса. -1 если запросов не было
     */
    int getLimit();
}
