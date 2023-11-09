package ir.exercise.p02.b;

import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostingListEx {
    public static void printPostingList(Directory directory, String term) throws IOException {
        // Load the previously created index
        IndexReader indexReader = DirectoryReader.open(directory);

        // Get the posting list for the term
        Terms terms = MultiFields.getTerms(indexReader, "content");
        TermsEnum termsEnum = terms.iterator();
        if (termsEnum.seekExact(new BytesRef(term))) {
            PostingsEnum postingsEnum = termsEnum.postings(null, PostingsEnum.POSITIONS);

            System.out.print("[" + term + ": total frequency = " + termsEnum.totalTermFreq() +
                    ", doc frequency = " + termsEnum.docFreq() + "] -> ");

            List<String> documents = new ArrayList<>();

            int docId;
            while ((docId = postingsEnum.nextDoc()) != PostingsEnum.NO_MORE_DOCS) {
                StringBuilder docString = new StringBuilder();
                docString.append("[").append(docId).append(": frequency = ").append(postingsEnum.freq())
                        .append(": positions = [");

                for (int i = 0; i < postingsEnum.freq(); i++) {
                    int position = postingsEnum.nextPosition();
                    docString.append(position).append(" ");
                }

                docString.append("]]");
                documents.add(docString.toString());
            }

            System.out.println(String.join(" -> ", documents));
        } else {
            System.out.println("Term not found");
        }
    }
}
