package com.flow.manager.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.youtube.YouTube;
import org.springframework.stereotype.Service;

import com.flow.manager.dto.PlaylistDTO;

@Service
public interface PlaylistService {

	public PlaylistDTO create(PlaylistDTO playlist, String userId);
}
