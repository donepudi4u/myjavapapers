package com.myjavapapers.mockito.matchers;
import java.util.List;

import org.mockito.ArgumentMatcher;


public class ListWithTwoValues extends ArgumentMatcher<List<String>> {

	@SuppressWarnings("unchecked")
	@Override
	public boolean matches(Object argument) {
		return ((List<String>)argument).size() >= 2;
	}



}
