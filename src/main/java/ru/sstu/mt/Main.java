package ru.sstu.mt;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserModel;
import ru.sstu.mt.dictionary.Dictionary;
import ru.sstu.mt.pipeline.LoggingMode;
import ru.sstu.mt.pipeline.PipelineEvent;
import ru.sstu.mt.sklonyator.SklonyatorApi;

import java.io.IOException;
import java.util.Scanner;

import static ru.sstu.mt.pipeline.PipelineLogger.DEFAULT_LOGGER;

public class Main {

    public static void main(String[] args) throws IOException {
        setDefaultLoggerParams();

        ParserModel parserModel = null;
        Parser parser = null;
        Dictionary dictionary = null;
        StanfordCoreNLP stanfordNLP = null;
        SklonyatorApi sklonyator = null;
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String sentence = sc.nextLine();
            Pipeline pipeline = new Pipeline(sentence)
                    .setParserModel(parserModel)
                    .setParser(parser)
                    .setDictionary(dictionary)
                    .setStanfordNLP(stanfordNLP)
                    .setSklonyator(sklonyator);
            setPipelineParams(pipeline);
            pipeline.getFinalTranslation();

            if (parser == null) parser = pipeline.getParser();
            if (dictionary == null) dictionary = pipeline.getDictionary();
            if (stanfordNLP == null) stanfordNLP = pipeline.getStanfordNLP();
            if (sklonyator == null) sklonyator = pipeline.getSklonyator();
            if (sklonyator != null && sklonyator.getLimit() >= 0) {
                System.err.println("Sklonyator limit: " + sklonyator.getLimit());
            }
        }
    }

    private static void setPipelineParams(Pipeline pipeline) {
        String sklonyatorApiKey = System.getProperty("sklonyatorApiKey");
        String dictionaryFilePath = System.getProperty("dictionaryFilePath");
        if (sklonyatorApiKey != null) pipeline.setSklonyatorApiKey(sklonyatorApiKey);
        if (dictionaryFilePath != null) pipeline.setDictionaryFilePath(dictionaryFilePath);
    }

    private static void setDefaultLoggerParams() {
        for (PipelineEvent event : PipelineEvent.values()) {
            String property = System.getProperty("log_" + event.name());
            if (property == null) continue;
            try {
                LoggingMode loggingMode = LoggingMode.valueOf(property);
                DEFAULT_LOGGER.setLogMode(loggingMode, event);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
