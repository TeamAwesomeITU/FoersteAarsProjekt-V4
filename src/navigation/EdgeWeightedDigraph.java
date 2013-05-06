package navigation;

import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;

public class EdgeWeightedDigraph {
	private final int nodes; // number of nodes
	private int edges; // number of edges
	private Bag<Integer>[] adj; // adjacency lists

	/**
	 * Creates the directed graphs with edge weights.
	 * @param N Number of nodes/vertices
	 */
	@SuppressWarnings("unchecked")
	public EdgeWeightedDigraph(int N) {
		long s = System.currentTimeMillis();
		nodes = N;
		edges = 0;
		adj = (Bag<Integer>[]) new Bag[N];
		for (int n = 0; n < N; n++)
			adj[n] = new Bag<Integer>();
		Edge edge;
		for(int i = 0; i < DataHolding.getEdgeArray().length; i++) 
		{
			edge = DataHolding.getEdgeArray()[i];
			
			//Both ways
			if(edge.getOneWay().equals("") || edge.getOneWay().equals("n")) 
			{
				addEdge(edge.getFromNode(), edge.getiD());
				addEdge(edge.getToNode(), edge.getiD());}
			//From here to there
			else if(edge.getOneWay().equals("ft")) {
				addEdge(edge.getFromNode(), edge.getiD());}
			
			//From there to here
			else if(edge.getOneWay().equals("tf")) {
				addEdge(edge.getToNode(), edge.getiD());}
		}
		long t = System.currentTimeMillis();
		System.out.println("Creation adj " + (t-s) + " wtf?");
	}

	/**
	 * @return the nodes field
	 */
	public int nodes() { return nodes; }
	 /**
	  * @return the edges field
	  */
	public int edges() { return edges; }

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
