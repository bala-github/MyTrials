package org.bala.MyNotes.filters;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.ext.Provider;

import org.bala.MyNotes.Utils.SessionUtils;
import org.bala.MyNotes.configuration.MyNotesConstant;
import org.bala.Mynotes.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class SessionRefreshFilter implements ContainerResponseFilter {

	private static final Logger logger = LoggerFactory.getLogger(SessionRefreshFilter.class);
	
	@Context
	HttpServletResponse response;
		
	@Override
	public void filter(ContainerRequestContext requestContext,	ContainerResponseContext responseContext) throws IOException {
		
		Principal principal = requestContext.getSecurityContext().getUserPrincipal();
		
		if(principal != null && principal instanceof User ) {
			User user = (User) principal;
			
			logger.debug("User:" + principal.getName() +  "Path:" + requestContext.getUriInfo().getPath());
			
			try {
												
				/* 
				 * Jersey is REST i.e stateless, so in Jersey ConainterResponseFilter we cannot set cookie.
				 * http://stackoverflow.com/questions/29954305/jersey-can-i-add-a-cookie-in-containerresponsefilter
				 * 
				 * But can we inject HttpServletResponse Context and set cookie. Try it...
				 */
				
				responseContext.getHeaders().add("Set-Cookie", new NewCookie(new Cookie(MyNotesConstant.SESSION_ID_HEADER, 
						SessionUtils.getSessionToken(user), "/", ".mynotes.io"), "Session", requestContext.getUriInfo().getPath().equalsIgnoreCase("signout") ? 1 : 315360000, new Date(1), false, false).toString());
					
				
			} catch (NoSuchAlgorithmException e) {
			
				logger.error("Error in refreshing session:"+ e.getMessage());
				e.printStackTrace();
			}
			
		} else {
			logger.debug("User is null");
		}
		
	}

}
