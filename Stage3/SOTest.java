package edu.yu.cs.com1320.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore;
import edu.yu.cs.com1320.project.stage3.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage3.impl.DocumentStoreImpl;


public class SOTest {
    URI uri1;
    URI uri2;
    URI uri3;
    String txt1;
    String txt2;
    String txt3;
    @Test
    public void testOrder() throws IOException, URISyntaxException {
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "Apple Apple AppleProducts applesAreGood Apps APCalculus Apricots";

        // init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        this.txt2 = "Apple Apple Apple Apple Apple";

        // init possible values for doc3
        this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        this.txt3 = "APenguin APark APiccalo APants APain APossum";

        URI uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        String txt4 = "ap APPLE apartment";
        DocumentStoreImpl store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), this.uri1, DocumentStore.DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()), this.uri2, DocumentStore.DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream(this.txt3.getBytes()), this.uri3, DocumentStore.DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream(txt4.getBytes()), uri4, DocumentStore.DocumentFormat.TXT);
        List<Document> wordList = store.search("apple");
        List<Document> prefixList = store.searchByPrefix("ap");
        assertEquals(wordList.size(), 3);
        assertEquals(wordList.get(0).getKey(), uri2);
        assertEquals(wordList.get(1).getKey(), uri1);
        assertEquals(wordList.get(2).getKey(), uri4);

        assertEquals(prefixList.size(), 4);
        assertEquals(prefixList.get(0).getKey(), uri1);
        assertEquals(prefixList.get(1).getKey(), uri3);
        assertEquals(prefixList.get(2).getKey(), uri2);

    }
}
