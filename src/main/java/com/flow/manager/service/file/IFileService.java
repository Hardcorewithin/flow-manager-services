package com.flow.manager.service.file;

import java.io.IOException;
import java.util.List;

import com.flow.manager.dto.FileDTO;

public interface IFileService {

	public List<String> loadPlaylistItems(FileDTO fileDTO) throws IOException;
	
	public List<String> loadPlaylistItems() throws IOException;

	public void printCurrentPlayList() throws IOException;
	
	public void printFileList() throws IOException;
	
}
