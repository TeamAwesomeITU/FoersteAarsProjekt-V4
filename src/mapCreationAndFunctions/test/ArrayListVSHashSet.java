package mapCreationAndFunctions.test;

import java.util.ArrayList;
import java.util.HashSet;

public class ArrayListVSHashSet {
	
	private ArrayList<Integer> list;
	private HashSet<Integer> set;
	private int numberOfTestNumbers = 800000;
	private int numberOfContainsNumbers = 50000;
	
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
	
	private void containSet() {
		for (int i = 0; i < numberOfContainsNumbers; i++) {
			set.contains(numberOfContainsNumbers-i);
		}		
	}

	private void containList() {
		for (int i = 0; i < numberOfContainsNumbers; i++) {
			list.contains(numberOfContainsNumbers-i);
		}		
	}

	private void iterateThroughEntireSet() {
		for(int number : set)
			doNothing();		
	}

	private void iterateThroughEntireList() {
		for(int number : list)
			doNothing();			
	}

	private void createArrayList()
	{
		list = new ArrayList<>();
		for (int i = 0; i < numberOfTestNumbers; i++) {
			list.add(i);
		}
		
	}
	
	private void createHashSet()
	{
		set = new HashSet<Integer>();
		for (int i = 0; i < numberOfTestNumbers; i++)
			set.add(i);
	}
	
	private void doNothing()
	{
		
	}
	
	public static void main( String[] args ) {
		new ArrayListVSHashSet();
	}

}
