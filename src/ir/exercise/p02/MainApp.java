package ir.exercise.p02;

import ir.exercise.p02.a.IndexIntersection;
import ir.exercise.p02.a.InvertedIndexEx;
import ir.exercise.p02.b.PostingListEx;
import org.apache.lucene.store.Directory;

import java.io.IOException;

public class MainApp {
    public static void main(String[] args) throws IOException {
        // Create the inverted index
        Directory directory = InvertedIndexEx.createInvertedIndex();

        // Print the inverted index
        System.out.println("----------Inverted Index-----------");
        InvertedIndexEx.printInvertedIndex(directory);
        System.out.println();

        // Print all the documents that contain the words “sunny” and “exciting”.
        String term1 = "sunny";
        String term2 = "exciting";
        System.out.println("---------- Documents containing both \"" + term1 + "\" and \"" + term2 + "\" -----------");
        IndexIntersection.findByIntersection(directory, term1, term2);
        System.out.println();

        // Print the posting list for the term "sunny"
//        PostingListEx.printPostingList( directory, "sunny");
        PostingListEx.printPostingList( directory, "sunny");

        // Print the posting list for the term "to"
        PostingListEx.printPostingList(directory, "to");
    }
}