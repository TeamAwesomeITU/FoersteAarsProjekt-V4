package navigation.test;

import static org.junit.Assert.*;

import java.util.Stack;

import mapCreationAndFunctions.data.Edge;
import navigation.DijkstraSP;
import navigation.EdgeWeightedDigraph;
import navigation.exceptions.NoRoutePossibleException;

import org.junit.Test;

public class DijsktraSPTest {

	@Test
	public void testShortestPath() throws NoRoutePossibleException {
		EdgeWeightedDigraph graph = new EdgeWeightedDigraph(6);
		Edge[] testEdges = new Edge[6];
		Stack<Edge> path = new Stack<Edge>();
		Stack<Edge> pathToMatch = new Stack<Edge>();
	
		testEdges[0] = new Edge(1, 2, 1, 1, 1, "Testvej", 0, 0, 0, 0, "","","","", 0, 0, 0, 1, "", 0, 0, 0);
		testEdges[1] = new Edge(2, 3, 1, 2, 1, "Testvej", 0, 0, 0, 0, "","","","", 0, 0, 0, 1, "", 0, 0, 0);
		testEdges[2] = new Edge(2, 4, 2, 3, 1, "Testvej", 0, 0, 0, 0, "","","","", 0, 0, 0, 2, "", 0, 0, 0);
		testEdges[3] = new Edge(3, 5, 2, 4, 1, "Testvej", 0, 0, 0, 0, "","","","", 0, 0, 0, 2, "", 0, 0, 0);
		testEdges[4] = new Edge(4, 6, 1, 5, 1, "Testvej", 0, 0, 0, 0, "","","","", 0, 0, 0, 1, "", 0, 0, 0);
		testEdges[5] = new Edge(6, 5, 4, 6, 1, "Testvej", 0, 0, 0, 0, "","","","", 0, 0, 0, 4, "", 0, 0, 0);

		pathToMatch.push(testEdges[3]);
		pathToMatch.push(testEdges[1]);
		for(Edge e: testEdges) {
			graph.addEdge(e.getFromNode(), e.getiD());
			System.out.println(e.getFromNode() +", " + e.getiD());
		}
		
		DijkstraSP dip = new DijkstraSP(graph, testEdges[0], testEdges, "Car", "Fastest");
			path = (Stack<Edge>) dip.pathTo(testEdges[3]);
		while(!path.isEmpty()) {
			assertTrue(path.pop().getiD() == pathToMatch.pop().getiD());
		}
	}
}
