package ru.sstu.mt;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import ru.sstu.mt.analysis.opennlp.ModelSource;
import ru.sstu.mt.analysis.stanfordnlp.StanfordNlpSource;
import ru.sstu.mt.dictionary.Dictionary;
import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.IRTransform;
import ru.sstu.mt.sklonyator.SklonyatorApi;
import ru.sstu.mt.sklonyator.SklonyatorApiImpl;
import ru.sstu.mt.util.NameTransliteration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Pipeline {

    public static ParserModel getParserModel() {
        return ModelSource.getParserModel();
    }

    public static Parser createParser(ParserModel model) {
        return ParserFactory.create(model);
    }

    /**
     * Парсинг предложения с помошью OpenNLP
     * @param parser парсер penNLP
     * @param sentence предложение на английском
     * @return результат парсинга OpenNLP
     */
    public static Parse parseNlp(String sentence, Parser parser) {
        return ParserTool.parseLine(sentence, parser, 1)[0];
    }

    /**
     * Перевод результата парсинга в промежуточное представление в виде дерева
     * @param parse результат парсинга OpenNLP
     * @return Промежуточное представление в виде дерева
     */
    public static IRNode convertToIR(Parse parse) {
        return new IRNode(parse);
    }

    public static StanfordCoreNLP getStanfordNLP () {
        return StanfordNlpSource.getStanfordNLP();
    }

    /**
     * Для каждого слова в промежуточном представлении проставляется начальная форма
     * @param stanfordNLP Инструмент StanfordNLP для определения инфинитивов
     * @param ir Промежуточное представление
     * @return ir
     */
    public static IRNode setInfinitives(StanfordCoreNLP stanfordNLP, IRNode ir) {
        List<IRNode> leafs = ir.getLeafs();
        String sentence = leafs.stream().map(IRNode::getEngOriginal).collect(Collectors.joining(" "));
        CoreDocument document = new CoreDocument(sentence);
        stanfordNLP.annotate(document);
        List<CoreLabel> tokens = document.tokens();
        for (int i = 0; i < tokens.size(); i++) {
            CoreLabel token = tokens.get(i);
            IRNode node = leafs.get(i);
            if(!node.getEngOriginal().equals(token.originalText())) {
                throw new IllegalStateException("Different original texts in node and token");
            }
            node.setEngInfinitive(token.lemma());
        }
        return ir;
    }

    /**
     * Возвращает список правил, которые отрабатывают до перевода инфинитивов на русский.
     * Это будут правила, которые обрабатывают особые случаи переводов для устойчивых выражений, меняют английские слова или устанавливают переводы на русский, не используя словарь.
     */
    public static List<IRTransform> getPreTransforms() {
        return Collections.emptyList(); //ToDo добавить правила
    }

    /**
     * Возвращает список правил, которые отрабатывают после перевода инфинитивов на русский
     */
    public static List<IRTransform> getPostTransforms() {
        return Collections.emptyList(); //ToDo добавить правила
    }

    /**
     * Преобразования дерева перед переводом
     * @param ir
     * @return Трансформации, которые успешно отработали
     */
    public List<IRTransform> preTransformIR(IRNode ir) {
        return getPreTransforms().stream()
                .filter(ir::applyTransform)
                .collect(Collectors.toList());
    }

    public static Dictionary getDictionary() throws IOException {
        String fileName = ClassLoader.getSystemClassLoader().getResource("ENRUS.TXT").getFile();
        return Dictionary.readDictionary(new File(fileName));
    }

    public static SklonyatorApi getSklonyator() {
        return new SklonyatorApiImpl();
    }

    /**
     * Перевод начальных форм слов на русский с помощью словаря
     * @param dictionary
     * @param ir
     */
    public static void translateIR(Dictionary dictionary, IRNode ir) {
        for (IRNode leaf : ir.getLeafs()) {
            if(leaf.getType().equals("NNP")) {
                leaf.setRusInfinitive(NameTransliteration.translit(leaf.getEngInfinitive()));
            } else {
                leaf.setRusInfinitive(
                        dictionary.getTranslation(
                                leaf.getEngInfinitive(),
                                null)); //ToDo Добавить проверку части речи
            }
        }
    }

    /**
     * Согласование падежей и прочих грамматических признаков слов
     */
    public static List<IRTransform> matchGrammems(IRNode ir) {
        return getPostTransforms().stream()
                .filter(ir::applyTransform)
                .collect(Collectors.toList());
    }

    /**
     * Склонение/спряжение слов на русском в соответствии с определёнными ранее формами
     */
    public static void modifyRussian() {
        //ToDo применять склонятор для приведения слов в нужные формы
    }
}
