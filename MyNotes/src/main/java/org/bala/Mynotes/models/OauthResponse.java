package org.bala.Mynotes.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class OauthResponse {
	private String access_token;
	private String bearer;
	private String token_type;
	private String uid;
	private String account_id;
	
	public String getAccess_token() {
		return access_token;
	}
	
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	
	public String getBearer() {
		return bearer;
	}
	
	public void setBearer(String bearer) {
		this.bearer = bearer;
	}
	
	public String getToken_type() {
		return token_type;
	}
	
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}
	
	
}
