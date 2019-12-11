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
import ru.sstu.mt.intermediate.transform.match.*;
import ru.sstu.mt.intermediate.transform.post.*;
import ru.sstu.mt.intermediate.transform.pre.*;
import ru.sstu.mt.intermediate.transform.pre.phrases.DativeForNeed;
import ru.sstu.mt.intermediate.transform.pre.phrases.MakeSureThat;
import ru.sstu.mt.intermediate.transform.pre.phrases.MakeSureTo;
import ru.sstu.mt.intermediate.transform.pre.phrases.StandUp;
import ru.sstu.mt.intermediate.transform.pre.phrases.verbs.BreatheIn;
import ru.sstu.mt.intermediate.transform.pre.phrases.verbs.ComeIn;
import ru.sstu.mt.intermediate.transform.pre.phrases.verbs.HaveTo;
import ru.sstu.mt.intermediate.transform.pre.phrases.verbs.LookAt;
import ru.sstu.mt.pipeline.PipelineLogger;
import ru.sstu.mt.sklonyator.SklonyatorApi;
import ru.sstu.mt.sklonyator.SklonyatorApiImpl;
import ru.sstu.mt.sklonyator.WordFormInfo;
import ru.sstu.mt.util.GrammemsUtils;
import ru.sstu.mt.util.NameTransliteration;
import ru.sstu.mt.util.ParsePrettyPrinter;
import ru.sstu.mt.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.sstu.mt.pipeline.PipelineEvent.*;
import static ru.sstu.mt.sklonyator.enums.RussianGrammem.PASSIVE;
import static ru.sstu.mt.sklonyator.enums.RussianGrammem.PAST;
import static ru.sstu.mt.sklonyator.enums.RussianPos.PARTICIPLE;

public class Pipeline {
    private static final String DEFAULT_DICTIONARY_RESOURCE = "ENRUS.TXT";
    public static final List<IRTransform> ALL_PRE_TRANSLATE_RULES = Arrays.asList(
            //Закомментировать SetPos чтобы не использовать склонятор в словаре
            new SetPos(),
            new EmptyDT(),
            new EmptyDoInQuestion(),
            new EmptyDT(),
            new MakeSureThat(),
            new MakeSureTo(),
            new PunctuationStaysSame(),
            new PresentPerfect(),
            new ByDoing(),
            new OfSmth(),
            new Possessive(),
            new DidQuestion(),
            new BreatheIn(),
            new ComeIn(),
            new StandUp(),
            new PredicateToBe(),
            new DoNot(),
            new LookAt(),
            new PassiveVoice(),
            new CaseByPreposition(),
            new HaveTo(),
            new DativeForNeed(),
            new ToBeFormAndNoun(),
            new ToBeNoun()
    );
    public static final List<IRTransform> ALL_POST_TRANSLATE_RULES = Arrays.asList(
            new Comparative(),
            new Superlative(),
            new PluralNNS(),
            new AccusativeForDependentNouns(),
            new SeemAdjectiveInstrumental(),
            new ImperativeS(),
            new ImperativeTOP(),
            new ThanNominative(),
            new PastSimple()
    );

    public static final List<Function<SklonyatorApi, IRTransform>> ALL_MATCHING_RULES = Arrays.asList(
            WillFuture::new,
            MatchAdjectiveToNoun::new,
            MatchSecondAdjectiveToNoun::new,
            MatchAdjectiveAfterNoun::new,
            MatchVerbToNoun::new,
            MatchVerbToSecondNoun::new
    );

    private PipelineLogger logger = PipelineLogger.getLogger();

    private String sklonyatorApiKey;
    private String dictionaryFilePath;

    private StanfordCoreNLP stanfordNLP;
    private SklonyatorApi sklonyator;
    private Dictionary dictionary;
    private ParserModel parserModel;
    private Parser parser;
    private List<IRTransform> preTranslateRules;
    private List<IRTransform> postTranslateRules;
    private List<IRTransform> grammemsMatchRules;

    private final String sentence;
    private Parse parse;
    private IRNode ir;
    private String finalTranslation;

    private boolean infinitivesSet = false;
    private boolean translated = false;
    private boolean transformedRussian = false;
    private List<IRTransform> appliedPreTransforms;
    private List<IRTransform> appliedPostTransforms;
    private List<IRTransform> appliedMatches;

