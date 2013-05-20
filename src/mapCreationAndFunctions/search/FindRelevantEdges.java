package mapCreationAndFunctions.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import mapCreationAndFunctions.AreaToDraw;
import mapCreationAndFunctions.ZoomLevel;
import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.data.Node;

/**
 * Locates the Edges that has a least one point that lies within the AreaToDraw	
 */
public class FindRelevantEdges {

	/**
	 * Finds all Edges, which are connected to a node in the specified AreaToDraw
	 * @return A HashSet of all Edges, which are connected to a node in the specified AreaToDraw
	 */
	public static ArrayList<Edge> findEdgesToDraw(AreaToDraw area)
	{
		return findEdges(area);
	}
		

	/**
	 * This method finds the Edges, that is found inside the input area.
	 * @param area Current area the user are viewing on the map.
	 * @return A HashSet of the Edges, that is found inside the input area and belongs to a Node in the nodeList.
	 */
	private static ArrayList<Edge> findEdges(AreaToDraw area)
	{		
		ArrayList<Node> nodeList = QuadTree.searchAreaForNodes(area);
		Iterator<Node> iterator = nodeList.iterator();
		ArrayList<Edge> foundEdgesSet = new ArrayList<Edge>();

		HashSet<Integer> zoomLevel = ZoomLevel.getlevel(area.getPercentageOfEntireMap());

		while(iterator.hasNext())
		{
			Node node = iterator.next();
			int[] edgeIDs = node.getEdgeIDs();
			
			for (int i = 0; i < edgeIDs.length; i++) {
				Edge edge = DataHolding.getEdge(edgeIDs[i]);
				if(zoomLevel.contains(edge.getRoadType()))
						foundEdgesSet.add(edge);
			}			

		}			

		return foundEdgesSet;
	}
}