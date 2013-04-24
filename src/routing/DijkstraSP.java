package routing;

import java.util.Stack;
import mapDrawer.dataSupplying.DataHolding;
import mapDrawer.drawing.Edge;

public class DijkstraSP
{
	private int[] edgeTo; //contains edgeIDs on the edge towards the node i on edgeTo[i].
	private double[] distTo;
	private IndexMinPQ<Double> pq;
	
	public DijkstraSP(EdgeWeightedDigraph graph, String roadName) {
		int s = 0;
		for(int i = 1; i < DataHolding.getEdgeArray().length; i++) 
		{
			if (DataHolding.getEdge(i).getRoadName().equals(roadName)) 
			{
				s = DataHolding.getEdge(i).getToNode();
			}
		}
		edgeTo = new int[graph.nodes()];
		distTo = new double[graph.nodes()];
		pq = new IndexMinPQ<Double>(graph.nodes());
		
		for (int v = 0; v < graph.nodes(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s] = 0.0;

		pq.insert(s, 0.0);
		while (!pq.isEmpty())
			relax(graph, pq.delMin());
	}

	private void relax(EdgeWeightedDigraph graph, int v) {
		Edge currentEdge;
		for(Integer e : graph.adj(v)) {
			currentEdge = DataHolding.getEdge(e);
			int w = currentEdge.getToNode();
			if (distTo[w] > distTo[v] + currentEdge.getDriveTime()) {
				distTo[w] = distTo[v] + currentEdge.getDriveTime();
				edgeTo[w] = e;
				if (pq.contains(w)) pq.changeKey(w, distTo[w]);
				else pq.insert(w, distTo[w]);
			}
		}
	}

	public double distTo(int v) { 
		return distTo[v]; 
	}

	public boolean hasPathTo(int v) { 
		return distTo[v] < Double.POSITIVE_INFINITY; 
	}

	public Iterable<Edge> pathTo(String roadName) {
		int v = 0;
		for(int i = 1; i < DataHolding.getEdgeArray().length; i++) 
		{
			if (DataHolding.getEdge(i).getRoadName().equals(roadName)) 
			{
				v = DataHolding.getEdge(i).getiD();
				System.out.println(v);
			}
		}
		if (!hasPathTo(v)) 
			return null;
		Stack<Edge> path = new Stack<Edge>();
		for (Edge e = DataHolding.getEdge(edgeTo[v]); e != null; e = DataHolding.getEdge(e.getFromNode()))
			path.push(e);
		return path;
	}
}