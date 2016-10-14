package org.bala.MyNotes.resources;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MyNotesException extends WebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3804378469350378150L;

	public MyNotesException(String message, int status) {
		
		super(Response.status(status).entity(message).header("Content-Type", MediaType.TEXT_PLAIN).build());
	}
}
