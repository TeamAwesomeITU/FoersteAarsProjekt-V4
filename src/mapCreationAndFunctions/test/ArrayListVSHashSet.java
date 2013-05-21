package mapCreationAndFunctions.test;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Tests whether ArrayList or HashSets are fastest. 
 *
 */
public class ArrayListVSHashSet {
	
	private ArrayList<Integer> list;
	private HashSet<Integer> set;
	private int numberOfTestNumbers = 800000;
	private int numberOfContainsNumbers = 50000;
	
	/**
	 * Constructor to run the test.
	 */
	public ArrayListVSHashSet()
	{
				
		double startTimeListCreation = System.currentTimeMillis();
		createArrayList();
		double listCreationDuration = System.currentTimeMillis()-startTimeListCreation;
		
		
		double startTimeSetCreation = System.currentTimeMillis();
		createHashSet();
		double setCreationDuration = System.currentTimeMillis()-startTimeSetCreation;
		
		double startTimeListIteration = System.currentTimeMillis();
		iterateThroughEntireList();
		double listIterationDuration = System.currentTimeMillis()-startTimeListIteration;
		
		double startTimeSetIteration = System.currentTimeMillis();
		iterateThroughEntireSet();
		double setIterationDuration = System.currentTimeMillis()-startTimeSetIteration;
		
		double startTimeListContains = System.currentTimeMillis();
		containList();
		double listContainsDuration = System.currentTimeMillis() - startTimeListContains;
		
		double startTimeSetContains = System.currentTimeMillis();
		containSet();
		double setContainsDuration = System.currentTimeMillis() - startTimeSetContains;
		
		
		System.out.println("List creation took: " + listCreationDuration);
		System.out.println("Set creation took: " + setCreationDuration);
		System.out.println("List iteration took: " + listIterationDuration);
		System.out.println("Set iteration took: " + setIterationDuration);
		System.out.println("List contains took: " + listContainsDuration);
		System.out.println("Set contains took: " + setContainsDuration);
		
	}
	
	/**
	 * Contains for HashSet.
	 */
	private void containSet() {
		for (int i = 0; i < numberOfContainsNumbers; i++) {
			set.contains(numberOfContainsNumbers-i);
		}		
	}

	/**
	 * Contains for ArrayList.
	 */
	private void containList() {
		for (int i = 0; i < numberOfContainsNumbers; i++) {
			list.contains(numberOfContainsNumbers-i);
		}		
	}

	/**
	 * Iterates through the entire HashSet.
	 */
	@SuppressWarnings("unused")
	private void iterateThroughEntireSet() {
		for(int number : set)
			doNothing();		
	}

	/**
	 * Iterates through the entire ArrayList.
	 */
	@SuppressWarnings("unused")
	private void iterateThroughEntireList() {
		for(int number : list)
			doNothing();			
	}

	/**
	 * Creates an ArrayList.
	 */
	private void createArrayList()
	{
		list = new ArrayList<>();
		for (int i = 0; i < numberOfTestNumbers; i++) {
			list.add(i);
		}
		
	}
	
	/**
	 * Creates a HashSet.
	 */
	private void createHashSet()
	{
		set = new HashSet<Integer>();
		for (int i = 0; i < numberOfTestNumbers; i++)
			set.add(i);
	}
	
	/**
	 * Does nothing.
	 */
	private void doNothing()
	{
		
	}
	
	/**
	 * Mainmethod to run.
	 * @param args
	 */
	public static void main( String[] args ) {
		new ArrayListVSHashSet();
	}

}
