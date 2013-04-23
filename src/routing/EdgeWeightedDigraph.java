package routing;

public class EdgeWeightedDigraph
{
	private final int N; // number of nodes
	private int E; // number of edges
	private Bag<DirectedEdge>[] adj; // adjacency lists

	@SuppressWarnings("unchecked")
	public EdgeWeightedDigraph(int N)
	{
		this.N = N;
		this.E = 0;
		adj = (Bag<DirectedEdge>[]) new Bag[N];
		for (int n = 0; n < N; n++)
			adj[n] = new Bag<DirectedEdge>();
	}

	
	public int N() { return N; }
	public int E() { return E; }

	public void addEdge(DirectedEdge e)
	{
		adj[e.from()].add(e);
		E++;
	}

	public Iterable<DirectedEdge> adj(int n) { return adj[n]; }

	public Iterable<DirectedEdge> directedEdges()
	{
		Bag<DirectedEdge> bag = new Bag<DirectedEdge>();
		for (int n = 0; n < N; n++)
			for (DirectedEdge e : adj[n])
				bag.add(e);
		return bag;
	}
}
