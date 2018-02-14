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

	public Drive getDriveService() throws IOException {
		Credential credential = authService.getCredentials();
		Drive client = new Drive.Builder(AuthServiceImpl.HTTP_TRANSPORT, AuthServiceImpl.JSON_FACTORY, credential)
				.setApplicationName(FlowManagerConstants.APPLICATION_NAME)
				.build();

		return client;
	}

	public YouTube getYouTubeService() throws IOException {
		Credential credential = authService.getCredentials();
		YouTube client = new YouTube.Builder(AuthServiceImpl.HTTP_TRANSPORT, AuthServiceImpl.JSON_FACTORY, credential)
				.setApplicationName(FlowManagerConstants.APPLICATION_NAME)
				.build();
		return client;
	}
}
