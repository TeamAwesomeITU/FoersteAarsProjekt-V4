package mapCreationAndFunctions.data.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Copied from the book "Algorithms - Fourth Edition" by Robert Sedgewick and Kevin Wayne, 2011
 * Modified to contain an HashSet<Integer>, instead of a single value.
 */
public class TernarySearchTrieCity
{
	private int N;       // size
	private TrieNode root;   // root of TST

	private class TrieNode
	{
		private char c;                 // character
		private TrieNode left, mid, right;  // left, middle, and right subtries
		private HashSet<Integer> set;              // HashSet<Integer> associated with string
	}

	public int size()
	{ return N;	}

	public boolean contains(String key)
	{ return get(key) != null; }

	public HashSet<Integer> get(String key) {
		if (key == null) throw new NullPointerException();
		if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
		TrieNode x = get(root, key, 0);
		if (x == null) return null;
		return x.set;
	}

	private TrieNode get(TrieNode x, String key, int d) {
		if (key == null) throw new NullPointerException();
		if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
		if (x == null) return null;
		char c = key.charAt(d);
		if      (c < x.c)              return get(x.left,  key, d);
		else if (c > x.c)              return get(x.right, key, d);
		else if (d < key.length() - 1) return get(x.mid,   key, d+1);
		else                           return x;
	}

	public void put(String s, HashSet<Integer> set) {
		if (!contains(s)) N++;
		root = put(root, s, set, 0);
	}

	private TrieNode put(TrieNode x, String s, HashSet<Integer> set, int d) {
		char c = s.charAt(d);
		if (x == null) {
			x = new TrieNode();
			x.c = c;
		}
		if      (c < x.c)             x.left  = put(x.left,  s, set, d);
		else if (c > x.c)             x.right = put(x.right, s, set, d);
		else if (d < s.length() - 1)  x.mid   = put(x.mid,   s, set, d+1);
		else                          x.set = set;
		return x;
	}

	public String longestPrefixOf(String s) {
		if (s == null || s.length() == 0) return null;
		int length = 0;
		TrieNode x = root;
		int i = 0;
		while (x != null && i < s.length()) {
			char c = s.charAt(i);
			if      (c < x.c) x = x.left;
			else if (c > x.c) x = x.right;
			else {
				i++;
				if (x.set != null) length = i;
				x = x.mid;
			}
		}
		return s.substring(0, length);
	}

	public Iterable<String> keys() {
		Queue<String> queue = new Queue<String>();
		collect(root, "", queue);
		return queue;
	}

	public Iterable<String> prefixMatch(String prefix) {
		Queue<String> queue = new Queue<String>();
		TrieNode x = get(root, prefix, 0);
		if (x == null) return queue;
		if (x.set != null) queue.enqueue(prefix);
		collect(x.mid, prefix, queue);
		return queue;
	}

	private void collect(TrieNode x, String prefix, Queue<String> queue) {
		if (x == null) return;
		collect(x.left,  prefix,       queue);
		if (x.set != null) queue.enqueue(prefix + x.c);
		collect(x.mid,   prefix + x.c, queue);
		collect(x.right, prefix,       queue);
	}

	public Iterable<String> wildcardMatch(String pat) {
		Queue<String> queue = new Queue<String>();
		collect(root, "", 0, pat, queue);
		return queue;
	}

	public void collect(TrieNode x, String prefix, int i, String pat, Queue<String> q) {
		if (x == null) return;
		char c = pat.charAt(i);
		if (c == '.' || c < x.c) collect(x.left, prefix, i, pat, q);
		if (c == '.' || c == x.c) {
			if (i == pat.length() - 1 && x.set != null) q.enqueue(prefix + x.c);
			if (i < pat.length() - 1) collect(x.mid, prefix + x.c, i+1, pat, q);
		}
		if (c == '.' || c > x.c) collect(x.right, prefix, i, pat, q);
	}
}