package org.bala.devtools.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.bala.ESQL.ESQuerySyntax;
import org.bala.ESQL.TokenValidationException;
import org.bala.devtools.models.ActionRequest;
import org.bala.devtools.models.ActionResponse;
import org.bala.devtools.models.ActionResponse.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/action")
public class ActionResource {
	
	
	private static final Logger logger = LoggerFactory.getLogger(ActionResource.class); 

	@POST
	@Path("/convert_to_es")
	@Consumes("application/json")
	@Produces("application/json")
	public ActionResponse convertToESQuery(ActionRequest request) {
		
		ESQuerySyntax syntax = new ESQuerySyntax();
		ActionResponse response = new ActionResponse();

		logger.info("Request: {}", request.getInput());
		
		try {
			
			response.setResponse(syntax.getQuery(syntax.validateQuery(request.getInput())));
			response.setStatus(Status.SUCCESS);
			logger.trace("Response: {}", response.getResponse());
		} catch (TokenValidationException e) {
			logger.error("Error {}", e.getMessage());
			e.printStackTrace();
			response.setStatus(Status.FAILURE);
			response.setResponse(e.getMessage());
		}
		
		return response;
	}
	
	
	
}
