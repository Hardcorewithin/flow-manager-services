package com.flow.manager.service;

import com.flow.manager.constants.FlowManagerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.flow.manager.dto.PlaylistDTO;

@Service
public class PlaylistServiceHandler {

	@Autowired
	private PlaylistService ytPlaylistService;

	public PlaylistDTO create(PlaylistDTO playlist) {

		switch(playlist.getPlatform()){
			case FlowManagerConstants.YOUTUBE:
                playlist = ytPlaylistService.create(playlist);
				break;
			default:
				break;
		}
		return playlist;
	}

}
