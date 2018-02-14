package com.flow.manager.service;

import org.springframework.stereotype.Service;

import com.flow.manager.dto.PlaylistDTO;

@Service
public interface PlaylistService {

	public PlaylistDTO create(PlaylistDTO playlist);
}
