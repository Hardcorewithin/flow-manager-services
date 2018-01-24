package com.flow.manager.service.auth;

public interface IAuthorize {

	public String authorize() throws Exception;
	
	public Object getClient();
}
