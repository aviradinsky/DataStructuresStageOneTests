package edu.yu.cs.com1320.project;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
public class RadinskyTest {
    @Test
    void ArglessConstructorTest(){
        try {
            new TrieImpl<>();
        } catch (RuntimeException e) {
            fail();
        }
    }
    @Test
    void TestGoneAfterRemoveAllWithPrefixAndSearch() throws IOException,URISyntaxException{
        DocumentStore store = new DocumentStoreImpl();
        URI one = new URI("one");
        URI two = new URI("two");
        URI three = new URI("three");
        URI four = new URI("four");
        store.putDocument(new ByteArrayInputStream("the the the".getBytes()),one,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("the".getBytes()),two,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("thei".getBytes()),three,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("this is number FOUR".getBytes()),four,DocumentFormat.TXT);
        assertEquals(store.search("the").size(),2);
        assertEquals(store.search("is").size(), 1);
        store.deleteAll("four");
        assertEquals(store.search("is").size(), 0);
        store.deleteAll("the");
        assertEquals(store.search("the").size(), 0);
        store.undo(four);
        assertEquals(store.search("is").size(), 1);
        store.undo(one);
        assertEquals(store.search("the").size(), 1);
    }
    @Test
    void TestUndoAfterDeleteAllWithPrefix() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI one = new URI("one");
        URI two = new URI("two");
        URI three = new URI("three");
        URI four = new URI("four");
        store.putDocument(new ByteArrayInputStream("the the the".getBytes()),one,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("the".getBytes()),two,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("thei".getBytes()),three,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("this is number FOUR".getBytes()),four,DocumentFormat.TXT);
        assertEquals(store.searchByPrefix("the").size(), 3);
        assertEquals(store.deleteAllWithPrefix("the").size(), 3);
        assertEquals(store.searchByPrefix("the").size(), 0);
        store.undo(); 
        assertEquals(store.searchByPrefix("the").size(), 3);
    }
    @Test
    void TestUndoAfterDeleteAll() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI one = new URI("one");
        URI two = new URI("two");
        URI three = new URI("three");
        URI four = new URI("four");
        store.putDocument(new ByteArrayInputStream("the the the".getBytes()),one,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("the".getBytes()),two,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("thei".getBytes()),three,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("this is number FOUR".getBytes()),four,DocumentFormat.TXT);
        assertEquals(store.search("the").size(), 2);
        assertEquals(store.deleteAll("the").size(), 2);
        assertEquals(store.search("the").size(), 0);
        store.undo(); 
        assertEquals(store.search("the").size(), 2);

    }
    @Test
    void TestGoneAfterRemoveAllWithPrefix() throws IOException,URISyntaxException{
        DocumentStore store = new DocumentStoreImpl();
        URI one = new URI("one");
        URI two = new URI("two");
        URI three = new URI("three");
        URI four = new URI("four");
        store.putDocument(new ByteArrayInputStream("the the the".getBytes()),one,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("the".getBytes()),two,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("thei".getBytes()),three,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("this is number FOUR".getBytes()),four,DocumentFormat.TXT);
        store.deleteAllWithPrefix("the");
        assertNull(store.getDocument(one));
        assertNull(store.getDocument(two));
        assertNull(store.getDocument(three));
        assertEquals(store.getDocument(four).getKey(), four);
    }
    @Test
    void TestGoneAfterRemoveAll() throws IOException,URISyntaxException{
        DocumentStore store = new DocumentStoreImpl();
        URI one = new URI("one");
        URI two = new URI("two");
        URI three = new URI("three");
        URI four = new URI("four");
        store.putDocument(new ByteArrayInputStream("the the the".getBytes()),one,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("the".getBytes()),two,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("the i".getBytes()),three,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("this is number FOUR".getBytes()),four,DocumentFormat.TXT);
        store.deleteAll("the");
        assertNull(store.getDocument(one));
        assertNull(store.getDocument(two));
        assertNull(store.getDocument(three));
        assertNotNull(store.getDocument(four));
    }


    @Test
    void DocumentStoreImplTestUndoWithURI() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI one = new URI("one");
        URI two = new URI("two");
        URI three = new URI("three");
        store.putDocument(new ByteArrayInputStream("the the the".getBytes()),one,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("the".getBytes()),two,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("thei".getBytes()),three,DocumentFormat.TXT);
        assertTrue(store.searchByPrefix("th").size() == 3);
        store.deleteDocument(one);
        assertTrue(store.searchByPrefix("th").size() == 2);
        store.deleteDocument(two);
        assertTrue(store.searchByPrefix("th").size() == 1);
        store.undo(one);
        assertNotEquals(store.getDocument(one), null);
        assertEquals(store.getDocument(two), null);
        assertTrue(store.searchByPrefix("th").size() == 2);
    }
    @Test
    void DocumentStoreImplTestUndo() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI one = new URI("one");
        URI two = new URI("two");
        store.putDocument(new ByteArrayInputStream("the the the".getBytes()),one,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("the".getBytes()),two,DocumentFormat.TXT);
        assertTrue(store.searchByPrefix("th").size() == 2);
        store.deleteDocument(one);
        assertTrue(store.searchByPrefix("th").size() == 1);
        store.undo();
        assertNotEquals(store.getDocument(one), null);
        assertTrue(store.searchByPrefix("th").size() == 2);
    }
    @Test
    void DocumentStoreImplTestSearch() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI one = new URI("one");
        URI two = new URI("two");
        store.putDocument(new ByteArrayInputStream("the the the".getBytes()),one,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("the".getBytes()),two,DocumentFormat.TXT);
        int n = 0;
        for(Document d : store.search("the")){
            n++;
        }
        assertTrue(n == 2);
    }
    @Test
    void DocumentStoreImplTestSearchPrefix() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI one = new URI("one");
        URI two = new URI("two");
        URI three= new URI("three");
        store.putDocument(new ByteArrayInputStream("the the the".getBytes()),one,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("the the".getBytes()),two,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("the".getBytes()),three,DocumentFormat.TXT);
        for(Document d : store.searchByPrefix("t")){
            //System.out.println(d.getKey());
        }
    }
    @Test
    void DocumentStoreImplTestDeleteAll() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI one = new URI("one");
        URI two = new URI("two");
        URI three= new URI("three");
        store.putDocument(new ByteArrayInputStream("the the the".getBytes()),one,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("there".getBytes()),two,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("the".getBytes()),three,DocumentFormat.TXT);
        for(Document d : store.searchByPrefix("the")){
            //System.out.println(d.getKey());
        }
        store.deleteAll("the");
        //System.out.println("Break");
        for(Document d : store.searchByPrefix("the")){
            //System.out.println(d.getKey());
        }
    }
    @Test
    void DocumentStoreImplTestDeleteAllWithPrefix() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI one = new URI("one");
        URI two = new URI("two");
        URI three= new URI("three");
        store.putDocument(new ByteArrayInputStream("the the the".getBytes()),one,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("there".getBytes()),two,DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream("the".getBytes()),three,DocumentFormat.TXT);
        for(Document d : store.searchByPrefix("the")){
            //System.out.println(d.getKey());
        }
        store.deleteAllWithPrefix("thereh");
        //System.out.println("Break");
        for(Document d : store.searchByPrefix("the")){
            //System.out.println(d.getKey());
        }
    }
    @Test
    void TriePutTest(){
        Trie<Integer> trie = new TrieImpl<>();
        trie.put("the", 29);
        trie.put("the", 30);
        trie.put("the", 31);
        trie.put("the", 32);
        trie.put("the", 33);
        int i = 0;
        for(Integer d : trie.getAllSorted("the", Comparator.naturalOrder())){
            //System.out.println(d);
            i++;
        }
        assertTrue(i == 5);
    }    
}