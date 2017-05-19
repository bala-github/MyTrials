package org.bala.devtools.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.bala.ESQL.TokenValidationException;
import org.bala.devtools.handlers.ActionHandler;
import org.bala.devtools.models.ActionRequest;
import org.bala.devtools.models.ActionResponse;
import org.bala.devtools.models.ActionResponse.Status;
import org.bala.devtools.models.Base64ActionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;


@Path("/action")
public class ActionResource {
	
	private ActionHandler actionHandler = new ActionHandler();
	
	private static final Logger logger = LoggerFactory.getLogger(ActionResource.class); 

	@POST
	@Path("/convert_to_es")
	@Consumes("application/json")
	@Produces("application/json")
	@Timed(name="CONVERT_TO_ESQUERY_TIMER")
	@Metered(name="CONVERT_TO_ESQUERY_METER")
	public ActionResponse convertToESQuery(ActionRequest request) {

		ActionResponse response = new ActionResponse();

		logger.info("Request: {}", request.getInput());
		
		try {
			
			response.setResponse(actionHandler.getElasticSearchQuery(request.getInput()));
			response.setStatus(Status.SUCCESS);
			
			logger.trace("Response: {}", response.getResponse());

			return response;
		} catch (TokenValidationException e) {
			throw new BadRequestException(e.getMessage(), e);
		}
		
		
	}
	
	@POST
	@Path("/base64_encode")
	@Consumes("application/json")
	@Produces("application/json")
	@Timed(name="BASE64_ENCODE_TIMER")
	@Metered(name="BASE64_ENCODE_METER")	
	public ActionResponse encodeBase64(Base64ActionRequest request) {
		ActionResponse response = new ActionResponse();
		
		logger.info("Request: {}", request.getInput());
		
		response.setResponse(actionHandler.encodeBase64(request.isUrlSafe(), request.getInput()));
		response.setStatus(Status.SUCCESS);
		
		return response;

	}
	
	@POST
	@Path("/base64_decode")
	@Consumes("application/json")
	@Produces("application/json")
	@Timed(name="BASE64_DECODE_TIMER")
	@Metered(name="BASE64_DECODE_METER")		
	public ActionResponse decodeBase64(Base64ActionRequest request) {
		ActionResponse response = new ActionResponse();
		
		logger.info("Request: {}", request.getInput());
		
		response.setResponse(actionHandler.decodeBase64(request.isUrlSafe(), request.getInput()));
		response.setStatus(Status.SUCCESS);
		
		return response;

	}
	
	@POST
	@Path("/url_encode")
	@Consumes("application/json")
	@Produces("application/json")
	@Timed(name="URL_ENCODE_TIMER")
	@Metered(name="URL_ENCODE_METER")	
	public ActionResponse encodeURL(ActionRequest request) throws UnsupportedEncodingException {
		ActionResponse response = new ActionResponse();
		
		logger.info("Request: {}", request.getInput());
		
		response.setResponse(URLEncoder.encode(request.getInput(), StandardCharsets.UTF_8.toString()));
		response.setStatus(Status.SUCCESS);
		
		return response;

	}
	
	@POST
	@Path("/url_decode")
	@Consumes("application/json")
	@Produces("application/json")
	@Timed(name="URL_DECODE_TIMER")
	@Metered(name="URL_DECODE_METER")		
	public ActionResponse decodeURL(ActionRequest request) throws UnsupportedEncodingException {
		ActionResponse response = new ActionResponse();
		
		logger.info("Request: {}", request.getInput());
		
		response.setResponse(URLDecoder.decode(request.getInput(), StandardCharsets.UTF_8.toString()));
		response.setStatus(Status.SUCCESS);
		
		return response;

	}	
	
}
