package org.bala.MyNotes.Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.bala.MyNotes.configuration.MyNotesConstant;
import org.bala.Mynotes.models.User;
import org.bouncycastle.util.encoders.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionUtils {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static String getSessionToken(User user) throws NoSuchAlgorithmException, JsonProcessingException {
		
		Map<String, String> sessionToken = new HashMap<String, String>();
		
		
		MessageDigest msgdigest = MessageDigest.getInstance("SHA-256");
		msgdigest.update(user.getName().getBytes(StandardCharsets.UTF_8));
		msgdigest.update(user.getAccessToken().getBytes(StandardCharsets.UTF_8));
		msgdigest.update(MyNotesConstant.clientSecret.getBytes(StandardCharsets.UTF_8));

		String checksum = Base64.toBase64String(msgdigest.digest());
	    
	    sessionToken.put(MyNotesConstant.LOGIN_FIELD, user.getName());
	    sessionToken.put(MyNotesConstant.ACCESS_TOKEN_FIELD, user.getAccessToken());
	    
	    sessionToken.put(MyNotesConstant.CHECKSUM_FIELD, checksum);
  
	    
		return Base64.toBase64String(mapper.writeValueAsString(sessionToken).getBytes(StandardCharsets.UTF_8));
	}
	
	public static User getSessionUser(String sessionTokenValue) {
		
		User user = null;
		try {
			Map<String, String> sessionToken = mapper.readValue(Base64.decode(sessionTokenValue), new TypeReference<HashMap<String, String>>(){});
		
			MessageDigest msgdigest = MessageDigest.getInstance("SHA-256");
			msgdigest.update(sessionToken.get(MyNotesConstant.LOGIN_FIELD).getBytes(StandardCharsets.UTF_8));
			msgdigest.update(sessionToken.get(MyNotesConstant.ACCESS_TOKEN_FIELD).getBytes(StandardCharsets.UTF_8));			
			msgdigest.update(MyNotesConstant.clientSecret.getBytes(StandardCharsets.UTF_8));
			
		    String checksum = Base64.toBase64String(msgdigest.digest());
		    
		    if(checksum.equalsIgnoreCase(sessionToken.get(MyNotesConstant.CHECKSUM_FIELD))) {
		    	user = new User();
		    	user.setName(sessionToken.get(MyNotesConstant.LOGIN_FIELD));
		    	user.setAccessToken(sessionToken.get(MyNotesConstant.ACCESS_TOKEN_FIELD));
		    } else {
		    	//checksum mismatch.
		    }
		    
		} catch (IOException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}
}
