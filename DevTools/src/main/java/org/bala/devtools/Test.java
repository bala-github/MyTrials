package org.bala.devtools;

import java.io.IOException;

import org.bala.devtools.models.Base64ActionRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		
		String input = "{\"input\":\"asdf\",\"urlSafe\":false}";
		
		ObjectMapper mapper = new ObjectMapper();
		
		Base64ActionRequest req = mapper.readValue(input, Base64ActionRequest.class);
	}

}
