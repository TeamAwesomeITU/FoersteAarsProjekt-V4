package mapCreationAndFunctions.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import mapCreationAndFunctions.data.TernarySearchTrie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the TernarySearchTrie
 */
public class TernarySearchTrieTest {
	TernarySearchTrie trie;
	/**
	 * Initializes the trie before each test.
	 */
	@Before
	public void initialize() {
		trie = new TernarySearchTrie();
		trie.put("Følfodvej", 1045);
		trie.put("Amagerbrogade", 10);
		trie.put("Amager Landevej", 12);
		trie.put("Ved Mønten", 42);
	}
	
	/**
	 * Tests the trie's get method.
	 */
	@Test
	public void testingTrieGet() {
		assertTrue(trie.get("Følfodvej").get(0) == 1045);
	}
	
	/**
	 * Tests the trie's size method.
	 */
	@Test
	public void testingTrieSize() {
		assertTrue(trie.size() == 4);
	}
	
	@Test
	public void testingTrieContains() {
		assertTrue(trie.contains("Ved Mønten") == true);
	}
	
	/**
	 * Tests the trie's longestPrefix method.
	 */
	@Test
	public void testingTrieLongestPrefix() {
		String string = "AmagerG";
		assertEquals("amager", trie.longestPrefixOf(string));
	}
	
	/**
	 * Tests the trie's prefixMatch method.
	 */
	@Test
	public void testingTriePrefixMatch() {
		String s = "AmagerG";
		ArrayList<String> matches = trie.prefixMatch(s);
		for(String match: matches) {
			assertTrue(match.equals("amagerbrogade") || match.equals("amager landevej"));
		}
	}
	
	/**
	 * Tears down the trie after each test.
	 */
	@After
	public void tearDown() {
		trie = null;
	}
}
