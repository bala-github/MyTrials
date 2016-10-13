package org.bala.MyNotes.filters;

import java.util.Optional;

import org.bala.MyNotes.Utils.SessionUtils;
import org.bala.Mynotes.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;


public class MyNotesAuthenticator  implements Authenticator<String, User>{

	private static final Logger logger = LoggerFactory.getLogger(MyNotesAuthenticator.class);
	
	public Optional<User> authenticate(String token)
			throws AuthenticationException {

		logger.debug("Verifying credentials " + token);
		
		User user = SessionUtils.getSessionUser(token);

		
		if(user != null) {
			return Optional.of(user);
		} else {
			return Optional.empty();
		}
	}
	
}
