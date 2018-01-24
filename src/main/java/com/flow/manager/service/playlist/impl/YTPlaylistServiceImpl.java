package com.flow.manager.service.playlist.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flow.manager.dto.PlaylistDTO;
import com.flow.manager.service.auth.YouTubeAuth;
import com.flow.manager.service.file.IFileService;
import com.flow.manager.service.playlist.IPlaylistService;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.PlaylistStatus;
import com.google.api.services.youtube.model.ResourceId;

@Service
public class YTPlaylistServiceImpl implements IPlaylistService{

	private static final Logger logger =
			LoggerFactory.getLogger(YTPlaylistServiceImpl.class);
	
	private static final String PLAYLIST_PRIVATE = "private";
	private static final String PLAYLIST_PUBLIC = "public";
	
	@Autowired
	private IFileService fileService;
	
	@Autowired
	private YouTubeAuth youtubeAuth;
	
	
	@Override
	public PlaylistDTO create(PlaylistDTO playlist) {
		
		logger.info("creating playlist: " + playlist.getTitle());
		
		try {
			
			//Load video urls from the file, if successfull go on
			List<String> videoList = fileService.loadPlaylistItems();

			// Create a new, private playlist in the authorized user's channel.
			String playlistTitle = playlist.getTitle();
			String playListId = insertPlaylist(playlistTitle);

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

		// This code constructs the playlist resource that is being inserted.
		// It defines the playlist's title, description, and privacy status.
		PlaylistSnippet playlistSnippet = new PlaylistSnippet();
		playlistSnippet.setTitle(playListTitle);
		//TODO: to be specified as input
		playlistSnippet.setDescription(playListTitle);
		//TODO: to be specified as input
		PlaylistStatus playlistStatus = new PlaylistStatus();
		playlistStatus.setPrivacyStatus(PLAYLIST_PUBLIC);

		Playlist youTubePlaylist = new Playlist();
		youTubePlaylist.setSnippet(playlistSnippet);
		youTubePlaylist.setStatus(playlistStatus);

		// Call the API to insert the new playlist. In the API call, the first
		// argument identifies the resource parts that the API response should
		// contain, and the second argument is the playlist being inserted.
		YouTube.Playlists.Insert playlistInsertCommand =
				youtubeAuth.getClient().playlists().insert("snippet,status", youTubePlaylist);
		Playlist playlistInserted = playlistInsertCommand.execute();

		// Print data from the API response and return the new playlist's
		// unique playlist ID.
		logger.info("New Playlist name: " + playlistInserted.getSnippet().getTitle());
		logger.info(" - Privacy: " + playlistInserted.getStatus().getPrivacyStatus());
		logger.info(" - Description: " + playlistInserted.getSnippet().getDescription());
		logger.info(" - Posted: " + playlistInserted.getSnippet().getPublishedAt());
		logger.info(" - Channel: " + playlistInserted.getSnippet().getChannelId() + "\n");

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
				youtubeAuth.getClient().playlistItems().insert("snippet,contentDetails", playlistItem);
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
