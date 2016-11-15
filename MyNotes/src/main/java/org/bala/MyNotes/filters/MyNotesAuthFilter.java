package org.bala.MyNotes.filters;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;

import org.bala.MyNotes.configuration.MyNotesConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.auth.AuthFilter;

public class MyNotesAuthFilter<P extends Principal> extends AuthFilter<String, P> {

	final static Logger logger = LoggerFactory.getLogger(MyNotesAuthFilter.class);
	
	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		
		
		
        if (!requestContext.getCookies().containsKey(MyNotesConstant.SESSION_ID_HEADER) 
        		|| !authenticate(requestContext, requestContext.getCookies().get(MyNotesConstant.SESSION_ID_HEADER).getValue(), SecurityContext.DIGEST_AUTH)) {
           
        	logger.error("Unauthorized request. Exception valid session. But not found." + requestContext.getUriInfo().getAbsolutePath().toString());
        	
        	logger.error("Session Header Value." + (requestContext.getCookies().containsKey(MyNotesConstant.SESSION_ID_HEADER) ? requestContext.getCookies().get(MyNotesConstant.SESSION_ID_HEADER).getValue() : "Empty"));
        	
        	
        	if(requestContext.getUriInfo().getAbsolutePath().toString().contains("/user")) {
        		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build());
        		return;
        	}
        	
        	try {
				requestContext.abortWith(Response.temporaryRedirect(UriBuilder.fromUri(new URI("https://www.dropbox.com/oauth2/authorize"))
						.queryParam("client_id", MyNotesConstant.clientId)
						.queryParam("response_type", "code")
						.queryParam("force_reapprove", false)
						.queryParam("redirect_uri", "https://mynotes.io/login").build()).build());
			} catch (URISyntaxException e) {
				
				logger.error("Error in redirecting to github site. Returning internal server error.");
				requestContext.abortWith(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
			}
        }
	}

	public static class Builder<P extends Principal> extends  AuthFilterBuilder<String, P, MyNotesAuthFilter<P>> {

			@Override
			protected MyNotesAuthFilter<P> newInstance() {
			    return new MyNotesAuthFilter<>();
			}
	}
	
}
