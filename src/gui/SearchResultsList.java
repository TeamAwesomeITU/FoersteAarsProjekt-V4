package gui;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.JTextField;

import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.EdgeSearch;

@SuppressWarnings("serial")
public class SearchResultsList extends AbstractListModel<String> {
	
	ArrayList<String> searchResultsList;
	
	public SearchResultsList()
	{
		searchResultsList = new ArrayList<String>();
	}

	@Override
	public String getElementAt( int index )
	{ return searchResultsList.get(index); }

	@Override
	public int getSize()
	{ return searchResultsList.size(); }
	
	
	/**
	 * Clears the data of this datamodel.
	 */
	public void clear()
	{
		int index = searchResultsList.size() - 1;
		searchResultsList.clear();
		if( index >= 0 )
		{
			fireIntervalRemoved( this, 0, index );
		}
	}

	private void calculateResults(JTextField currentField)
	{
		Edge[] foundEdges = EdgeSearch.getRoadNameSuggestions(currentField.getText());
		searchResultsList = new ArrayList<>();
		for(int i = 0; i < foundEdges.length; i++)
			searchResultsList.add(foundEdges[i].getRoadName() + " - " + foundEdges[i].getPostalNumberLeft());		
		
	}

}
