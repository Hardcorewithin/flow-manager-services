package com.flow.manager.service;

import com.google.api.services.drive.Drive;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface FileService {

    public List<String> loadPlaylistItemsFromDriveFile() throws IOException;

    public void printCurrentPlayList() throws IOException;

    public String retrievePlaylistTitleFromDriveFile() throws IOException;
}
