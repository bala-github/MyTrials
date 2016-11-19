package org.bala.MyNotes.handlers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.bala.MyNotes.Utils.SessionUtils;
import org.bala.MyNotes.configuration.MyNotesConstant;
import org.bala.MyNotes.resources.NotesResource;
import org.bala.Mynotes.models.OauthResponse;
import org.bala.Mynotes.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.BasicAccount;
import com.dropbox.core.v2.users.GetAccountErrorException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginHandler {

	private final static ObjectMapper mapper = new ObjectMapper();
	
	private final static Logger logger = LoggerFactory.getLogger(LoginHandler.class);

	private DbxAppInfo appInfo;

	private DbxRequestConfig requestConfig;
	
	public LoginHandler(DbxAppInfo appInfo, DbxRequestConfig requestConfig) {
		this.appInfo = appInfo;
		this.requestConfig = requestConfig;
	}

	public Response handleLoginWithOauthCode(String code, String redirectURI) {
		
		try {
						
			//Get Access Token.
			OauthResponse oauthtoken = getAccessToken(code, redirectURI);
			
			//Get user details
			BasicAccount  userDetails = getUserDetails(oauthtoken);
			
			User user = new User();
			
			user.setName(userDetails.getName().getDisplayName());
			user.setAccessToken(oauthtoken.getAccess_token());
			user.setAccount_id(oauthtoken.getAccount_id());

			//Frame and set session cookie
			return Response.temporaryRedirect(UriBuilder.fromResource(NotesResource.class).build())
					.cookie(new NewCookie(new Cookie(MyNotesConstant.SESSION_ID_HEADER, 
							SessionUtils.getSessionToken(user), "/", ".mynotes.io"), "Session", 315360000, false)).build();
			
		} catch (IOException | IllegalArgumentException | UriBuilderException | NoSuchAlgorithmException | DbxException e) {
			
			logger.error("Error in login request." + e.getMessage());
			e.printStackTrace();
			return Response.serverError().build();
		}
		
		
	}
	
	public User getUserDetails(String code) throws ClientProtocolException, IOException, GetAccountErrorException, IllegalArgumentException, UriBuilderException, DbxException {

		//Get Access Token.
		OauthResponse oauthtoken = getAccessToken(code, "");
		
		//Get user details
		BasicAccount  userDetails = getUserDetails(oauthtoken);
		
		User user = new User();
		
		user.setName(userDetails.getName().getDisplayName());
		user.setAccessToken(oauthtoken.getAccess_token());
		user.setAccount_id(oauthtoken.getAccount_id());
				
		return user;
		
	}
	private OauthResponse getAccessToken(String code, String redirectURI) throws ClientProtocolException, IOException {
		
		
		Request request =  Request.Post("https://api.dropboxapi.com/oauth2/token")
		.addHeader("Accept", "application/json");
		
		if(redirectURI != null && !redirectURI.isEmpty()) {
			request = request.bodyForm(new BasicNameValuePair("client_id", MyNotesConstant.clientId), new BasicNameValuePair("client_secret", MyNotesConstant.clientSecret),
				new BasicNameValuePair("code", code), new BasicNameValuePair("grant_type", "authorization_code"), new BasicNameValuePair("redirect_uri", redirectURI));
		}
		else {
			request = request.bodyForm(new BasicNameValuePair("client_id", MyNotesConstant.clientId), new BasicNameValuePair("client_secret", MyNotesConstant.clientSecret),
					new BasicNameValuePair("code", code), new BasicNameValuePair("grant_type", "authorization_code"));
		}
		
		
		HttpResponse response = request.execute().returnResponse();
		
						
		OauthResponse oauthtoken =  null;
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			oauthtoken = mapper.readValue(response.getEntity().getContent(), OauthResponse.class);
			logger.debug("OauthResponse:" + mapper.writeValueAsString(oauthtoken));
		} else {
			HashMap<String, String> output = mapper.readValue(response.getEntity().getContent(), new TypeReference<HashMap<String, String>>(){});
			logger.debug("OauthResponse:" + mapper.writeValueAsString(output));
		}
		return oauthtoken;
		
	}
	
	
	private BasicAccount getUserDetails(OauthResponse oauthtoken) throws ClientProtocolException, IllegalArgumentException, UriBuilderException, IOException, GetAccountErrorException, DbxException {
		
		DbxClientV2 client = new DbxClientV2(requestConfig, oauthtoken.getAccess_token());
		
		BasicAccount basicaccount = client.users().getAccount(oauthtoken.getAccount_id());
		
		
		
		
		logger.debug("User details obtained for login " + basicaccount.getName());
		
		return basicaccount;
	}
}
