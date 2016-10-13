package org.bala.Mynotes.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Note {

	private String title;
	
	private String description;
	
	private boolean url;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isUrl() {
		return url;
	}

	public void setUrl(boolean url) {
		this.url = url;
	}


}
