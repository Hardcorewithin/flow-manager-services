package com.flow.manager.service;

import java.io.IOException;

import com.flow.manager.constants.FlowManagerConstants;
import com.flow.manager.service.impl.AuthServiceImpl;
import com.google.api.client.auth.oauth2.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.services.drive.Drive;
import com.google.api.services.youtube.YouTube;

@Service
public class ServicesHandler {

	@Autowired
	private AuthServiceImpl authService;

	public boolean initServices(String userId) throws IOException {

	    Credential credential = authService.getCredentials(userId);

	    YouTube _youtube = new YouTube.Builder(AuthServiceImpl.HTTP_TRANSPORT, AuthServiceImpl.JSON_FACTORY, credential)
				.setApplicationName(FlowManagerConstants.APPLICATION_NAME)
				.build();

	    Drive _drive = new Drive.Builder(AuthServiceImpl.HTTP_TRANSPORT, AuthServiceImpl.JSON_FACTORY, credential)
                .setApplicationName(FlowManagerConstants.APPLICATION_NAME)
                .build();

        if(_youtube == null || _drive == null) return false;

        this.youtube = _youtube;
        this.drive = _drive;

		return true;
	}

	public static YouTube youtube;

	public static Drive drive;
}
