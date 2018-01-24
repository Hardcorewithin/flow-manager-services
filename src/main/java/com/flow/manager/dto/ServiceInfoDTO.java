package com.flow.manager.dto;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class ServiceInfoDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7772152676385058812L;

	private String platform;

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
}
