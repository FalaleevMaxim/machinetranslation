package ru.sstu.mt.analysis.stanfordnlp;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

public class StanfordNlpSource {
    private static Properties properties;
    private static String propertiesName = "tokenize, ssplit, pos, lemma";
    private static StanfordCoreNLP stanfordCoreNLP;

    static {
        properties = new Properties();
        properties.setProperty("annotators", propertiesName);
    }

    private StanfordNlpSource() {
    }

    public static StanfordCoreNLP getStanfordNLP() {
        if(stanfordCoreNLP==null) {
            stanfordCoreNLP = new StanfordCoreNLP(properties);
        }
        return stanfordCoreNLP;
    }
}
