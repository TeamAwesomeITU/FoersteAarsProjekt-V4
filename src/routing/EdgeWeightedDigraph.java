package routing;

import mapDrawer.dataSupplying.DataHolding;
import mapDrawer.drawing.Edge;

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
		
		Edge edge;
		for(int i = 0; i < DataHolding.getEdgeArray().length; i++) 
		{
			edge = DataHolding.getEdgeArray()[i];
			
			//Both ways
			if(edge.getOneWay().equals("")) 
			{
				addEdge(edge.getFromNode(), edge.getiD());
				addEdge(edge.getToNode(), edge.getiD());
			}
			//From here to there
			else if(edge.getOneWay().equals("ft")) 
			{
				addEdge(edge.getFromNode(), edge.getiD());
			}
			
			//To here from there
			else if(edge.getOneWay().equals("tf")) 
			{
				addEdge(edge.getToNode(), edge.getiD());
			}
			
		}
	}

	public int nodes() { return nodes; }
	public int edges() { return edges; }

	public void addEdge(int nodeID, int edgeID) {
		adj[nodeID-1].add(edgeID);
		edges++;
	}

	public Iterable<Integer> adj(int n) { return adj[n-1]; }

	public Iterable<Integer> Edges() {
		Bag<Integer> bag = new Bag<Integer>();
		for (int n = 0; n < nodes; n++)
			for (int e : adj[n])
				bag.add(e);
		return bag;
	}
}
