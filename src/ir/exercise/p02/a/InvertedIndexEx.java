package ir.exercise.p02.a;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class InvertedIndexEx {
    public static Directory createInvertedIndex() throws IOException {
        // Create the directory to store the index in memory
        Directory directory = new RAMDirectory();

        // Create the index writer configuration
        IndexWriterConfig config = new IndexWriterConfig(new SimpleAnalyzer());

        // Create the index writer
        IndexWriter indexWriter = new IndexWriter(directory, config);

        // Define the field type for the inverted index
        FieldType fieldType = new FieldType();
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        fieldType.setStored(true);
        fieldType.setTokenized(true);

        // Define the document collection
        List<String> documents = Arrays.asList(
                "Today is sunny.",
                "She is a sunny girl.",
                "To be or not to be.",
                "She is in Berlin today.",
                "Sunny Berlin!",
                "Berlin is always exciting!"
        );

        // Index each document
        for (int i = 0; i < documents.size(); i++) {
            String documentText = documents.get(i);

            // Split document text based on non-letter characters and lowercase
//            String[] terms = documentText.toLowerCase().split("[^a-z]+");
            List<String> terms = StandardTokenizerEx.tokenize(documentText.toLowerCase());

            // Create a new document
            Document document = new Document();

            // Add each term to the document
            for (String term : terms) {
                Field field = new Field("content", term, fieldType);
                document.add(field);
            }

            // Add the document to the index writer
            indexWriter.addDocument(document);
        }

        // Commit the changes and close the index writer
        indexWriter.commit();
        indexWriter.close();

        return directory;
    }

    public static void printInvertedIndex(Directory directory) throws IOException {
        // Create the index reader
        IndexReader indexReader = DirectoryReader.open(directory);

        // Iterate over the terms in the index
        Terms terms = MultiFields.getTerms(indexReader, "content");
        TermsEnum termsEnum = terms.iterator();

        // Print the inverted index
        while (termsEnum.next() != null) {
            String term = termsEnum.term().utf8ToString();
            PostingsEnum postingsEnum = termsEnum.postings(null);

            System.out.print("Term: " + term + ", Documents: ");

            // Iterate over the documents containing the term
            while (postingsEnum.nextDoc() != PostingsEnum.NO_MORE_DOCS) {
                int docId = postingsEnum.docID();
                System.out.print(docId + " ");
            }

            System.out.println();
        }

        // Close the index reader
        indexReader.close();
    }
}
