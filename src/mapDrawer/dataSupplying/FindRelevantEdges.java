package mapDrawer.dataSupplying;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import mapDrawer.AreaToDraw;
import mapDrawer.ZoomLevel;
import mapDrawer.drawing.Edge;

/**
 * Locates the Edges that has a least one point that lies within the AreaToDraw
 */
public class FindRelevantEdges {

	/**
	 * Finds all Edges, which are connected to a node in the specified AreaToDraw
	 * @return A HashSet of all Edges, which are connected to a node in the specified AreaToDraw
	 */
	public static HashSet<Edge> findEdgesToDraw(AreaToDraw area)
	{
		return findEdges(area);
	}
		

	/**
	 * This method finds the Edges, that is found inside the input area.
	 * @param area Current area the user are viewing on the map.
	 * @return A HashSet of the Edges, that is found inside the input area and belongs to a Node in the nodeSet.
	 */
	private static HashSet<Edge> findEdges(AreaToDraw area)
	{
		
		HashSet<Node> nodeSet = QuadTree.searchAreaForNodes(area);
		Iterator<Node> iterator = nodeSet.iterator();
		HashSet<Edge> foundEdgesSet = new HashSet<Edge>();

		HashSet<Integer> zoomLevel = ZoomLevel.getlevel(area.getPercentageOfEntireMap());

		while(iterator.hasNext())
		{
			Node node = iterator.next();
			int[] edgeIDs = node.getEdgeIDs();
			
			HashMap<Integer, Edge> allEdgesMap = DataHolding.getEdgeMap();
			
			for (int i = 0; i < edgeIDs.length; i++) {
				Edge edge = allEdgesMap.get(edgeIDs[i]);
				if(zoomLevel.contains(edge.getRoadType()))
						foundEdgesSet.add(edge);
			}
			

		}			

		return foundEdgesSet;
	}
}