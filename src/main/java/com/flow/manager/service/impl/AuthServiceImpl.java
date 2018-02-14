package com.flow.manager.service.impl;

import java.io.IOException;

import com.flow.manager.constants.AppProperties;
import com.flow.manager.constants.FlowManagerConstants;
import com.flow.manager.repo.entity.Token;
import com.flow.manager.repo.TokenRepository;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

	public static HttpTransport HTTP_TRANSPORT;

    @Autowired
    private TokenRepository tokenRepository;

	static {
	    try{
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        }catch (Exception e){
	        e.printStackTrace();
        }
    }


	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	public static String authorize() throws Exception {

		GoogleAuthorizationCodeFlow flow = initFlow();
        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
				.setAccessType("offline")
                .setApprovalPrompt("auto")
				.setRedirectUri(AppProperties.REDIRECT_URI);

        logger.debug("drive authorization Url ->" + authorizationUrl);

		return authorizationUrl.build();
	}

	private static GoogleAuthorizationCodeFlow initFlow() throws IOException{

		return new GoogleAuthorizationCodeFlow.Builder(
		        HTTP_TRANSPORT,
                JSON_FACTORY,
                AppProperties.GOOGLE_CLIENT_ID,
                AppProperties.GOOGLE_CLIENT_SECRET,
                FlowManagerConstants.FM_SCOPES)
				.build();
	}

     public Credential getCredentials() throws IOException {
	    //TODO: check that the user is in the db and the token is valid
         Token token = tokenRepository.findByUserId(FlowManagerConstants.USER_ID);

         GoogleCredential credential = new GoogleCredential.Builder().setTransport(AuthServiceImpl.HTTP_TRANSPORT)
                 .setJsonFactory(AuthServiceImpl.JSON_FACTORY)
                 .setClientSecrets(AppProperties.GOOGLE_CLIENT_ID, AppProperties.GOOGLE_CLIENT_SECRET)
                 .build()
                 .setRefreshToken(token.getRefreshToken())
                 .setAccessToken(token.getAccessToken())
         ;
        return credential;
    }

    public void storeCredentials(String authToken) throws IOException {

        GoogleAuthorizationCodeFlow flow = initFlow();
        GoogleTokenResponse tokenResponse = flow.newTokenRequest(authToken).setRedirectUri(AppProperties.REDIRECT_URI).execute();
        Token token = new Token(tokenResponse.getRefreshToken(),tokenResponse.getAccessToken(),FlowManagerConstants.USER_ID);
        tokenRepository.save(token);
    }

}
