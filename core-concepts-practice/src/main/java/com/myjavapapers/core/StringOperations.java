package com.myjavapapers.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

public class StringOperations {
	
	public static void main(String[] args) {
		StringOperations stringOperations = new StringOperations();
		String strValue = "This is Dileep Donepudi";
		//stringOperations.printFirstOccuranceOfRepeatCharactor(strValue);
		// stringOperations.printRepeats(strValue);
		//System.out.println(printRepeats);
		//stringOperations.sortNumbers();
		// http://10.32.8.22/FAB/STR.nsf/0/044315bf610aae6e8825689d0003a73d/STR_INSTRUCTION/0.D84.jpg
		//stringOperations.printWordsInReverseOrder(strValue);
		try {
			buildImageURL("http", "10.32.8.22", "/FAB/STR.nsf/0/044315bf610aae6e8825689d0003a73d/STR_INSTRUCTION/0.D84?OpenElement&amp;FieldElemFormat=jpg");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private static String buildImageURL(String protocal, String host, String sourceUrl) throws MalformedURLException {
        URL imageUrl = new URL(protocal + "://" + host + "/" + sourceUrl);

        //   System.out.println("Image path [ " + imageUrl.getPath() + " ]");
        //    System.out.println("Image Query params [ " + imageUrl.getQuery() + " ]");
        String query = imageUrl.getQuery();

        String[] imageFormat = query.split("=");
        if (imageFormat != null && imageFormat.length > 0) {
            //        System.out.println("Image Format " + imageFormat[1]);
        }
        String imagePath = imageUrl.getPath();
        System.out.println("imagePath : "+ imagePath);
        int count = StringUtils.countMatches(imagePath, "/");
       // System.out.println("----" +StringUtils.ordinalIndexOf(imagePath, "/", count-1));
        System.out.println(imagePath.substring(StringUtils.ordinalIndexOf(imagePath, "/", count-1)+1, StringUtils.lastIndexOf(imagePath, "/")));
        String imageName = imagePath.substring(StringUtils.lastIndexOf(imagePath, "/"), imagePath.length());
        String newPath = imagePath.substring(1, StringUtils.lastIndexOf(imagePath, "/")) + imageName + "." + imageFormat[1];
        //   System.out.println("New Path" + newPath);
        String imageURL = protocal + "://" + host + newPath;
        System.out.println("New URL Built : " + imageURL);
        
        
        int count2 = StringUtils.countMatches(imagePath, "/");
         String iteamName = imagePath.substring(StringUtils.ordinalIndexOf(imagePath, "/", StringUtils.countMatches(imagePath, "/")-1)+1, StringUtils.lastIndexOf(imagePath, "/"));
        return imageURL;
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
