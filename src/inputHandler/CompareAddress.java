package inputHandler;

import inputHandler.exceptions.MalformedAdressException;

import java.util.ArrayList;
import java.util.Arrays;


import mapCreationAndFunctions.data.DataHolding;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.CitySearch;

public class CompareAddress {

	private static String[] testArray = new String[]{"følfodvej","266",null,null,"2300",null};
	private String roadName = null;
	private String city = null;	
	private int letter = -1;
	private int post = -1;
	private int number = -1;
	private int relevanceCount = 0;
	ArrayList<Integer> possibleAdress = new ArrayList<>();


	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		new CompareAddress(testArray);
		long t = System.currentTimeMillis();
		System.out.println("At finde en mulig adress tager " + (t-s));
	}


	public CompareAddress(String[] addressArray) {


		if(addressArray[0]!=null) {
			roadName = addressArray[0].toLowerCase().trim(); relevanceCount++;
			if(addressArray[1]!=null){ number = Integer.parseInt(addressArray[1]); relevanceCount++;}
			if(addressArray[2]!=null){ letter = addressArray[2].toLowerCase().trim().charAt(0); relevanceCount++;} 
			if(addressArray[4]!=null){ post = Integer.parseInt(addressArray[4]); relevanceCount++;}
			if(addressArray[5]!=null){ city = addressArray[5].toLowerCase().trim(); relevanceCount++;}
		} else{	System.out.println("No roadname. No destination");
		}
		compareEntireAdress();
	}

	public void compareEntireAdress(){
		Edge edge;
		int relevance = 0;
		int length = DataHolding.getEdgeArray().length;
		long s = System.currentTimeMillis();
		
		for(int i = 1; i < length; i++) {
			edge = DataHolding.getEdge(i);

			if(compareRoad(edge)==true) {
				relevance++;
				if(post!= -1 && comparePost(edge)==true){
					relevance++; 
				}
				if(number!=-1 && compareNumber(edge)==true)
				{
					relevance++;
					if(letter!=-1 && compareLetter(edge)==true)
					{
						relevance++;
					}
				}
				if(city!=null && compareCity(edge)==true){
					relevance++;
				}

				if(relevance==relevanceCount) {
					possibleAdress.add(edge.getiD());
					System.out.println(edge.getRoadName());
					System.out.println(edge.getiD());
				}
				else if(relevance>1) {
					//System.out.println(relevance);
					//System.out.println(edge.getiD());
					//System.out.println(edge.getPostalNumberLeft());
				}
				
			}
			relevance = 0;
		} 
		long t = System.currentTimeMillis();
		System.out.println("wtf " + (t-s));
	}
	//possibleAdress.add(edge.getiD());



	/*
	 * 
	 */


	private boolean compareRoad(Edge edge) {

		if(edge.getRoadName().trim().toLowerCase().equals(roadName)){
			return true; }
		return false;
	}

	private boolean compareNumber(Edge edge){

		if(number%2==0){
			if(edge.getFromRightNumber()>0 && edge.getToRightNumber()>0) {
				int fromRight = edge.getFromRightNumber();
				int toRight = edge.getToRightNumber();
				if(number>=fromRight && number<= toRight){
					return true;
				}
			}
		}
		if(number%2==1){
			if(edge.getFromLeftNumber()>0 && edge.getToLeftNumber()>0) { 
				int fromLeft = edge.getFromLeftNumber(); 
				int toLeft = edge.getToLeftNumber();
				if(number>=fromLeft &&number<= toLeft){
					return true;
				}
			}
		}
		return false;
	}
	private boolean compareLetter(Edge edge){
		if(number%2==0){
			if(edge.getFromRightLetter().length()>0 && edge.getToRightLetter().length()>0){
				int fromRight = (int)edge.getFromRightLetter().toLowerCase().charAt(0);
				int toRight = (int)edge.getToRightLetter().toLowerCase().charAt(0);
				if(letter>=fromRight && letter<= toRight)
					return true;
			}
		}

		if(number%2==1){
			if(edge.getFromLeftLetter().length()>0 && edge.getToLeftLetter().length()>0){
				int fromLeft = (int)edge.getFromLeftLetter().toLowerCase().charAt(0);
				int toLeft = (int)edge.getToLeftLetter().toLowerCase().charAt(0);
				if(letter>=fromLeft && letter<=toLeft)
					return true;
			}
		}
		return false; 
	}

	private boolean comparePost(Edge edge){
		int leftPost = edge.getPostalNumberLeft();
		int rightPost = edge.getPostalNumberRight();

		if(post==leftPost || post==rightPost){
			return true;
		}
		return false; 
	}

	private boolean compareCity(Edge edge){
		
		if(CitySearch.searchForCityName(city).length==1){return true;}
		
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}