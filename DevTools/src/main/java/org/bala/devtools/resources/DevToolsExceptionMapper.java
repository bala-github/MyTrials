package org.bala.devtools.resources;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.bala.devtools.models.ActionResponse;
import org.bala.devtools.models.ActionResponse.Status;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.jersey.errors.LoggingExceptionMapper;

@Provider
public class DevToolsExceptionMapper extends LoggingExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {

		final long id = logException(exception);
		
		ActionResponse actionResponse = new ActionResponse();
		
		actionResponse.setStatus(Status.FAILURE);
		
		if(exception instanceof WebApplicationException) {
			actionResponse.setCode(((WebApplicationException) exception).getResponse().getStatus());
			
		} else {
			actionResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR_500);
		}
		
		actionResponse.setResponse(formatErrorMessage(id, exception) + "\n" + exception.getMessage());
		
		return Response.status(actionResponse.getCode())
				.type(MediaType.APPLICATION_JSON_TYPE)
				.entity(actionResponse)
				.build();
	}
}
