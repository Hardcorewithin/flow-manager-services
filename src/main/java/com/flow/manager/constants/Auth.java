package com.flow.manager.constants;

import org.springframework.beans.factory.annotation.Value;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

public abstract class Auth {

	protected GoogleClientSecrets clientSecrets;

	protected GoogleAuthorizationCodeFlow flow;

	protected Credential credential;
	
	@Value("${application.name}")
    public String applicationName;
	
	@Value("${google.client.clientId}")
	protected String clientId;

	@Value("${google.client.clientSecret}")
	protected String clientSecret;
	
	/**
     * This is the directory that will be used under the user's home directory where OAuth tokens will be stored.
     */
    @Value("${oauth2.credentials.directory}")
    public String credentialDataStoreDirectory;
    
    protected static final String CREDENTIAL_DATASTORE_DRIVE = "flowmanager_drive";
    protected static final String CREDENTIAL_DATASTORE_YT = "flowmanager_youtube";
    
	/** Global instance of the {@link FileDataStoreFactory}. */
	protected static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the HTTP transport. */
	protected static HttpTransport HTTP_TRANSPORT;

	/** Global instance of the JSON factory. */
	protected static final JsonFactory JSON_FACTORY =
			JacksonFactory.getDefaultInstance();
	
	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}
}