    public Pipeline(String sentence) {
        this.sentence = StringUtils.capitalize(sentence);
        logger.logEvent("Created pipeline for sentence:\n" + sentence + "\n", START);
    }

    public ParserModel getParserModel() {
        if (parserModel == null) {
            logger.logEvent("Loading parser model", PARSE);
            parserModel = ModelSource.getParserModel();
        }
        return parserModel;
    }

    public Parser getParser() {
        if (parser == null) {
            logger.logEvent("Creating parser", PARSE);
            parser = ParserFactory.create(getParserModel());
        }
        return parser;
    }

    /**
     * Парсинг предложения с помошью OpenNLP
     *
     * @return результат парсинга OpenNLP
     */
    public Parse parseNlp() {
        if (parse == null) {
            logger.logEvent("Parsing", PARSE);
            parse = ParserTool.parseLine(sentence, getParser(), 1)[0];
            logger.logResult(() -> "Parse tree:\n" + ParsePrettyPrinter.getPrettyPrint(parse), PARSE);
        }
        return parse;
    }

    /**
     * Перевод результата парсинга в промежуточное представление в виде дерева
     *
     * @return Промежуточное представление в виде дерева
     */
    public IRNode getIR() {
        if (ir == null) {
            Parse parse = parseNlp();
            logger.logEvent("Transforming to IR", TO_IR);
            ir = new IRNode(parse);
            logger.logResult(() -> "Intermediate representation:\n" + ParsePrettyPrinter.getPrettyPrint(ir.toString()) + "\n", TO_IR);
        }
        return ir;
    }

    public StanfordCoreNLP getStanfordNLP() {
        if (stanfordNLP == null) {
            logger.logEvent("Initializing StanfordNLP", STANFORD_NLP);
            stanfordNLP = StanfordNlpSource.getStanfordNLP();
        }
        return stanfordNLP;
    }

    /**
     * Для каждого слова в промежуточном представлении проставляется начальная форма
     *
     * @return ir
     */
    public IRNode setInfinitives() {
        if (!infinitivesSet) {
            getIR();
            logger.logEvent("Start setting infinitives", INFINITIVES);
            List<IRNode> leafs = ir.getLeaves();
            String sentence = leafs.stream().map(IRNode::getEngOriginal).collect(Collectors.joining(" "));
            CoreDocument document = new CoreDocument(sentence);
            getStanfordNLP().annotate(document);
            List<CoreLabel> tokens = document.tokens();
            for (int i = 0; i < tokens.size(); i++) {
                CoreLabel token = tokens.get(i);
                IRNode node = leafs.get(i);
                if (!node.getEngOriginal().equals(token.originalText())) {
                    throw new IllegalStateException(
                            String.format("Different original texts in node and token. Node: %s. Token: %s",
                                    node.getEngOriginal(), token.originalText()));
                }
                node.setEngInfinitive(token.lemma());
                if (node.getEngInfinitive() == null) {
                    node.setEngInfinitive("");
                }
            }
            infinitivesSet = true;
            logger.logResult(() -> "Infinitives:\n" + ir.getFullEngInfinitive() + "\n", INFINITIVES);
        }
        return ir;
    }

    /**
     * Возвращает список правил, которые отрабатывают до перевода инфинитивов на русский.
     * Это будут правила, которые обрабатывают особые случаи переводов для устойчивых выражений, меняют английские слова или устанавливают переводы на русский, не используя словарь.
     */
    public List<IRTransform> getPreTranslateRules() {
        if (preTranslateRules == null) {
            preTranslateRules = ALL_PRE_TRANSLATE_RULES;
        }
        return preTranslateRules;
    }

    /**
     * Возвращает список правил, которые отрабатывают после перевода инфинитивов на русский
     */
    public List<IRTransform> getPostTranslateRules() {
        if (postTranslateRules == null) {
            postTranslateRules = ALL_POST_TRANSLATE_RULES;
        }
        return postTranslateRules;
    }

