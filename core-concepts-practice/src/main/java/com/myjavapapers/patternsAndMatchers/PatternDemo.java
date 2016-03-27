package com.myjavapapers.patternsAndMatchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternDemo {

	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("Dilileep");
		Matcher matcher = pattern.matcher("i*l");
		//System.out.println(matcher.replaceAll("bha"));
		//System.out.println(matcher.replaceFirst("ILU"));
		System.out.println(Pattern.matches("il","Dilileep"));
	//	System.out.println(matcher.lookingAt());
		
		
	}
}
