package navigation;

import navigation.data.Bag;

public class EdgeWeightedDigraph {
	private final int nodes; // number of nodes
	private int edges; // number of edges
	private Bag<Integer>[] adj; // adjacency lists

	/**
	 * Creates the directed graphs with edge weights.
	 * @param N Number of nodes
	 */
	@SuppressWarnings("unchecked")
	public EdgeWeightedDigraph(int N) {
		nodes = N;
		edges = 0;
		adj = (Bag<Integer>[]) new Bag[N];
		for (int n = 0; n < N; n++)
			adj[n] = new Bag<Integer>();
	}

	/**
	 * @return the number of nodes
	 */
	public int nodes() { 
		return nodes; 
	}
	
	/**
	 * @return the number of edges
	 */
	public int edges() { 
		return edges; 
	}
	
	public Bag<Integer>[] getAdj() {
		return adj;
	}

	/**
	 * Adds edge references in the adjacency list at the index corresponding to the 
	 * node ID. 
	 * @param nodeID
	 * @param edgeID
	 */
	public void addEdge(int nodeID, int edgeID) {
		adj[nodeID-1].add(edgeID);
		edges++;
	}

	public Iterable<Integer> adj(int n) { return adj[n]; }

	public Iterable<Integer> Edges() {
		Bag<Integer> bag = new Bag<Integer>();
		for (int n = 0; n < nodes; n++)
			for (int e : adj[n])
				bag.add(e);
		return bag;
	}
}