    /**
     * Возвращает список правил, которые отрабатывают после перевода инфинитивов на русский
     */
    public List<IRTransform> getGrammemsMatchRules() {
        if (grammemsMatchRules == null) {
            SklonyatorApi sklonyator = getSklonyator();
            grammemsMatchRules = ALL_MATCHING_RULES.stream()
                    .map(f -> f.apply(sklonyator))
                    .collect(Collectors.toList());
        }
        return grammemsMatchRules;
    }

    /**
     * Преобразования дерева перед переводом
     *
     * @return Трансформации, которые успешно отработали
     */
    public List<IRTransform> preTransformIR() {
        if (appliedPreTransforms == null) {
            setInfinitives();
            logger.logEvent("Start transforming IR", PRE_TRANSFORM);
            appliedPreTransforms = getPreTranslateRules().stream()
                    .filter(ir::applyTransform)
                    .collect(Collectors.toList());
            logger.logResult(
                    () -> "Applied rules:\n" +
                            appliedPreTransforms.stream()
                                    .map(Object::toString)
                                    .distinct()
                                    .collect(Collectors.joining("\n")) + "\n",
                    PRE_TRANSFORM);
        }
        return appliedPreTransforms;
    }

    public Dictionary getDictionary() throws IOException {
        if (dictionary == null) {
            logger.logEvent("Loading dictionary", DICTIONARY);
            String fileName = dictionaryFilePath;
            if (fileName == null) {
                URL resource = ClassLoader.getSystemClassLoader().getResource(DEFAULT_DICTIONARY_RESOURCE);
                if (resource == null) throw new FileNotFoundException();
                fileName = resource.getFile();
            }
            dictionary = Dictionary.readDictionary(new File(fileName)).withPosDetector(getSklonyator());
        }
        return dictionary;
    }

    public SklonyatorApi getSklonyator() {
        if (sklonyator == null) {
            logger.logEvent("Initializing sklonyator api with key = " + sklonyatorApiKey, SKLONYATOR);
            sklonyator = new SklonyatorApiImpl(sklonyatorApiKey).setLogger(logger);
        }
        return sklonyator;
    }

    /**
     * Перевод начальных форм слов на русский с помощью словаря
     */
    public IRNode translateIR() throws IOException {
        if (!translated) {
            preTransformIR();
            Dictionary dictionary = getDictionary();
            logger.logEvent("Translating infinitives", TRANSLATE);
            for (IRNode leaf : ir.getLeaves()) {
                if (leaf.getEngInfinitive().isEmpty()) continue;
                if (leaf.getRusInfinitive() != null) continue;
                if (leaf.getType().equals("NNP")) {
                    leaf.setRusInfinitive(NameTransliteration.translit(leaf.getEngInfinitive()));
                } else {
                    leaf.setRusInfinitive(
                            dictionary.getTranslation(
                                    leaf.getEngInfinitive(),
                                    leaf.getPosForDictionary()));
                }
                if (leaf.getRusInfinitive() == null) {
                    if (leaf.getType().equals("JJR") || leaf.getType().equals("RBR") || leaf.getEngInfinitive().endsWith("er")) {
                        leaf.setRusInfinitive(
                                dictionary.getTranslation(
                                        leaf.getEngInfinitive().substring(0, leaf.getEngInfinitive().length() - 2),
                                        leaf.getPosForDictionary()));
                    }
                    if (leaf.getType().equals("JJS") || leaf.getType().equals("RBS") || leaf.getEngInfinitive().endsWith("st")) {
                        leaf.setRusInfinitive(
                                dictionary.getTranslation(
                                        leaf.getEngInfinitive().substring(0, leaf.getEngInfinitive().length() - 2),
                                        leaf.getPosForDictionary()));
                        if (leaf.getRusInfinitive() == null && leaf.getEngInfinitive().endsWith("est")) {
                            leaf.setRusInfinitive(
                                    dictionary.getTranslation(
                                            leaf.getEngInfinitive().substring(0, leaf.getEngInfinitive().length() - 3),
                                            leaf.getPosForDictionary()));
                        }
                    }
                    if (leaf.getRusInfinitive() == null) {
                        leaf.setRusInfinitive("");
                    }
                }
            }
            translated = true;
            logger.logResult(() -> "Translated:\n" + ir.getFullRusInfinitive() + "\n", TRANSLATE);
        }
        return ir;
    }

