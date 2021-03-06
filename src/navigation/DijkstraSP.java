package navigation;

import inputHandler.Zoidberg;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Stack;

import mapCreationAndFunctions.data.Edge;
import navigation.data.SWPriorityQueue;
import navigation.exceptions.NoRoutePossibleException;

/**
 * DijsktraSP finds the best route between two points with certain parameters as weights for how a road is deemed the best.
 */
public class DijkstraSP
{
	private int[] edgeTo; //contains edgeIDs on the edge towards the node i on edgeTo[i].
	private double[] distTo;
	private SWPriorityQueue<Double> pq;
	private int s;
	private String routeType = "Fastest", meansOfTransportation;
	private HashSet<Integer> setOfNonViableEdges;
	private HashSet<String> setOfNonViableRoadTypes;
	private EdgeWeightedDigraph graph;
	private Edge[] edgeArray;
	private boolean badInput = false;

	public DijkstraSP(EdgeWeightedDigraph graph, Edge fromEdge, Edge[] edgeArray, String meansOfTransportation, String routeType) {
		TransportType(meansOfTransportation);

		this.edgeArray = edgeArray;
		this.meansOfTransportation = meansOfTransportation;
		this.routeType = routeType;
		if(setOfNonViableEdges == null) 
			throw new NullPointerException("setOfNonViableEdges is empty");
		this.graph = graph;
		s = -1;
		s = fromEdge.getToNode()-1;

		if(s != -1) 
		{
			edgeTo = new int[graph.nodes()];
			distTo = new double[graph.nodes()];
			pq = new SWPriorityQueue<Double>(graph.nodes());

			for (int n = 0; n < graph.nodes(); n++)
				distTo[n] = Double.POSITIVE_INFINITY;
			distTo[s] = 0.0;

			pq.insert(s, 0.0);
			while (!pq.isEmpty())
				prepareForRelax(pq.delMin());
		}
		else 
		{
			badInput = true;
			Zoidberg.badInputMessages();
		}
	}

	/**
	 * This method makes sure that we do not add an edge to the graph that we cant go by. For instance one direction roads as a car.
	 * @param n is the node id-1.
	 */
	private void prepareForRelax(int n) {
		Edge currentEdge;
		for(Integer e : graph.adj(n)) 
		{
			currentEdge = edgeArray[e-1];
			if(!setOfNonViableEdges.contains(currentEdge.getRoadType())) {
				if(currentEdge.getRoadType() == 1)
					relax(n, currentEdge);
				else if(!setOfNonViableRoadTypes.contains(currentEdge.getOneWay()))
					relax(n, currentEdge);
			}
		}
	}

	/**
	 * Checks if the route that this edge is a part to the given end node is the fastest that has been found so far.
	 * If so, this is saved.
	 * @param n is the node id-1
	 * @param currentEdge the edge currently looked at.
	 */
	private void relax(int n, Edge currentEdge) {
		int w = -1;
		w = currentEdge.getToNode()-1;
		if(w == n)
			w = currentEdge.getFromNode()-1;
		if (routeType.trim().equals("Fastest") && meansOfTransportation.equals("Car")) 
		{
			if (distTo[w] > distTo[n] + currentEdge.getDriveTime()) 
			{
				distTo[w] = distTo[n] + currentEdge.getDriveTime();
				edgeTo[w] = currentEdge.getiD();
				if (pq.contains(w)) pq.changeKey(w, distTo[w]);
				else pq.insert(w, distTo[w]);
			}
		}
		else
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

	/**
	 * Checks if there's a route to the given end node. If there is, the Edges in the route is pushed to a Stack.
	 * @param toEdge is the end node.
	 * @throws NoRoutePossibleException Is thrown if no possible route could be found
	 * @returns the stack with the edges for the route or returns null.
	 */
	public Iterable<Edge> pathTo(Edge toEdge) throws NoRoutePossibleException {
		if(badInput == false) 
		{
			int n = -1; 
			n = toEdge.getToNode()-1;
			
			if(n != -1) 
			{
				if (!hasPathTo(n)) { 
					throw new NoRoutePossibleException("No route is possible between these roads");
				}
				Stack<Edge> path = new Stack<Edge>();
				for (Edge e = edgeArray[edgeTo[n]-1]; e != null; e = edgeArray[edgeTo[n]-1]) {
					if (n+1 == e.getFromNode())
						n = e.getToNode()-1;
					else {
						n = e.getFromNode()-1;
					}
					if (n == s) 
					{
						path.push(e);
						break;
					}
					path.push(e);
				}
				return path;
			}
			else {
				Zoidberg.badInputMessages();
				return null;
			}
		}
		return null;
	}

	/**
	 * Is used for limiting which road type a vehicle type can use, and also what kind of edges the vehicle type can use, for instance
	 * cars can't use one way roads going in the opposite way that the car is going.
	 * @param meansOfTransportation
	 */
	private void TransportType(String meansOfTransportation) {
		switch (meansOfTransportation) {
		case "Bike": setOfNonViableEdges = new HashSet<Integer>(Arrays.asList(new Integer[]{1,2,3,21,22,23,31,32,33,41,42,43}));
		setOfNonViableRoadTypes = new HashSet<String>(Arrays.asList(new String[]{"tf"})); break;

		case "Car": setOfNonViableEdges = new HashSet<Integer>(Arrays.asList(new Integer[]{8,10,11,28}));
		setOfNonViableRoadTypes = new HashSet<String>(Arrays.asList(new String[]{"tf", "n"})); break;

		case "Walk": setOfNonViableEdges = new HashSet<Integer>(Arrays.asList(new Integer[]{1,2,3,21,22,23,31,32,33,41,42,43})); 
		setOfNonViableRoadTypes = new HashSet<String>();
		default:
			break;
		}
	}

	/**
	 * The distance to the node n from the source node.
	 * @param n is the end node.
	 * @returns the distance.
	 */
	public double distTo(int n) { 
		return distTo[n]; 
	}

	/**
	 * Checks if there is a route the node n.
	 * @param n is the node.
	 * @returns either true of false.
	 */
	private boolean hasPathTo(int n) { 
		return distTo[n] < Double.POSITIVE_INFINITY; 
	}

	/**
	 * Is used to trigger error messages if either start or end input is wrong.
	 * @return true or false.
	 */
	public boolean isBadInput() {
		return badInput;
	}
}