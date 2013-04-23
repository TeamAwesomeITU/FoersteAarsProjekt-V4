package routing;

import java.util.ArrayList;
import java.util.HashMap;

public class FastestPath {
	private int nodeCounter = 0;
	//private ArrayList<Integer> nodesInGraph = new ArrayList<Integer>();
	private HashMap<Integer, Integer> nodesHashMap;
	private EdgeWeightedDigraph graph;
	
	public FastestPath() {
		int nodeID = 0;
		nodesHashMap.put(nodeID, nodeCounter);
		nodeCounter++;
		graph = new EdgeWeightedDigraph(nodeCounter);
	}
	public static void main(String[] args) {

		//EdgeWeightedDigraph graph = new EdgeWeightedDigraph(roadsInGraph.size());
	}
}