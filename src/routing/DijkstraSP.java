package routing;

import java.util.Stack;

import mapDrawer.dataSupplying.DataHolding;
import mapDrawer.drawing.Edge;

public class DijkstraSP
{
	private int[] edgeTo; //contains edgeIDs on the edge towards the node i on edgeTo[i].
	private double[] distTo;
	private IndexMinPQ<Double> pq;
	int s, counter = 0;
	
	public DijkstraSP(EdgeWeightedDigraph graph, String roadName) {
		s = -1;
		for(int i = 1; i < DataHolding.getEdgeArray().length; i++) 
		{
			if (DataHolding.getEdge(i).getRoadName().equals(roadName)) 
			{
				s = DataHolding.getEdge(i).getToNode();
				break;
			}
		}
		edgeTo = new int[graph.nodes()];
		distTo = new double[graph.nodes()];
		pq = new IndexMinPQ<Double>(graph.nodes());
		
		for (int v = 0; v < graph.nodes(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s] = 0.0;

		pq.insert(s, 0.0);
		System.out.println("Starting relaxation");
		while (!pq.isEmpty())
			relax(graph, pq.delMin());
	}

	private void relax(EdgeWeightedDigraph graph, int n) {
		Edge currentEdge;
		for(Integer e : graph.adj(n)) 
		{
			currentEdge = DataHolding.getEdge(e);
			int w = currentEdge.getToNode()-1;
			if(w == n)
				w = currentEdge.getFromNode()-1;
			if (distTo[w] > distTo[n] + currentEdge.getDriveTime()) 
			{
				distTo[w] = distTo[n] + currentEdge.getDriveTime();
				edgeTo[w] = e;
				if (pq.contains(w)) pq.changeKey(w, distTo[w]);
				else pq.insert(w, distTo[w]);
			}
		}
	}

	public double distTo(int n) { 
		return distTo[n]; 
	}

	public boolean hasPathTo(int n) { 
		return distTo[n] < Double.POSITIVE_INFINITY; 
	}

	public Iterable<Edge> pathTo(String roadName) {
		int n = -1; 
		for(int i = 1; i < DataHolding.getEdgeArray().length; i++) 
		{
			if (DataHolding.getEdge(i).getRoadName().equals(roadName)) 
			{
				n = DataHolding.getEdge(i).getToNode()-1;
				break;
			}
		}
		if (!hasPathTo(n)) 
			return null;
		System.out.println("finding route");
		Stack<Edge> path = new Stack<Edge>();
		int newNode = -1;
		for (Edge e = DataHolding.getEdge(edgeTo[n]); e != null; e = DataHolding.getEdge(newNode)) {
			if (n+1 == e.getFromNode())
				newNode = e.getToNode();
			else {
				newNode = e.getFromNode();
			}
			if (e.getFromNode() == s) 
			{
				path.push(e);
				System.out.println("Succes!");
				break;
			}
			System.out.println(e.getiD());
			path.push(e);
		}
		return path;
	}
}