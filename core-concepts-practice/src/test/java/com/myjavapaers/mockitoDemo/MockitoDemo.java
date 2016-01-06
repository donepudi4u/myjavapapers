package com.myjavapaers.mockitoDemo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.myjavapapers.mockito.matchers.ListWithTwoValues;
/**
 * 
 * http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html#1
 * @author Dileep
 *
 */

public class MockitoDemo {
	
	@Test
	public void comparableInterfaceMockTest(){
		@SuppressWarnings("unchecked")
		Comparable<String> comparable = mock(Comparable.class);
		when(comparable.compareTo("TEST")).thenReturn(1);
		assertEquals(1, comparable.compareTo("TEST"));
	}
	
	@Test
	public void arrayListMockDemo(){
		List<String> strList = mock(List.class);
		Iterator<String> iterator = mock(Iterator.class);
		when(iterator.next()).thenReturn("STRING 1").thenReturn("String 2").thenReturn("String 3");
		when(strList.get(2)).thenReturn("Test");
		when(strList.get(3)).thenReturn("Donepudi");
		when(strList.get(4)).thenReturn("Bhavani");
		when(strList.get(5)).thenReturn("Dileep");
	//	when(strList.get(anyInt())).thenReturn(null);
		when(strList.contains(new ListWithTwoValues())).thenReturn(true);
		when(strList.iterator()).thenReturn(iterator);
		when(strList.addAll(isValid())).thenReturn(true);
		assertEquals("Bhavani", strList.get(4));
		
		assertNotNull(strList.iterator());
		
		Iterator<String> iterator2 = strList.iterator();
		assertEquals("STRING 1",iterator2.next());
		System.out.println(iterator2.next());
		System.out.println(iterator2.next());
	}
	
	@Test
	public void listWithMatchersDemo(){
		List<String> strList = mock(List.class);
		strList.addAll(Arrays.asList("two","Two","Three"));
		strList.addAll(Arrays.asList("one","Zero"));
		strList.addAll(Arrays.asList("one","three"));
		verify(strList,atLeast(2)).addAll(isValid());
	}
	
	@Test
	public void numberOfInvocations(){
		List<String> strList = mock(List.class);
		strList.add("Value One");
		strList.add("Value Two");
		
		strList.add("Value Two");
		strList.add("Value Four");
		strList.add("Value Five");
		verify(strList,times(1)).add("Value One");
		verify(strList,times(2)).add("Value Two");
	}
	
	@Test
	public void handlingVoidMethods(){
		try{
		List<String> strList = mock(List.class);
		doThrow(new Exception("Eception")).when(strList).clear();
		strList.clear();
		}catch (Exception e){
			Assert.assertTrue(true);
		}
	}
	
	
	private List<String> isValid() {
		return argThat(new ListWithTwoValues()); 
	}

}
