package com.myjavapapers.jackson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonTestClass {

	@Test
	public void givenJsonHasUnknownValuesButJacksonIsIgnoringUnknowns_whenDeserializing_thenCorrect()
	  throws JsonParseException, JsonMappingException, IOException {
	    
		String jsonAsString = 
	        "{stringValue:a," +
	        "intValue:1," +
	        "booleanValue:true," +
	        "stringValue2:something}";
	    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	 
	    MyDto readValue = mapper.readValue(jsonAsString, MyDto.class);
	 
	    assertNotNull(readValue);
	    assertEquals(readValue.getStringValue(), "a");
	    assertEquals(readValue.isBooleanValue(), true);
	    assertEquals(readValue.getIntValue(), 1);
	}
}
