package com.flow.manager.service;

import com.flow.manager.dto.PlaylistDto;
import org.springframework.stereotype.Service;

@Service
public interface PlaylistService {

	public PlaylistDto create(PlaylistDto playlist, String userId);
}
