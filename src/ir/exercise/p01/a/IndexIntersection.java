package ir.exercise.p01.a;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class IndexIntersection {
    public static void main(String[] args) throws IOException {
        // Create the directory to store the index in memory
        Directory directory = new RAMDirectory();

        // Create the index writer configuration
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());

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
            String[] terms = documentText.toLowerCase().split("[^a-z]+");

            // Create a new Lucene document
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









        // Load the previously created index
        IndexReader indexReader = DirectoryReader.open(directory);

        // Create the index search
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // Define the search terms
        String term1 = "sunny";
        String term2 = "exciting";

        // Create the term queries for the search terms
        Query query1 = new TermQuery(new Term("content", term1));
        Query query2 = new TermQuery(new Term("content", term2));

        // Create the boolean query for the intersection
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        booleanQueryBuilder.add(query1, BooleanClause.Occur.MUST);
        booleanQueryBuilder.add(query2, BooleanClause.Occur.MUST);

        // Execute the intersection search
        TopDocs topDocs = indexSearcher.search(booleanQueryBuilder.build(), Integer.MAX_VALUE);

        // Print the documents that contain both terms
        System.out.println("Documents containing both \"" + term1 + "\" and \"" + term2 + "\":");
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            int docId = scoreDoc.doc;
            System.out.println("Document ID: " + docId);
        }

        // Close the index reader
        indexReader.close();
    }
}
