package ru.sstu.mt.util;

import opennlp.tools.parser.Parse;

import java.io.PrintStream;
import java.util.ArrayList;

public class ParsePrettyPrinter {
    private PrintStream out;

    public ParsePrettyPrinter() {
        this(System.out);
    }

    public ParsePrettyPrinter(PrintStream out) {
        this.out = out;
    }

    public void prettyPrint(Parse parse) {
        StringBuffer sb = new StringBuffer(parse.getText().length()*4);
        parse.show(sb);
        prettyPrint(sb.toString());
    }

    public void prettyPrint(String s) {

        char[] chars = s.toCharArray();

        int level = 0;
        ArrayList<Boolean> inner = new ArrayList<>();
        inner.add(false);
        for (char c : chars) {
            switch (c) {
                case '(':
                    inner.set(level, true);
                    out.println();
                    printOffset(level);
                    level++;
                    inner.add(false);
                    break;
                case ')':
                    boolean inn = inner.get(level);
                    inner.remove(level);
                    level--;
                    if(inn) {
                        out.println();
                        printOffset(level);
                    }
                    break;
                default:
                    break;
            }
            out.print(c);
        }
        out.println();
    }

    private void printOffset(int level) {
        for (int i = 0; i < level; i++) {
            out.print("|\t");
        }
    }
}
