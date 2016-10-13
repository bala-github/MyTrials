package org.bala.MyNotes.views;

import io.dropwizard.views.View;

import org.bala.Mynotes.models.User;

public class UserView extends View {

	private final User user;
	
	public UserView(User user) {
		super("/templates/welcome.ftl");
		this.user = user;
	}
	
	public User getUser() {
		return user;		
	}
}
