package navigation.test;


import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import mapCreationAndFunctions.data.City;
import mapCreationAndFunctions.data.Edge;
import mapCreationAndFunctions.search.CitySearch;

import org.junit.Test;

public class DjikstraTest {

	public static void main(String[] args) {
		new DjikstraTest();
	}

	public DjikstraTest() {
		initialize();
	}
	
}

