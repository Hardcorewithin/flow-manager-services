package com.flow.manager.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppProperties {

    public static final String pathToCertificatePublicKey = "private/flowManagerBot.pem"; //only for self-signed webhooks
    public static final String pathToCertificateStore = "private/flowManagerBot.jks"; //self-signed and non-self-signed.

    public static String AUTH_URI;
    public static String TOKEN_URI;
    public static String REDIRECT_URI;
    public static String GOOGLE_CLIENT_ID;
    public static String GOOGLE_CLIENT_SECRET;
    public static String HTTP_PORT;
    public static String HTTPS_PORT;
    public static String CERTIFICATE_STORE_PASSWORD; //password for your certificate-store

    //public static String SPRING_USERNAME;
    //public static String SPRING_PASSWORD;
    
    //configure telegram bot
    public static String BOT_NAME;
    public static String BOT_TOKEN;
    public static boolean USE_WEBHOOK = false;
    public static String EXTERNALWEBHOOKURL; // https://(xyz.)externaldomain.tld
    public static String INTERNALWEBHOOKURL; // https://(xyz.)localip/domain(.tld)
    public static String YOUTUBE_CHANNEL_ID;


    /*@Value("${SPRING_PASSWORD}")
    private void setSpringPassword(String springPassword){
        AppProperties.SPRING_PASSWORD = springPassword;
    }
    
    @Value("${SPRING_USERNAME}")
    private void setSpringUsername(String springUsername){
        AppProperties.SPRING_USERNAME = springUsername;
    }*/
    
    @Value("${KEYSTOREPASSWORD}")
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

    @Value("${oauth2.redirect.uri}")
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

    @Value("${bot.use_webhook}")
    private void setUseWebhook(boolean useWebHook){
        AppProperties.USE_WEBHOOK = useWebHook;
    }

    @Value("${bot.name}")
    private void setBotName(String botName){
        AppProperties.BOT_NAME = botName;
    }

    @Value("${bot.token}")
    private void setBotToken(String botToken){
        AppProperties.BOT_TOKEN = botToken;
    }

    @Value("${YOUTUBE_CHANNEL_ID}")
    private void setYoutubeChannelId(String youtubeChannelId){
        AppProperties.YOUTUBE_CHANNEL_ID = youtubeChannelId;
    }

}
