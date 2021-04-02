package edu.yu.cs.com1320.project.impl;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import edu.yu.cs.com1320.project.Trie;


public class TrieImplEdgeCaseTest {
    Trie<Integer> trie = new TrieImpl<>();
    String string1 = "It was a dark and stormy night";
    String string2 = "It was the best of times it was the worst of times";
    String string3 = "It was a bright cold day in April and the clocks were striking thirteen";
    String string4 = "I am free no matter what rules surround me";
    
    @BeforeEach
    public void init() {
        for (String word : string1.split(" ")) {
            trie.put(word, string1.indexOf(word));
        }
        for (String word : string2.split(" ")) {
            trie.put(word, string2.indexOf(word));
        }
        for (String word : string3.split(" ")) {
            trie.put(word, string3.indexOf(word));
        }
        for (String word : string4.split(" ")) {
            trie.put(word, string4.indexOf(word));
        }
    }
    @Test
    public void testPut() {
        assertThrows(IllegalArgumentException.class, () -> {
            trie.put(null, 100);
        });
        
        trie.put("", 100);
        
        trie.put("the", null);
        
        assertEquals(trie.getAllSorted("the", Comparator.naturalOrder()).size(), 2);
    }
    @Test
    public void testGetAllSorted() {
        assertThrows(IllegalArgumentException.class, () -> {
            trie.getAllSorted("the", null);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            trie.getAllSorted(null, Comparator.naturalOrder());
        });
        
        assertEquals(trie.getAllSorted("", Comparator.naturalOrder()).size(), 0);      
    }
    @Test
    public void testGetAllPrefixSorted() {
        assertThrows(IllegalArgumentException.class, () -> {
            trie.getAllWithPrefixSorted("the", null);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            trie.getAllWithPrefixSorted(null, Comparator.naturalOrder());
        });
        
        assertEquals(trie.getAllWithPrefixSorted("", Comparator.naturalOrder()).size(), 0);      
    }
    @Test
    public void testDeleteWithPrefix() {
        assertThrows(IllegalArgumentException.class, () -> {
            trie.deleteAllWithPrefix(null);
        });
        
        assertTrue(trie.deleteAllWithPrefix("").size()==0);
        
        assertEquals(trie.getAllWithPrefixSorted("the", Comparator.naturalOrder()).size(), 2);
    }
    @Test
    public void testDeleteAll() {
        assertThrows(IllegalArgumentException.class, () -> {
            trie.deleteAll(null);
        });
        
        assertTrue(trie.deleteAll("").size()==0);
        
        assertEquals(trie.getAllSorted("the", Comparator.naturalOrder()).size(), 2);
    }
    @Test
        public void testDelete() {
        assertThrows(IllegalArgumentException.class, () -> {
            trie.delete("the", null);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            trie.delete(null, 100);
        });
        
        assertTrue(trie.delete("", 0)==null);
        //Deliberately don't use Integer factory
        @SuppressWarnings("deprecation")
        Integer largeInteger = new Integer(1000);
        @SuppressWarnings("deprecation")
        Integer largeInteger2 = new Integer(1000);
        trie.put("largeInteger", largeInteger);
        assertTrue(largeInteger != largeInteger2);
        assertTrue(trie.delete("largeInteger", largeInteger2)==largeInteger);
    }
}
