package ru.sstu.mt.intermediate.model;

/**
 * Базовые части речи
 */
public enum BasicPos {
    NOUN("существительное", "NN", "NNP", "NNS"),
    ADJ("прилагательное", "JJ", "JJR", "JJS"),
    VERB("глагол", "VB", "VBD", "VBP", "VBG", "VBN", "VBZ"),
    PARTICIPLE("причастие"),
    DEE_PARTICIPLE("деепричастие"),
    NUM("числительное"),
    PRONOUN("местоимение", "PRP"),
    ADVERB("наречие"),
    PREP("предлог", "IN"),
    CONJ("союз"),
    INTERJ("междометие"),
    PART("частица"),
    PHRASE("фраза");

    public final String desc;
    public final String[] NodeTypes;

    BasicPos(String desc, String... nodeTypes) {
        this.desc = desc;
        NodeTypes = nodeTypes;
    }
}
