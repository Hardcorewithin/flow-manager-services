package com.flow.manager.service.playlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.flow.manager.constants.ApplicationConstants;
import com.flow.manager.dto.PlaylistDTO;
import com.flow.manager.dto.ServiceInfoDTO;

@Service
public class PlaylistService {

	@Autowired
	private IPlaylistService ytPlaylistService;

	public String create(ServiceInfoDTO service, PlaylistDTO playlist) {
		switch(service.getPlatform()){
			case ApplicationConstants.YOUTUBE:
					ytPlaylistService.create(playlist);
				break;
			default:
				break;
		}
		return null;
	}

}
