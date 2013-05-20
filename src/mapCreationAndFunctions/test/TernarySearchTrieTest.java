package mapCreationAndFunctions.test;

import java.util.ArrayList;

import mapCreationAndFunctions.search.TernarySearchTrie;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TernarySearchTrieTest {
	TernarySearchTrie trie;
	@Before
	public void initialize() {
		trie = new TernarySearchTrie();
		trie.put("Følfodvej", 1045);
		trie.put("Amagerbrogade", 10);
		trie.put("Amager Landevej", 12);
		trie.put("Ved Mønten", 42);
	}
	
	@Test
	public void testingTrieGet() {
		assertTrue(trie.get("Følfodvej").get(0) == 1045);
	}
	
	@Test
	public void testingTrieSize() {
		assertTrue(trie.size() == 4);
	}
	
	@Test
	public void testingTrieContains() {
		assertTrue(trie.contains("Ved Mønten") == true);
	}
	
	@Test
	public void testingTrieLongestPrefix() {
		String string = "AmagerG";
		assertEquals("amager", trie.longestPrefixOf(string));
	}
	
	@Test
	public void testingTriePrefixMatch() {
		String s = "AmagerG";
		ArrayList<String> matches = trie.prefixMatch(s);
		for(String match: matches) {
			assertTrue(match.equals("amagerbrogade") || match.equals("amager landevej"));
		}
	}
	
	@After
	public void tearDown() {
		trie = null;
	}
}
