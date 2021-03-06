package com.flow.manager.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppProperties {

    public static final String pathToCertificatePublicKey = "private/flowManagerBot.pem"; //only for self-signed webhooks
    public static final String pathToCertificateStore = "private/flowManagerBot.jks"; //self-signed and non-self-signed.

    public static String APPLICATION_NAME;

    public static String AUTH_URI;
    public static String TOKEN_URI;
    public static String REDIRECT_URI;
    public static String GOOGLE_CLIENT_ID;
    public static String GOOGLE_CLIENT_SECRET;
    public static String HTTP_PORT;
    public static String HTTPS_PORT;
    public static String CERTIFICATE_STORE_PASSWORD; //password for your certificate-store

    //configure telegram bot
    public static String BOT_NAME;
    public static String BOT_TOKEN;
    public static boolean USE_WEBHOOK = false;
    public static String EXTERNALWEBHOOKURL; // https://(xyz.)externaldomain.tld
    public static String INTERNALWEBHOOKURL; // https://(xyz.)localip/domain(.tld)
    public static String DRIVE_PLAYLIST_FILE_ID;
    
    @Value("${KEYSTOREPASSWORD:notused}")
    private void setCertificateStorePassword(String certificateStorePassword){
        AppProperties.CERTIFICATE_STORE_PASSWORD = certificateStorePassword;
    }

    @Value("${external.webhook.url}")
    private void setExternalWebHookUrl(String externalWebhookUrl){
        AppProperties.EXTERNALWEBHOOKURL = externalWebhookUrl;
    }

    @Value("${internal.webhook.url}")
    private void setInternalWebHookUrl(String internalWebhookUrl){
        AppProperties.INTERNALWEBHOOKURL = internalWebhookUrl;
    }

    @Value("${server.https.port}")
    private void setHttpsServerPort(String httpsServerPort){
        AppProperties.HTTPS_PORT = httpsServerPort;
    }

    @Value("${server.http.port}")
    private void setHttpServerPort(String httpServerPort){
        AppProperties.HTTP_PORT = httpServerPort;
    }

    @Value("${OAUTH2_REDIRECT_URI}")
    private void setRedirectUri(String redirectUri){
        AppProperties.REDIRECT_URI = redirectUri;
    }

    @Value("${google.client.clientSecret}")
    private void setGoogleClientSecret(String googleClientSecret){
        AppProperties.GOOGLE_CLIENT_SECRET = googleClientSecret;
    }

    @Value("${google.client.clientId}")
    private void setGoogleClientId(String googleClientId){
        AppProperties.GOOGLE_CLIENT_ID = googleClientId;
    }

    @Value("${google.client.accessTokenUri}")
    private void setTokenUri(String tokenUri){
        AppProperties.TOKEN_URI = tokenUri;
    }

    @Value("${google.client.userAuthorizationUri}")
    private void setAuthUri(String authUri){
        AppProperties.AUTH_URI = authUri;
    }

    @Value("${USE_WEBHOOK:false}")
    private void setUseWebhook(boolean useWebHook){
        AppProperties.USE_WEBHOOK = useWebHook;
    }

    @Value("${BOT_NAME}")
    private void setBotName(String botName){
        AppProperties.BOT_NAME = botName;
    }

    @Value("${BOT_TOKEN}")
    private void setBotToken(String botToken){
        AppProperties.BOT_TOKEN = botToken;
    }

    @Value("${DRIVE_PLAYLIST_FILE_ID}")
    private void setDrivePlaylistFileId(String drivePlaylistFileId){
        AppProperties.DRIVE_PLAYLIST_FILE_ID = drivePlaylistFileId;
    }

    @Value("${application.name}")
    private void setApplicationName(String applicationName){
        AppProperties.APPLICATION_NAME = applicationName;
    }

}
