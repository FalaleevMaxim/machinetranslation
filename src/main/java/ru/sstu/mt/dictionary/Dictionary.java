package ru.sstu.mt.dictionary;

import ru.sstu.mt.sklonyator.enums.RussianPos;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Dictionary {
    private Map<String, List<Translation>> content = new HashMap<>();
    private PosDetector posDetector;



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

    public String getTranslation(String word, RussianPos pos) {
        word = word.toLowerCase();
        if(!containsTranslationFor(word)) return null;
        List<Translation> translationList = content.get(word);
        if(pos==null) return translationList.get(0).word;
        return translationList.stream()
                .filter(translation -> translation.pos.contains(pos))
                .map(Translation::getWord)
                .min(Comparator.comparingInt(s -> s.split(" ").length))
                .orElse(null);
    }

    public List<String> getTranslations(String word, RussianPos pos) {
        word = word.toLowerCase();
        if(!containsTranslationFor(word)) return Collections.emptyList();
        List<Translation> translationList = content.get(word);
        if(pos==null) return translationList.stream().map(Translation::getWord).collect(Collectors.toList());
        return translationList.stream()
                .filter(translation -> checkPos(translation, pos))
                .map(Translation::getWord)
                .collect(Collectors.toList());
    }

    public void addTranslation(String eng, String rus, RussianPos... pos) {
        List<Translation> translations;
        if (content.containsKey(eng)) {
            translations = content.get(eng);
        } else {
            translations = new ArrayList<>();
            content.put(eng, translations);
        }
        Translation tr = new Translation(rus);
        tr.pos = new HashSet<>(Arrays.asList(pos));
        translations.add(tr);
    }

    private boolean checkPos(Translation translation, RussianPos pos) {
        if(translation.pos==null) {
            if(posDetector==null) return false;
            try {
                if(translation.word.split(" ").length>1) {
                    translation.pos = Collections.singleton(RussianPos.PHRASE);
                    return RussianPos.PHRASE.equals(pos);
                }
                translation.pos = new HashSet<>(posDetector.getPos(translation.word));
            } catch (Exception e) {
                System.err.println("Ошибка получения частей речи для слова \""+translation.word+"\"");
                return false;
            }
        }
        return translation.pos.contains(pos);
    }

    public Dictionary withPosDetector(PosDetector posDetector) {
        this.posDetector = posDetector;
        return this;
    }

    private static class Translation {
        private String word;
        private Set<RussianPos> pos;

        public Translation(String word) {
            this.word = word;
        }

        public String getWord() {
            return word;
        }

        public Set<RussianPos> getPos() {
            return pos==null?Collections.emptySet():pos;
        }
    }
}