    /**
     * Преобразования внутреннего представления после перевода инфинитивов.
     * На этом этапе отрабатывают правила, устанавливающие грамматические формы для русских слов.
     */
    public List<IRTransform> postTransformIR() throws IOException {
        if (appliedPostTransforms == null) {
            translateIR();
            logger.logEvent("Applying rules to set grammems", POST_TRANSFORM);
            appliedPostTransforms = getPostTranslateRules().stream()
                    .filter(ir::applyTransform)
                    .collect(Collectors.toList());
            logger.logResult(
                    () -> "Applied rules:\n" +
                            appliedPostTransforms.stream()
                                    .map(Object::toString)
                                    .distinct()
                                    .collect(Collectors.joining("\n")) + "\n",
                    POST_TRANSFORM);
        }
        return appliedPostTransforms;
    }

    public List<IRTransform> matchGrammems() throws IOException {
        if (appliedMatches == null) {
            postTransformIR();
            appliedMatches = getGrammemsMatchRules().stream()
                    .filter(ir::applyTransform)
                    .collect(Collectors.toList());
            logger.logResult(
                    () -> "Applied matches:\n" +
                            appliedMatches.stream()
                                    .map(Object::toString)
                                    .distinct()
                                    .collect(Collectors.joining("\n")) + "\n",
                    GRAMMEMS_MATCH);
        }
        return appliedMatches;
    }

    /**
     * Склонение/спряжение слов на русском в соответствии с определёнными ранее формами
     */
    public IRNode transformRussian() throws IOException {
        if (!transformedRussian) {
            matchGrammems();
            SklonyatorApi sklonyator = getSklonyator();
            logger.logEvent("Start transforming russian words by grammems", TRANSFORM_RUSSIAN);
            for (IRNode leaf : ir.getLeaves()) {
                if (leaf.getRusInfinitive().isEmpty()) {
                    leaf.setRusTransformed("");
                }
                if (leaf.getGrammemsCollection().isEmpty()) {
                    leaf.setRusTransformed(leaf.getRusInfinitive());
                }
                if (leaf.getRusTransformed() != null) {
                    continue;
                }

                GrammemsUtils.setDefaults(leaf);
                if (leaf.getType().equals("PRP$")) {
                    leaf.setRusTransformed(GrammemsUtils.formPossessivePRP(
                            leaf.getEngInfinitive(),
                            leaf.getGrammemsCollection()));
                    continue;
                }

                List<WordFormInfo> transforms = sklonyator.transformWithInfo(leaf.getRusInfinitive(), leaf.getGrammemsCollection());
                String transformed;
                if (transforms.isEmpty()) {
                    logger.logEvent(String.format("Could not transform \"%s\" to form %s", leaf.getRusInfinitive(), leaf.getGrammemsCollection().toString()), TRANSFORM_RUSSIAN);
                    transformed = leaf.getRusInfinitive();
                } else if (transforms.size() == 1) {
                    transformed = transforms.get(0).getWord();
                } else {
                    List<WordFormInfo> byRecommended = transforms.stream()
                            .filter(fi -> fi.getPos() == leaf.getPos())
                            .collect(Collectors.toList());
                    if (!byRecommended.isEmpty()) {
                        transformed = byRecommended.get(0).getWord();
                        if (byRecommended.size() > 1) {
                            logger.logEvent(String.format(
                                    "Multiple options to transform \"%s\" to form %s with recommended part of speech %s:\n%s\nSelected: %s\n",
                                    leaf.getRusInfinitive(),
                                    leaf.getGrammemsCollection().toString(),
                                    leaf.getPos().description,
                                    byRecommended.stream().map(fi -> fi.getWord() + ":" + fi.getPos().systemName).collect(Collectors.joining(";")),
                                    transformed
                            ), TRANSFORM_RUSSIAN);
                        }
                    } else {
                        List<WordFormInfo> byPossible = transforms.stream()
                                .filter(fi -> leaf.getPosForDictionary().contains(fi.getPos()))
                                .collect(Collectors.toList());
                        if (!byPossible.isEmpty()) {
                            transformed = byPossible.get(0).getWord();
                            //Для JJ, заканчивающегося на ed (fried) предпочесть действительное причастие прошедшего времени, если такое есть
                            if (leaf.getType().equals("JJ") && leaf.getEngOriginal().endsWith("ed")) {
                                transformed = byPossible.stream()
                                        .filter(wi -> wi.getPos() == PARTICIPLE)
                                        .filter(wi -> wi.getGrammems().containsAll(Arrays.asList(PASSIVE, PAST)))
                                        .findFirst()
                                        .map(WordFormInfo::getWord)
                                        .orElse(transformed);
                            }
                            if (byPossible.size() > 1) {
                                logger.logEvent(String.format(
                                        "Multiple options to transform \"%s\" to form %s with possible parts of speech %s:\n%s\nSelected: %s\n",
                                        leaf.getRusInfinitive(),
                                        leaf.getGrammemsCollection().toString(),
                                        leaf.getPosForDictionary().stream().map(pos -> pos.systemName).collect(Collectors.joining(";")),
                                        byPossible.stream().map(fi -> fi.getWord() + ":" + fi.getPos().systemName).collect(Collectors.joining(";")),
                                        transformed
                                ), TRANSFORM_RUSSIAN);
                            }
                        } else {
                            transformed = transforms.get(0).getWord();
                            logger.logEvent(String.format(
                                    "Could not transform \"%s\" to form %s with possible parts of speech: %s.\nOptions are: %s.\nSelected first option: %s\n",
                                    leaf.getRusInfinitive(),
                                    leaf.getGrammemsCollection().toString(),
                                    leaf.getPosForDictionary().stream().map(pos -> pos.systemName).collect(Collectors.joining(";")),
                                    transforms.stream().map(fi -> fi.getWord() + ":" + fi.getPos().systemName).collect(Collectors.joining(";")),
                                    transformed
                            ), TRANSFORM_RUSSIAN);
                        }
                    }
                }
                leaf.setRusTransformed(transformed.toLowerCase());

                if (leaf.getRusTransformedPrefix() != null && (leaf.getType().equals("JJS") || leaf.getType().equals("RBS"))) {
                    List<String> prefixTransforms = sklonyator.transform(leaf.getRusTransformedPrefix(), leaf.getGrammemsCollection());
                    if (!prefixTransforms.isEmpty()) leaf.setRusTransformedPrefix(prefixTransforms.get(0));
                }
            }
            transformedRussian = true;
            logger.logResult(() -> "Russian transformed:\n" + ir.getFullRusTransformed() + "\n", TRANSFORM_RUSSIAN);
        }
        return ir;
    }

