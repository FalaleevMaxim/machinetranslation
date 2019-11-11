package ru.sstu.mt.analysis.opennlp;

import opennlp.tools.parser.ParserModel;

import java.io.IOException;
import java.io.InputStream;

public class ModelSource {
    private static ParserModel model;

    public static ParserModel getParserModel() {
        if(model==null) {
            try (InputStream modelIn = ClassLoader.getSystemResourceAsStream("en-parser-chunking.bin")) {
                if (modelIn != null) {
                    model = new ParserModel(modelIn);
                } else {
                    System.err.println("Can not read model");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return model;
    }
}
