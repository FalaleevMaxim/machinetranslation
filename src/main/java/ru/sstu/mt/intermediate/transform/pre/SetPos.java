package ru.sstu.mt.intermediate.transform.pre;

import ru.sstu.mt.intermediate.model.IRNode;
import ru.sstu.mt.intermediate.transform.AbstractTransform;
import ru.sstu.mt.intermediate.transform.NodeCriteria;

import java.util.Map;

import static ru.sstu.mt.sklonyator.enums.RussianPos.*;

public class SetPos extends AbstractTransform {

    public SetPos() {
        super("Установка частей речи по тегам", new NodeCriteria()
                .withOtherConditions((node, stringIRNodeMap) -> node.getChildren().isEmpty()));
    }

    @Override
    public void perform(IRNode ir, Map<String, IRNode> queryResults) {
        switch (ir.getType()) {
            case "CC":
                ir.addPosForDictionary(CONJ);
                break;
            case "CD":
                ir.addPosForDictionary(NUM);
                break;
            case "EX":
                ir.addPosForDictionary(PART);
                break;
            case "IN":
                ir.addPosForDictionary(PREP, CONJ);
                break;
            case "JJ":
                ir.setPos(ADJ);
            case "JJR":
            case "JJS":
                ir.addPosForDictionary(ADJ, SHORT_ADJ, PRONOUN_ADJ);
                break;
            case "MD":
                ir.addPosForDictionary(VERB_PERS, VERB_INF);
                break;
            case "NN":
            case "NNS":
            case "NNP":
            case "NNPS":
                ir.addPosForDictionary(NOUN);
                break;
            case "PDT":
                ir.addPosForDictionary(ADVERB, PRONOUN_N, PRONOUN_ADJ);
                break;
            case "PRP":
            case "PRP$":
                ir.addPosForDictionary(PRONOUN_N, PRONOUN_ADJ, PRONOUN_PR);
                break;
            case "RB":
            case "RBR":
            case "RBS":
                ir.addPosForDictionary(ADVERB, ADJ, SHORT_ADJ, PRONOUN_ADJ);
                break;
            case "RP":
                ir.addPosForDictionary(PART, PREP);
                break;
            case "TO":
                ir.addPosForDictionary(CONJ, PREP);
                break;
            case "UH":
                ir.addPosForDictionary(INTERJ);
                break;
            case "VB":
            case "VBD":
            case "VBG":
            case "VBN":
            case "VBP":
            case "VBZ":
                ir.addPosForDictionary(VERB_INF, VERB_PERS);
                break;
            case "WP":
                ir.addPosForDictionary(PRONOUN_N, PRONOUN_ADJ, CONJ);
                break;
            case "WDT":
                ir.addPosForDictionary(PRONOUN_ADJ, CONJ, PRONOUN_N);
                break;
            case "WRB":
            case "WP$":
                ir.addPosForDictionary(PRONOUN_ADJ, CONJ, PRONOUN_N);
                break;
        }
    }
}