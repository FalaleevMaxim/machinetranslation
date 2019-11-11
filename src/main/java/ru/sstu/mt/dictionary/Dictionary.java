package ru.sstu.mt.dictionary;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Dictionary {
    private Map<String, List<Translation>> content = new HashMap<>();

    public static Dictionary readDictionary(File file) throws IOException {
        Dictionary dictionary = new Dictionary();

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            for(String line; (line = br.readLine()) != null; ) {
                String eng = line.toLowerCase();
                String tr = br.readLine();
                if(tr==null) break;
                if(eng.contains(" ")) continue;

                String[] translations = tr.split("\t");
                List<Translation> translationList = new ArrayList<>();
                for (String translation : translations) {
                    translation = translation.replaceAll("\\(.*\\)", "");
                    translation = translation.replaceAll(".*\\)", "");
                    translation = translation.replaceAll("[.,]", "");
                    translationList.add(new Translation(translation));
                }
                if(!translationList.isEmpty()) {
                    dictionary.content.put(eng, translationList);
                }
            }
        }
        return dictionary;
    }

    private Dictionary() {

    }

    public boolean containsTranslationFor(String word) {
        return content.containsKey(word);
    }

    public int countTranslationFor(String word) {
        if(!content.containsKey(word)) return 0;
        return content.get(word).size();
    }

    public String getTranslation(String word) {
        return getTranslation(word, null);
    }

    public String getTranslation(String word, String pos) {
        word = word.toLowerCase();
        if(!containsTranslationFor(word)) return null;
        List<Translation> translationList = content.get(word);
        if(pos==null) return translationList.get(0).word;
        return translationList.stream()
                .filter(translation -> translation.pos.equalsIgnoreCase(pos))
                .map(Translation::getWord)
                .min(Comparator.comparingInt(s -> s.split(" ").length))
                .orElse(null);
    }

    public List<String> getTranslations(String word, String pos) {
        word = word.toLowerCase();
        if(!containsTranslationFor(word)) return Collections.emptyList();
        List<Translation> translationList = content.get(word);
        if(pos==null) return translationList.stream().map(Translation::getWord).collect(Collectors.toList());
        return translationList.stream()
                .filter(translation -> translation.pos.equalsIgnoreCase(pos))
                .map(Translation::getWord)
                .collect(Collectors.toList());
    }

    public void addTranslation(String eng, String rus, String pos) {
        List<Translation> translations;
        if (content.containsKey(eng)) {
            translations = content.get(eng);
        } else {
            translations = new ArrayList<>();
            content.put(eng, translations);
        }
        translations.add(new Translation(rus, pos));
    }

    private static class Translation {
        private String word;
        private String pos;

        public Translation(String word) {
            this.word = word;
        }

        public Translation(String word, String pos) {
            this.word = word;
            this.pos = pos;
        }

        public String getWord() {
            return word;
        }

        public String getPos() {
            return pos;
        }
    }
}
