package routing;

public class EdgeWeightedDigraph {
	private final int nodes; // number of nodes
	private int edges; // number of edges
	private Bag<Integer>[] adj; // adjacency lists

	@SuppressWarnings("unchecked")
	public EdgeWeightedDigraph(int N) {
		nodes = N;
		edges = 0;
		adj = (Bag<Integer>[]) new Bag[N];
		for (int n = 0; n < N; n++)
			adj[n] = new Bag<Integer>();
	}

	
	public int nodes() { return nodes; }
	public int edges() { return edges; }

	public void addEdge(int nodeID, int edgeID) {
		adj[nodeID-1].add(edgeID);
		edges++;
	}

	public Iterable<Integer> adj(int n) { return adj[n]; }

	public Iterable<Integer> directedEdges() {
		Bag<Integer> bag = new Bag<Integer>();
		for (int n = 0; n < nodes; n++)
			for (int e : adj[n])
				bag.add(e);
		return bag;
	}
}
