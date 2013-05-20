package mapCreationAndFunctions.search;

import java.util.ArrayList;

/**
 * Copied from the book "Algorithms - Fourth Edition" by Robert Sedgewick and Kevin Wayne, 2011
 * Contains multiple modifications, for example it now contains an ArrayList<Integer>, instead of a single value.
 */
public class TernarySearchTrie
{
	private int N;       // size
	private TrieNode root;   // root of TST

	private class TrieNode
	{
		private char character;                 // character
		private TrieNode left, mid, right;  // left, middle, and right subtries
		private ArrayList<Integer> val = new ArrayList<>();              // ArrayList<Integer> associated with string
	}

	public int size()
	{ return N;	}

	public boolean contains(String key) {
	key = key.toLowerCase();
	return get(key) != null; }

	public ArrayList<Integer> get(String key) {
		key = key.toLowerCase();
		if (key == null) throw new NullPointerException();
		if (key.trim().isEmpty()) throw new IllegalArgumentException("Key cannot be empty");
		TrieNode currentNode = get(root, key, 0);
		if (currentNode == null) return null;
		return currentNode.val;
	}

	private TrieNode get(TrieNode currentNode, String key, int d) {
		if (key == null) throw new NullPointerException();
		if (key.length() == 0) throw new IllegalArgumentException("Key must have length >= 1");
		if (currentNode == null) return null;
		char character = key.charAt(d);
		if (character < currentNode.character) return get(currentNode.left,  key, d);
		else if (character > currentNode.character) return get(currentNode.right, key, d);
		else if (d < key.length() - 1) return get(currentNode.mid,   key, d+1);
		else
			return currentNode;
	}

	public void put(String s, int val) {
		s = s.toLowerCase();
		if (!contains(s)) N++;
		root = put(root, s, val, 0);
	}

	private TrieNode put(TrieNode currentNode, String s, int val, int d) {
		char character = s.charAt(d);
		if (currentNode == null) {
			currentNode = new TrieNode();
			currentNode.character = character;
		}
		if      (character < currentNode.character)             currentNode.left  = put(currentNode.left,  s, val, d);
		else if (character > currentNode.character)             currentNode.right = put(currentNode.right, s, val, d);
		else if (d < s.length() - 1)  currentNode.mid   = put(currentNode.mid,   s, val, d+1);
		else                          currentNode.val.add(val);
		return currentNode;
	}

	public String longestPrefixOf(String s) {
		s = s.toLowerCase();
		if (s == null || s.length() == 0) return null;
		int length = 0;
		TrieNode currentNode = root;
		int i = 0;
		while (currentNode != null && i < s.length()) {
			char character = s.charAt(i);
			if      (character < currentNode.character) currentNode = currentNode.left;
			else if (character > currentNode.character) currentNode = currentNode.right;
			else {
				i++;
				if (currentNode.val != null) length = i;
				currentNode = currentNode.mid;
			}
		}
		return s.substring(0, length);
	}

	public ArrayList<String> keys() {
		ArrayList<String> list = new ArrayList<String>();
		collect(root, "", list);
		return list;
	}

	public ArrayList<String> prefixMatch(String prefix) {
		prefix = prefix.toLowerCase();
		ArrayList<String> list = new ArrayList<String>();
		TrieNode currentNode = get(root, prefix, 0);
		if (currentNode == null) return list;
		if (currentNode.val != null) list.add(prefix);
		collect(currentNode.mid, prefix, list);
		return list;
	}

	private void collect(TrieNode currentNode, String prefix, ArrayList<String> list) {
		if (currentNode == null) return;
		collect(currentNode.left,  prefix, list);
		if (currentNode.val != null) list.add(prefix + currentNode.character);
		collect(currentNode.mid, prefix + currentNode.character, list);
		collect(currentNode.right, prefix, list);
	}

	public void collect(TrieNode currentNode, String prefix, int i, String pattern, ArrayList<String> list) {
		if (currentNode == null) return;
		char character = pattern.charAt(i);
		if (character == '.' || character < currentNode.character) collect(currentNode.left, prefix, i, pattern, list);
		if (character == '.' || character == currentNode.character) {
			if (i == pattern.length() - 1 && currentNode.val != null) list.add(prefix + currentNode.character);
			if (i < pattern.length() - 1) collect(currentNode.mid, prefix + currentNode.character, i+1, pattern, list);
		}
		if (character == '.' || character > currentNode.character) collect(currentNode.right, prefix, i, pattern, list);
	}
}