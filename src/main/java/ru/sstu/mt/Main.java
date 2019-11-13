package ru.sstu.mt;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.StringUtils;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import ru.sstu.mt.analysis.opennlp.ModelSource;
import ru.sstu.mt.analysis.stanfordnlp.StanfordUtils;
import ru.sstu.mt.dictionary.Dictionary;
import ru.sstu.mt.sklonyator.SklonyatorApi;
import ru.sstu.mt.sklonyator.enums.RussianPoS;
import ru.sstu.mt.util.ParsePrettyPrinter;
import ru.sstu.mt.intermediate.model.IRNode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static ru.sstu.mt.Pipeline.*;

public class Main {

    public static void main(String[] args) throws IOException {
        /*ParserModel parserModel = ModelSource.getParserModel();
        Parser parser = ParserFactory.create(parserModel);

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String sentence = sc.nextLine();
            Parse[] parses = ParserTool.parseLine(sentence, parser, 1);
            Parse parse = parses[0];
            new ParsePrettyPrinter().prettyPrint(new IRNode(parse).toString());
            System.out.println(StanfordUtils.getWordsInfo(sentence)
                    .stream()
                    .map(StanfordUtils.WordInfo::toString)
                    .collect(Collectors.joining("\n")));
        }*/

        Dictionary dictionary = getDictionary();
        StanfordCoreNLP stanfordNLP = getStanfordNLP();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String sentence = sc.nextLine();

            IRNode ir = new IRNode(parseNlp(sentence, createParser(getParserModel())));
            setInfinitives(stanfordNLP, ir);
            translateIR(dictionary, ir);

            List<IRNode> leafs = ir.getLeafs();

            SklonyatorApi sklonyator = getSklonyator();
            RussianPoS pos = sklonyator.getPos(leafs.get(0).getRusInfinitive());
            System.out.println(pos);
            System.out.println(sklonyator.getLimit());

            System.out.println(leafs.stream().map(IRNode::getEngInfinitive).collect(Collectors.joining(" ")));
            System.out.println(leafs.stream().map(IRNode::getRusInfinitive).collect(Collectors.joining(" ")));
            new ParsePrettyPrinter().prettyPrint(ir.toString());
        }
    }
}
