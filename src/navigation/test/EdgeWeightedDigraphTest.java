package navigation.test;

import static org.junit.Assert.assertTrue;
import mapCreationAndFunctions.data.DataHolding;
import navigation.EdgeWeightedDigraph;
import navigation.data.Bag;

import org.junit.Test;

public class EdgeWeightedDigraphTest {

	@Test
	public void fillingUpGraphTest() {
		int numberOfNodes = DataHolding.getNumberOfNodes();

		EdgeWeightedDigraph graph = DataHolding.getGraph();

		int nodesInGraph = graph.nodes();		
		assertTrue(numberOfNodes == nodesInGraph);

		Bag<Integer>[] adj = graph.getAdj();
		for (int n = 0; n < graph.nodes(); n++)
			for (int e : adj[n]) 
			{
				assertTrue(DataHolding.getEdge(e).getFromNode() == n+1 || DataHolding.getEdge(e).getToNode() == n+1);
			}
	}
}
