package org.bala.devtools.models;

public class ActionResponse {

	public static enum Status {
		SUCCESS,
		FAILURE
	}
	
	private int code = 200; //defaults to OK
	
	private Status status;
	
	private String response;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
