package com.flow.manager.service.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.flow.manager.constants.AppProperties;
import com.flow.manager.constants.AppConstants;
import com.google.api.services.youtube.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flow.manager.dto.PlaylistDto;
import com.flow.manager.service.ServicesHandler;
import com.flow.manager.service.PlaylistService;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;

@Service
public class PlaylistServiceImpl implements PlaylistService {

	private static final Logger logger =
			LoggerFactory.getLogger(PlaylistServiceImpl.class);


    @Autowired
	private FileServiceImpl fileService;
	
	@Autowired
	private ServicesHandler servicesHandler;

	@Override
    public PlaylistDto create(PlaylistDto playlist, String userId) {

        if( playlist.getPlatform() == null) playlist.setPlatform("youtube") ;

        switch(playlist.getPlatform()){
            case AppConstants.YOUTUBE:
                playlist = createYoutubePlaylist(playlist, userId);
                break;
            default:
                break;
        }
        return playlist;
    }

    private PlaylistDto createYoutubePlaylist(PlaylistDto playlist, String userId) {

		try {
			retrieveAndSetPlaylistTitle(playlist);

            playlist = checkPlaylistExist(playlist);

			if(StringUtils.isEmpty(playlist.getUrl()) && !playlist.doesExist()){

                logger.info("creating playlist: " + playlist.getTitle());

                List<String> videoList = fileService.loadPlaylistItemsFromDriveFile();

                String playListId = insertPlaylist(playlist.getTitle());

				if(videoList!=null) {
					videoList.forEach(videoUrl->{
						try {
							String videoId = getVideoId(videoUrl);
							insertPlaylistItem(playListId, videoId);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					});
				}
				playlist.setUrl(playListId);

			}

		} catch (GoogleJsonResponseException e) {
			logger.error("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			logger.error("Throwable: " + t.getMessage());
			t.printStackTrace();
		}

		return playlist;
	}

    private void retrieveAndSetPlaylistTitle(PlaylistDto playlist) throws IOException {
	    String playlistTitle = fileService.retrievePlaylistTitleFromDriveFile();
        playlist.setTitle(playlistTitle);
    }

    private PlaylistDto checkPlaylistExist(PlaylistDto playlist) throws IOException {

        playlist.setExist(false);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("part", "snippet");
        parameters.put("channelId", AppProperties.YOUTUBE_CHANNEL_ID);
        parameters.put("maxResults", "20");

        try{
           YouTube.Playlists.List playlistsListByChannelIdRequest = ServicesHandler.youtube.playlists().list(parameters.get("part").toString());
           playlistsListByChannelIdRequest.setChannelId(parameters.get("channelId").toString());
           playlistsListByChannelIdRequest.setMaxResults(Long.parseLong(parameters.get("maxResults").toString()));

           PlaylistListResponse response = playlistsListByChannelIdRequest.execute();

           Optional<Playlist> result = response.getItems().stream()
                   .filter(item -> item.getSnippet().getTitle().equals(playlist.getTitle()))
                   .findFirst();
           if(result.isPresent()){
               playlist.setExist(true);
               playlist.setUrl(result.get().getId());
           }

       }catch(Exception ex){
           ex.printStackTrace();
       }

        return playlist;
    }


    private String getVideoId(String videoUrl) throws URISyntaxException {
		String videoId = null;
		List<NameValuePair> params = null;
		URI videoUri = new URI(videoUrl);
		params = URLEncodedUtils.parse(videoUri, "UTF-8");
		
		if(params.size() > 0) {
			for (NameValuePair param : params) {
				if(param.getName().equals("v")) {
					videoId = param.getValue();
					break;
				}
			}
		}else {
			String path = videoUri.getPath();
			if(StringUtils.isNotEmpty(path)) {
				videoId = path.substring(1);
			}
		}

		return videoId;
	}

	/**
	 * Create a playlist and add it to the authorized account.
	 */
	private String insertPlaylist(String playListTitle) throws IOException {

		PlaylistSnippet playlistSnippet = new PlaylistSnippet();
		playlistSnippet.setTitle(playListTitle);
		playlistSnippet.setDescription(playListTitle);
		PlaylistStatus playlistStatus = new PlaylistStatus();
		playlistStatus.setPrivacyStatus(AppConstants.PLAYLIST_PUBLIC);

		Playlist youTubePlaylist = new Playlist();
		youTubePlaylist.setSnippet(playlistSnippet);
		youTubePlaylist.setStatus(playlistStatus);

		YouTube.Playlists.Insert playlistInsertCommand =
                ServicesHandler.youtube.playlists().insert("snippet,status", youTubePlaylist);
		Playlist playlistInserted = playlistInsertCommand.execute();

		if(logger.isDebugEnabled()){
            logger.debug("New Playlist name: " + playlistInserted.getSnippet().getTitle());
            logger.debug(" - Privacy: " + playlistInserted.getStatus().getPrivacyStatus());
            logger.debug(" - Description: " + playlistInserted.getSnippet().getDescription());
            logger.debug(" - Posted: " + playlistInserted.getSnippet().getPublishedAt());
            logger.debug(" - Channel: " + playlistInserted.getSnippet().getChannelId() + "\n");
        }

		return playlistInserted.getId();
	}

	/**
	 * Create a playlist item with the specified video ID and add it to the
	 * specified playlist.
	 *
	 * @param playlistId assign to newly created playlistitem
	 * @param videoId    YouTube video id to add to playlistitem
	 */
	private String insertPlaylistItem(String playlistId, String videoId) throws IOException {

		// Define a resourceId that identifies the video being added to the
		// playlist.
		ResourceId resourceId = new ResourceId();
		resourceId.setKind("youtube#video");
		resourceId.setVideoId(videoId);

		// Set fields included in the playlistItem resource's "snippet" part.
		PlaylistItemSnippet playlistItemSnippet = new PlaylistItemSnippet();
		playlistItemSnippet.setTitle("First video in the test playlist");
		playlistItemSnippet.setPlaylistId(playlistId);
		playlistItemSnippet.setResourceId(resourceId);

		// Create the playlistItem resource and set its snippet to the
		// object created above.
		PlaylistItem playlistItem = new PlaylistItem();
		playlistItem.setSnippet(playlistItemSnippet);

		// Call the API to add the playlist item to the specified playlist.
		// In the API call, the first argument identifies the resource parts
		// that the API response should contain, and the second argument is
		// the playlist item being inserted.
		YouTube.PlaylistItems.Insert playlistItemsInsertCommand =
                ServicesHandler.youtube.playlistItems().insert("snippet,contentDetails", playlistItem);
		PlaylistItem returnedPlaylistItem = playlistItemsInsertCommand.execute();

		// Print data from the API response and return the new playlist
		// item's unique playlistItem ID.

		logger.info("New PlaylistItem name: " + returnedPlaylistItem.getSnippet().getTitle());
		logger.info(" - Video id: " + returnedPlaylistItem.getSnippet().getResourceId().getVideoId());
		logger.info(" - Posted: " + returnedPlaylistItem.getSnippet().getPublishedAt());
		logger.info(" - Channel: " + returnedPlaylistItem.getSnippet().getChannelId());

		return returnedPlaylistItem.getId();

	}

}
