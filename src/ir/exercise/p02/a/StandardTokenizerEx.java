package ir.exercise.p02.a;

import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class StandardTokenizerEx {
    public static List<String> tokenize(String text) throws IOException {
        List<String> tokens = new ArrayList<>();
        // StandardTokenizer
        StandardTokenizer standardTokenizer = new StandardTokenizer();
        standardTokenizer.setReader(new StringReader(text));
        standardTokenizer.reset();

//        System.out.println("StandardTokenizer:");
        while (standardTokenizer.incrementToken()) {
            CharTermAttribute charTermAttribute = standardTokenizer.getAttribute(CharTermAttribute.class);
            tokens.add(charTermAttribute.toString());

//            System.out.print("\"" + charTermAttribute.toString() + "\"" + ", ");
        }
        return tokens;
    }
}

