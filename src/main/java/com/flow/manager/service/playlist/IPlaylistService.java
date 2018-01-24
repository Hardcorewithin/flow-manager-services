package com.flow.manager.service.playlist;

import org.springframework.stereotype.Service;

import com.flow.manager.dto.PlaylistDTO;

@Service
public interface IPlaylistService {

	public PlaylistDTO create(PlaylistDTO playlist);
}
