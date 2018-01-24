package com.flow.manager.dto;

import java.io.Serializable;

public class FileDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5860332046069312256L;
	private String fileId;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
}
