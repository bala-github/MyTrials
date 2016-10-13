package org.bala.MyNotes.views;

import org.bala.Mynotes.models.User;

import io.dropwizard.views.View;

public class LogoutView extends View {

	private final User user;
	
	public LogoutView(User user) {
		super("/templates/signout.ftl");
		this.user = user;
	}
	
	public User getUser() {
		return user;		
	}
}
