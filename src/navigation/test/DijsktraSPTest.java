package navigation.test;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

import mapCreationAndFunctions.data.Edge;
import navigation.DijkstraSP;
import navigation.EdgeWeightedDigraph;
import navigation.exceptions.NoRoutePossibleException;

import org.junit.Test;

public class DijsktraSPTest {

	@Test
	public void testShortestPathOne() throws NoRoutePossibleException {
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
		}
		
		DijkstraSP dip = new DijkstraSP(graph, testEdges[0], testEdges, "Car", "Fastest");
			path = (Stack<Edge>) dip.pathTo(testEdges[3]);
		while(!path.isEmpty()) {
			assertTrue(path.pop().getiD() == pathToMatch.pop().getiD());
		}
	}
	
	@Test
	public void testShortestPathTwo() throws NoRoutePossibleException {
		Edge[] edgeArray = initialize();
		EdgeWeightedDigraph graph = new EdgeWeightedDigraph(edgeArray.length);
		Stack<Edge> path = new Stack<Edge>();
		Stack<Edge> pathToMatch = new Stack<Edge>();
		
		pathToMatch.push(edgeArray[17]);
		pathToMatch.push(edgeArray[8]);
		pathToMatch.push(edgeArray[6]);
		pathToMatch.push(edgeArray[5]);
		pathToMatch.push(edgeArray[2]);
		
		for(Edge e: edgeArray) {
			graph.addEdge(e.getFromNode(), e.getiD());
		}
		
		DijkstraSP dip = new DijkstraSP(graph, edgeArray[0], edgeArray, "Car", "Shortest");
		path = (Stack<Edge>) dip.pathTo(edgeArray[17]);
		while(!path.isEmpty()) {
			Edge edgeToMatch = pathToMatch.pop();
			Edge edge = path.pop(); 
			assertTrue(edge.getFromNode() == edgeToMatch.getFromNode() && edge.getToNode() == edgeToMatch.getToNode());
		}
		
	}
	
	public Edge[] initialize() {

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("XML/DjikstraTest.txt"), "UTF-8"));

			Edge[] edgeArray = new Edge[22];
			String line;
			int counter = 0;
			while((line = reader.readLine()) != null) 
			{
				String[] lineParts = line.split("\\,");
				
				Edge edge = new Edge(Integer.parseInt(lineParts[0].trim()), Integer.parseInt(lineParts[1].trim()), Double.parseDouble(lineParts[2].trim()), counter+1, 1,"" , 1, 1, 1, 1, "", "", "", "", 1, 1, 1, 0.0, "", 1, 1, 1);

				edgeArray[counter] = edge;
				counter++;
			}
			
			for(Edge edges : edgeArray) {
				System.out.println(edges.getLength());
			}
			
			reader.close();
			return edgeArray;

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
}
