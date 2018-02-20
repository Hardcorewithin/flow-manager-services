package com.flow.manager.constants;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.youtube.YouTubeScopes;

import java.util.Arrays;
import java.util.Collection;

public class AppConstants {

    public static String YOUTUBE_PLAYLIST_BASE_URL = "https://www.youtube.com/playlist?list=";
    public static final String YOUTUBE = "youtube";
    public static final String PLAYLIST_PUBLIC = "public";
    public static final String USER_ID = "flow_manager";

    public static final Collection<String> FM_SCOPES = Arrays.asList(
            DriveScopes.DRIVE_APPDATA,
            DriveScopes.DRIVE,
            DriveScopes.DRIVE_METADATA,
            YouTubeScopes.YOUTUBE,
            YouTubeScopes.YOUTUBE_FORCE_SSL,
            YouTubeScopes.YOUTUBEPARTNER
    );

    public static String TELEGRAM_ME_URI =  "https://telegram.me/";

}
