package navigation;

import inputHandler.AdressParser;
import inputHandler.exceptions.MalformedAdressException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Stack;

import navigation.data.IndexMinPQ;

import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;

public class DijkstraSP
{
	private int[] edgeTo; //contains edgeIDs on the edge towards the node i on edgeTo[i].
	private double[] distTo;
	private IndexMinPQ<Double> pq;
	private int from;
	private String routeType = "Fastest";
	private HashSet<Integer> setOfNonViableEdges;
	private HashSet<String> setOfNonViableRoadTypes;
	private EdgeWeightedDigraph graph;

	public DijkstraSP(EdgeWeightedDigraph graph, String roadName, String meansOfTransportation, String routeType) {
		TransportType(meansOfTransportation);

		this.routeType = routeType;
		if(setOfNonViableEdges == null) 
			throw new NullPointerException("setOfNonViableEdges is empty");
		this.graph = graph;

		from = compareAddress(roadName);
		if(from != -1) 
		{
			from = DataHolding.getEdge(from).getToNode()-1;

			edgeTo = new int[graph.nodes()];
			distTo = new double[graph.nodes()];
			pq = new IndexMinPQ<Double>(graph.nodes());

			for (int n = 0; n < graph.nodes(); n++)
				distTo[n] = Double.POSITIVE_INFINITY;
			distTo[from] = 0.0;

			pq.insert(from, 0.0);
			System.out.println("Starting relaxation");
			while (!pq.isEmpty())
				prepareForRelax(pq.delMin());
			System.out.println("Relaxation done");
		}
		
		else
			System.out.println("Du er en idiot, din from findes ikke.");
	}

	

	private void prepareForRelax(int n) {
		Edge currentEdge;
		for(Integer e : graph.adj(n)) 
		{
			currentEdge = DataHolding.getEdge(e);
			if(!setOfNonViableEdges.contains(currentEdge.getRoadType())) {
				if(currentEdge.getRoadType() == 1)
					relax(n, currentEdge);
				else if(!setOfNonViableRoadTypes.contains(currentEdge.getOneWay()))
					relax(n, currentEdge);
			}
		}
	}

	private void relax(int n, Edge currentEdge) {
		int w = -1;
		w = currentEdge.getToNode()-1;
		if(w == n)
			w = currentEdge.getFromNode()-1;
		if (routeType.trim().equals("Fastest")) 
		{
			if (distTo[w] > distTo[n] + currentEdge.getDriveTime()) 
			{
				distTo[w] = distTo[n] + currentEdge.getDriveTime();
				edgeTo[w] = currentEdge.getiD();
				if (pq.contains(w)) pq.changeKey(w, distTo[w]);
				else pq.insert(w, distTo[w]);
			}
		}
		else if(routeType.trim().equals("Shortest"))
		{
			if (distTo[w] > distTo[n] + currentEdge.getLength()) 
			{
				distTo[w] = distTo[n] + currentEdge.getLength();
				edgeTo[w] = currentEdge.getiD();
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
		int to = compareAddress(roadName); 
		if(to != -1) {
			to = DataHolding.getEdge(to).getToNode()-1;
		if (!hasPathTo(to)) { 
			System.out.println("nej");
			return null;
		}
		System.out.println("Finding route!");
		Stack<Edge> path = new Stack<Edge>();
		for (Edge e = DataHolding.getEdge(edgeTo[to]); e != null; e = DataHolding.getEdge(edgeTo[to])) {
			if (to+1 == e.getFromNode())
				to = e.getToNode()-1;
			else {
				to = e.getFromNode()-1;
			}
			if (to == from) 
			{
				path.push(e);
				System.out.println("Succes! Route found.");
				break;
			}
			path.push(e);
		}
		return path;
		
		} else
			return null;
	}

	private void TransportType(String meansOfTransportation) {
		switch (meansOfTransportation) {
		case "Bike": setOfNonViableEdges = new HashSet<Integer>(Arrays.asList(new Integer[]{1,2,21,22,23,3,31,32,33,41,42,43}));
		setOfNonViableRoadTypes = new HashSet<String>(Arrays.asList(new String[]{"tf"})); break;

		case "Car": setOfNonViableEdges = new HashSet<Integer>(Arrays.asList(new Integer[]{8,11,28}));
		setOfNonViableRoadTypes = new HashSet<String>(Arrays.asList(new String[]{"tf", "n"})); break;

		case "Walk": setOfNonViableEdges = new HashSet<Integer>(Arrays.asList(new Integer[]{1,2,3,4,21,22,23,24,31,32,33,34,41,42,43,44})); 
		setOfNonViableRoadTypes = new HashSet<String>();
		default:
			break;
		}
	}
}