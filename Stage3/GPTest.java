package edu.yu.cs.com1320.project;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import edu.yu.cs.com1320.project.Trie;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore;
import edu.yu.cs.com1320.project.stage3.DocumentStore.DocumentFormat;
import edu.yu.cs.com1320.project.stage3.impl.DocumentStoreImpl;

public class GPTest {

    @Test
    public void complicatedTrieTest() {
        Trie trie = new TrieImpl<Integer>();
        trie.put("APPLE123", 1);
        trie.put("APPLE123", 2);
        trie.put("APPLE123", 3);
        trie.put("APPle87", 8);
        trie.put("aPpLe87", 7);
        List<Integer> appleList = trie.getAllSorted("apple123", (int1, int2) -> {
            if ((int) int1 < (int) int2) {
                return -1;
            } else if ((int) int2 < (int) int1) {
                return 1;
            }
            return 0;
        });
        appleList.addAll(trie.getAllSorted("apple87", (int1, int2) -> {
            if ((int) int1 < (int) int2) {
                return -1;
            } else if ((int) int2 < (int) int1) {
                return 1;
            }
            return 0;
        }));
        assertEquals(5, appleList.size());
        List<Integer> testSet = List.copyOf(appleList);
        Set<Integer> deleteSet = trie.deleteAllWithPrefix("app");
        assertEquals(5, deleteSet.size());
        assertEquals(deleteSet.size(), testSet.size());
        if (!deleteSet.containsAll(testSet)) {
            fail();
        }
        System.out.println("you passed complicatedTrieTest, congratulations!!!");
    }

    // variables to hold possible values for doc1
    private URI uri1;
    private String txt1;

    // variables to hold possible values for doc2
    private URI uri2;
    String txt2;

    private URI uri3;
    String txt3;

    @Test
    public void complicatedDocumentStoreTest() throws IOException, URISyntaxException {
        // init possible values for doc1
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "Apple Apple AppleProducts applesAreGood Apps APCalculus Apricots";

        // init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        this.txt2 = "Apple Apple Apple Apple Apple";

        // init possible values for doc3
        this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        this.txt3 = "APenguin APark APiccalo APants APain APossum";
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), this.uri1, DocumentStore.DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()), this.uri2, DocumentStore.DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream(this.txt3.getBytes()), this.uri3, DocumentStore.DocumentFormat.TXT);
        List<Document> appleList = new ArrayList<>();
        appleList.addAll(store.searchByPrefix("ap"));
        assertEquals(3, appleList.size());
        List<URI> testSet = new ArrayList<>();
        for (Document doc : appleList) {
            testSet.add(doc.getKey());
        }
        Set<URI> deleteSet = store.deleteAllWithPrefix("ap");
        assertEquals(3, deleteSet.size());
        assertEquals(deleteSet.size(), testSet.size());
        if (!deleteSet.containsAll(testSet)) {
            fail();
        }
        System.out.println("you passed complicatedDocumentStoreTest, congratulations!!!");
    }

    @Test
    public void reallyComplicatedDocumentStoreUndoTest() throws IOException, URISyntaxException {
        // init possible values for doc1
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "Apple Apple AppleProducts applesAreGood Apps APCalculus Apricots";

        // init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        this.txt2 = "Apple Apple Apple Apple Apple";

        // init possible values for doc3
        this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        this.txt3 = "APenguin APark APiccalo APants APain APossum";
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), this.uri1, DocumentStore.DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()), this.uri2, DocumentStore.DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream(this.txt3.getBytes()), this.uri3, DocumentStore.DocumentFormat.TXT);
        List<Document> appleList = new ArrayList<>();
        appleList.addAll(store.searchByPrefix("ap"));
        assertEquals(3, appleList.size());
        store.undo(this.uri2);
        appleList = store.searchByPrefix("ap");
        assertEquals(2, appleList.size());
        List<URI> testSet = new ArrayList<>();
        for (Document doc : appleList) {
            testSet.add(doc.getKey());
        }
        store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()), this.uri2, DocumentStore.DocumentFormat.TXT);
        appleList = store.searchByPrefix("ap");
        assertEquals(3, appleList.size());
        Set<URI> deleteSet = store.deleteAllWithPrefix("ap");
        assertEquals(3, deleteSet.size());
        store.undo(this.uri1);
        store.undo(this.uri3);
        assertEquals(2, store.searchByPrefix("ap").size());
        deleteSet = store.deleteAllWithPrefix("ap");
        assertEquals(2, deleteSet.size());
        assertEquals(deleteSet.size(), testSet.size());
        if (!deleteSet.containsAll(testSet)) {
            fail();
        }
        System.out.println("you passed reallyComplicatedDocumentStoreUndoTest, congratulations!!!");
    }

    @Test
    public void testCommandSetRemovedIfEmpty() throws IOException, URISyntaxException {
        // init possible values for doc1
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "Apple Apple AppleProducts applesAreGood Apps APCalculus Apricots";

        // init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        this.txt2 = "Apple Apple Apple Apple Apple";

        // init possible values for doc3
        this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        this.txt3 = "APenguin APark APiccalo APants APain APossum";
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), this.uri1, DocumentStore.DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()), this.uri2, DocumentStore.DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream(this.txt3.getBytes()), this.uri3, DocumentStore.DocumentFormat.TXT);
        List<Document> appleList = new ArrayList<>();
        appleList.addAll(store.searchByPrefix("ap"));
        assertEquals(3, appleList.size());
        store.deleteAllWithPrefix("ap");
        store.undo(this.uri1);
        store.undo(this.uri2);
        store.undo(this.uri3);
        store.undo();
        assertNotEquals(null, store.getDocument(this.uri2));
        assertNotEquals(null, store.getDocument(this.uri1));
        assertNull(store.getDocument(this.uri3));
        System.out.println("you passed testCommandSetRemovedIfEmpty, congratulations!!!");
    }

}
