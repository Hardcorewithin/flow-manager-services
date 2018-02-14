package com.flow.manager.constants;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.youtube.YouTubeScopes;

import java.util.Arrays;
import java.util.Collection;

public class FlowManagerConstants {

    public static String YOUTUBE_PLAYLIST_BASE_URL = "https://www.youtube.com/playlist?list=";
    public static final String YOUTUBE = "youtube";
    public static final String PLAYLIST_PUBLIC = "public";
    public final static String APPLICATION_NAME = "Flow Manager";
    public static final String USER_ID = "flow_manager";

    public static final Collection<String> FM_SCOPES = Arrays.asList(
            DriveScopes.DRIVE_APPDATA,
            DriveScopes.DRIVE,
            DriveScopes.DRIVE_METADATA,
            YouTubeScopes.YOUTUBE,
            YouTubeScopes.YOUTUBE_FORCE_SSL,
            YouTubeScopes.YOUTUBEPARTNER
    );
    //"https://www.googleapis.com/auth/youtube.force-ssl https://www.googleapis.com/auth/youtube https://www.googleapis.com/auth/youtubepartner https://www.googleapis.com/auth/drive.file https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/drive.appdata https://www.googleapis.com/auth/drive.metadata https://www.googleapis.com/auth/drive.metadata.readonly https://www.googleapis.com/auth/drive.readonly");

    public static String TELEGRAM_ME_URI =  "https://telegram.me/";

}
