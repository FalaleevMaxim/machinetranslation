package ru.sstu.mt.analysis.stanfordnlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.List;
import java.util.stream.Collectors;

public class StanfordUtils {
    private static StanfordCoreNLP stanfordCoreNLP = StanfordNlpSource.getStanfordNLP();

    public static List<WordInfo> getWordsInfo(String sentence) {
        CoreDocument document = new CoreDocument(sentence);
        stanfordCoreNLP.annotate(document);
        List<CoreLabel> tokens = document.tokens();
        return tokens.stream()
                .map(label -> new WordInfo(label.originalText(), label.get(CoreAnnotations.PartOfSpeechAnnotation.class), label.lemma()))
                .collect(Collectors.toList());
    }

    public static class WordInfo {
        public final String word;
        public final String pos;
        public final String lemma;

        public WordInfo(String word, String pos, String lemma) {
            this.word = word;
            this.pos = pos;
            this.lemma = lemma;
        }

        @Override
        public String toString() {
            return pos + ' ' + word + " (" + lemma + ')';
        }
    }
}
