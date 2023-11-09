package ir.exercise.p02.a;

import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

import java.io.IOException;

public class IndexIntersection {
    public static void findByIntersection(Directory directory, String term1, String term2) throws IOException {
        // Load the previously created index
        IndexReader indexReader = DirectoryReader.open(directory);

        // Create the index search
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

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
        if(topDocs.totalHits > 0) {
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                int docId = scoreDoc.doc;
                System.out.println("Document ID: " + docId);
            }
        } else {
            System.out.println("No document exists containing both given terms");
        }

        // Close the index reader
        indexReader.close();
    }
}
