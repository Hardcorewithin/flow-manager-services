package com.flow.manager.dto;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class PlaylistDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
