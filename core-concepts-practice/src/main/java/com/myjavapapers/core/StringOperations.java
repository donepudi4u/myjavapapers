package com.myjavapapers.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class StringOperations {
	
	public static void main(String[] args) {
		StringOperations stringOperations = new StringOperations();
		String strValue = "This is Dileep Donepudi";
		//stringOperations.printFirstOccuranceOfRepeatCharactor(strValue);
		// stringOperations.printRepeats(strValue);
		//System.out.println(printRepeats);
		//stringOperations.sortNumbers();
		
		stringOperations.printWordsInReverseOrder(strValue);
	}
	
	private void printFirstOccuranceOfRepeatCharactor(String inputString){
		//char [] words = {'B','B','E','D','B','A','X'};
		char [] words = inputString.toCharArray();
		boolean repeatCharFound = false;
		for (int i = 0; i < words.length; i++) {
			if(!repeatCharFound){
				char charPos = words[i];
				for (int j = i+1; j < words.length; j++) {
					if(charPos == words[j]){
						System.out.println("First Repeating Char is : "+words[j] + " and First Occurrance is at :" + (i+1) +" And repeat char At position :" + (j+1));
						repeatCharFound = true;
						break;
					}
				}
			}
		}
		if(!repeatCharFound){
			System.out.println("No repeat charector found");
		}
	}
	
	private Integer testPrintRepeats(String inputString){
		char [] words = inputString.toCharArray();
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		Integer number;
		for (int i =0; i<words.length; i++) {
		    number = map.get(words[i]);
		    if (number != null) {
		        return number;//that's the position
		    } else {
		        map.put(words[i], i);
		    }
		}
		System.out.println("No repeat charector found");
		return null;
	}
	
	private void printRepeats(String test) {
	    // only characters means we need an array to accommodate 'A' to 'Z'
	    boolean[] charOccurred = new boolean['Z' - 'A'];

	    for (int i=0; i < test.length(); i++){
	        // our current character
	        char current = test.charAt(i);

	        if (charOccurred[current - 'A']){
	            System.out.println(current + " is repeated, first occurrence " + test.indexOf(current) + " Second repeat: " + i);
	            return;
	        } else {
	            // mark as already occured
	            charOccurred[current - 'A'] = true;
	        }
	    }
	    System.out.println("No repeats");
	}
	
	private void sortNumbers(){
		Integer[] numbersArray = {12,9,11,25,1,0,13,25,99};
		for (int i = 0; i < numbersArray.length; i++) {
			//Integer currentNumber = numbersArray[i];
			for (int j = 0; j < numbersArray.length; j++) {
				if (numbersArray[i] < numbersArray[j]){
					int temp = numbersArray[i];
					numbersArray[i] = numbersArray[j];
					numbersArray[j] = temp;
				}
			}
		}
		for (int i = 0; i < numbersArray.length; i++) {
			System.out.println(numbersArray[i]);
		}
	}

	private void printWordsInReverseOrder(String inputString){
		StringTokenizer stringTokenizer = new StringTokenizer(inputString," ");
		Set<String> wordsSet = new LinkedHashSet<String>();
		List<String> strings = new ArrayList<String>();
		while (stringTokenizer.hasMoreTokens()) {
			//wordsSet.add(stringTokenizer.nextToken());
			strings.add(stringTokenizer.nextToken());
		}
		Collections.reverse(strings);
		
		for (String string : strings) {
			System.out.println(string);
		}
		/*for (String word : wordsSet) {
			System.out.println(word);
		}*/
		
	}
	
}