    public String getFinalTranslation() throws IOException {
        if (finalTranslation == null) {
            finalTranslation = StringUtils.capitalize(transformRussian().getFullRusTransformed()
                    .replaceAll(" +", " ")
                    .replaceAll(" ,", ",")
                    .replaceAll(" с с", " со с")
                    .replaceAll(" в в", " во в")
                    .replaceAll(" о о", " об о")
                    .trim());
            logger.logResult("Final translation:\n" + finalTranslation + "\n", FINISHED);
        }
        return finalTranslation;
    }

    public Pipeline setSklonyatorApiKey(String sklonyatorApiKey) {
        this.sklonyatorApiKey = sklonyatorApiKey;
        return this;
    }

    public Pipeline setDictionaryFilePath(String dictionaryFilePath) {
        this.dictionaryFilePath = dictionaryFilePath;
        return this;
    }

    public Pipeline setStanfordNLP(StanfordCoreNLP stanfordNLP) {
        this.stanfordNLP = stanfordNLP;
        return this;
    }

    public Pipeline setSklonyator(SklonyatorApi sklonyator) {
        this.sklonyator = sklonyator;
        return this;
    }

    public Pipeline setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
        return this;
    }

    public Pipeline setParserModel(ParserModel parserModel) {
        this.parserModel = parserModel;
        return this;
    }

    public Pipeline setParser(Parser parser) {
        this.parser = parser;
        return this;
    }

    public Pipeline setParse(Parse parse) {
        this.parse = parse;
        return this;
    }

    public Pipeline setIr(IRNode ir) {
        this.ir = ir;
        return this;
    }

    public String getSentence() {
        return sentence;
    }

    public Parse getParse() {
        return parse;
    }

    public boolean infinitivesSet() {
        return infinitivesSet;
    }

    public boolean translated() {
        return translated;
    }

    public PipelineLogger getLogger() {
        return logger;
    }
}
