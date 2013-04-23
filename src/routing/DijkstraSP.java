package routing;

import java.util.HashMap;
import java.util.Stack;

import mapDrawer.dataSupplying.DataHolding;
import mapDrawer.drawing.Edge;

public class DijkstraSP
{
	private int[] edgeTo;
	private double[] distTo;
	private IndexMinPQ<Double> pq;
	
	public DijkstraSP(EdgeWeightedDigraph graph, int s)
	{
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
			
			//TODO Access EdgeMap and get the edge with edgeID e
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

	public Iterable<DirectedEdge> pathTo(int v) {
		if (!hasPathTo(v)) 
			return null;
		Stack<DirectedEdge> path = new Stack<DirectedEdge>();
		for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
			path.push(e);
		return path;
	}
}