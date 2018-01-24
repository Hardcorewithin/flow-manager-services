package com.flow.manager.service.auth;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.flow.manager.constants.Auth;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.services.drive.Drive;

@Service
public class DriveAuth extends Auth implements IAuthorize{

	@Value("${drive.client.redirectUri}")
	private String redirectUri;

	/** 
	 *
	 * If modifying these scopes, delete your previously saved credentials
	 */
	@Value("${drive.client.scope}")
	private String scope;
	
	private Drive client;

	/**
	 * Build and return an authorized Drive client service.
	 * @return an authorized Drive client service
	 * @throws IOException
	 */
	public Drive getService(String code) throws IOException {
		// Authorize the request.
		TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
		credential = flow.createAndStoreCredential(response, "userID");
		client = new Drive.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
				.setApplicationName(applicationName)
				.build();

		return client;
	}

	public Drive getClient(){
		return client;
	}

	public String authorize() throws Exception {
		AuthorizationCodeRequestUrl authorizationUrl;
		if (flow == null) {
			Details web = new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientSecret);
			clientSecrets = new GoogleClientSecrets().setWeb(web);

			flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
					Arrays.asList(scope.split(","))).build();
		}
		authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri);

		System.out.println("drive authorization Url ->" + authorizationUrl);
		return authorizationUrl.build();
	}
}
