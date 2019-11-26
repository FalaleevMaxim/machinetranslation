package ru.sstu.mt.intermediate.model;

import opennlp.tools.parser.Parse;
import opennlp.tools.util.Span;
import ru.sstu.mt.intermediate.transform.IRTransform;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;
import ru.sstu.mt.sklonyator.enums.RussianPos;
import ru.sstu.mt.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class IRNode {
    private final String type;
    private String engOriginal;
    private String engInfinitive;
    private String rusInfinitive;
    private String rusTransformed;
    private GrammemsHolder grammems;
    /**
     * Возможные части речи для перевода по словарю
     */
    private Set<RussianPos> posForDictionary;
    /**
     * Часть речи для склонения
     */
    private RussianPos pos;

    private List<IRNode> children = new ArrayList<>();
    private IRNode parent;

    public IRNode(Parse parse) {
        int start;
        start = parse.getSpan().getStart();
        this.type = parse.getType();
        if (parse.getChildCount() == 1 && "TK".equals(parse.getChildren()[0].getType())) {
            this.engOriginal = parse.getChildren()[0].getText();
        } else {
            for (Parse c : parse.getChildren()) {
                Span s = c.getSpan();
                IRNode child = new IRNode(c);
                child.parent = this;
                children.add(child);
                start = s.getEnd();
            }
        }
        if (start < parse.getSpan().getEnd()) {
            this.engOriginal = parse.getText().substring(start, parse.getSpan().getEnd());
        }
        grammems = new GrammemsHolder();
    }

    public IRNode(String type, String engOriginal) {
        this.type = type;
        this.engOriginal = engOriginal;
    }

    public String getType() {
        return type;
    }

    public String getEngOriginal() {
        return engOriginal;
    }

    public String getEngInfinitive() {
        return engInfinitive;
    }

    public void setEngInfinitive(String engInfinitive) {
        this.engInfinitive = engInfinitive;
    }

    public String getRusInfinitive() {
        return rusInfinitive;
    }

    public void setRusInfinitive(String rusInfinitive) {
        this.rusInfinitive = rusInfinitive;
    }

    public String getRusTransformed() {
        return rusTransformed;
    }

    public void setRusTransformed(String rusTransformed) {
        this.rusTransformed = rusTransformed;
    }

    public void doNotTranslate() {
        setRusInfinitive("");
        setRusTransformed("");
    }

    public List<IRNode> getChildren() {
        return children;
    }

    public List<IRNode> getChildrenOfType(String type) {
        return children.stream()
                .filter(node -> node.getType().equals(type))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return ("(" + type + ' ' + (engOriginal != null ? engOriginal : "") + (children.size() != 0 ? children.toString() : "") + ')').replaceAll("[\\[\\],]|\\(\\)", "");
    }

    public String getFullEngOriginal() {
        return getLeaves().stream().map(IRNode::getEngOriginal).map(StringUtils::emptyIfNull).collect(Collectors.joining(" "));
    }

    public String getFullEngInfinitive() {
        return getLeaves().stream().map(IRNode::getEngInfinitive).map(StringUtils::emptyIfNull).collect(Collectors.joining(" "));
    }

    public String getFullRusInfinitive() {
        return getLeaves().stream().map(IRNode::getRusInfinitive).map(StringUtils::emptyIfNull).collect(Collectors.joining(" "));
    }

    public String getFullRusTransformed() {
        return getLeaves().stream().map(IRNode::getRusTransformed).map(StringUtils::emptyIfNull).collect(Collectors.joining(" "));
    }

    public List<IRNode> getLeaves() {
        if (children.isEmpty()) return Collections.singletonList(this);
        return children.stream().map(IRNode::getLeaves).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public boolean applyTransform(IRTransform transform) {
        boolean applied = false;
        if (transform.performIfPossible(this)) applied = true;
        for (IRNode child : children) {
            if (child.applyTransform(transform)) applied = true;
        }
        return applied;
    }

    public GrammemsHolder getGrammems() {
        return grammems;
    }

    public Collection<RussianGrammem> getGrammemsCollection() {
        return grammems.toCollection();
    }

    public IRNode addGrammems(RussianGrammem... grammems) {
        for (RussianGrammem grammem : grammems) {
            this.grammems.add(grammem);
        }
        return this;
    }

    public IRNode addGrammemsIfNone(RussianGrammem... grammems) {
        for (RussianGrammem grammem : grammems) {
            this.grammems.addIfNone(grammem);
        }
        return this;
    }

    public RussianPos getPos() {
        return pos;
    }

    public IRNode setPos(RussianPos pos) {
        this.pos = pos;
        return this;
    }

    public IRNode getParent() {
        return parent;
    }

    public Set<RussianPos> getPosForDictionary() {
        return posForDictionary;
    }

    public IRNode addPosForDictionary(RussianPos... posForDictionary) {
        if (this.posForDictionary == null) {
            this.posForDictionary = new HashSet<>();
        }
        this.posForDictionary.addAll(Arrays.asList(posForDictionary));
        return this;
    }
}
