package ru.sstu.mt.intermediate.model;

import opennlp.tools.parser.Parse;
import opennlp.tools.util.Span;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class IRNode {
    private final String type;
    private String engOriginal;
    private String engInfinitive;
    private String rusInfinitive;
    private String rusTransformed;

    private List<IRNode> children = new ArrayList<>();

    public IRNode(Parse parse) {
        int start;
        start = parse.getSpan().getStart();
        this.type = parse.getType();
        if(parse.getChildCount()==1 && "TK".equals(parse.getChildren()[0].getType())) {
            this.engOriginal = parse.getChildren()[0].getText();
        } else {
            for (Parse c : parse.getChildren()) {
                Span s = c.getSpan();
                children.add(new IRNode(c));
                start = s.getEnd();
            }
        }
        if (start < parse.getSpan().getEnd()) {
            this.engOriginal = parse.getText().substring(start, parse.getSpan().getEnd());
        }
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
        return ("(" + type + ' ' + (engOriginal!=null?engOriginal:"") + (children.size()!=0?children.toString():"") + ')').replaceAll("[\\[\\],]|\\(\\)", "");
    }

    public List<IRNode> getLeafs() {
        if(children.isEmpty()) return Collections.singletonList(this);
        return children.stream().map(IRNode::getLeafs).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
