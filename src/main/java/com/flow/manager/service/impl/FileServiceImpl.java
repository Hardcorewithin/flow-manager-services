package com.flow.manager.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.flow.manager.constants.AppProperties;
import com.flow.manager.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flow.manager.service.ServicesHandler;
import com.google.api.client.util.Charsets;
import com.google.api.services.drive.Drive.Files.Export;

@Service
public class FileServiceImpl implements FileService {

	private static final Logger logger =
			LoggerFactory.getLogger(FileServiceImpl.class);
	
	@Autowired
	private ServicesHandler servicesHandler;

	public void printCurrentPlayList() throws IOException {

		Export s = ServicesHandler.drive.files().export(AppProperties.DRIVE_PLAYLIST_FILE_ID, "text/csv");
		InputStream in=s.executeMediaAsInputStream();
		InputStreamReader isr=new InputStreamReader(in,Charsets.UTF_8);
		BufferedReader br = new BufferedReader(isr);
		String line = null;

		while((line = br.readLine()) != null) {
			logger.info(line);
		}
	}

    public String retrievePlaylistTitleFromDriveFile() throws IOException {
        Export s = ServicesHandler.drive.files().export(AppProperties.DRIVE_PLAYLIST_FILE_ID, "text/csv");
        InputStream in=s.executeMediaAsInputStream();
        InputStreamReader isr=new InputStreamReader(in,Charsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        String playlistTitle = "";

        while((line = br.readLine()) != null) {
            if(line.contains("Title") || line.contains("Artist") || line.contains("Youtube")) {
                //skip first line
                continue;
            } else {
                String[] res = line.split(",");
                playlistTitle = res[0].toString();
                break;
            }
        }

        return playlistTitle;
    }

    public List<String> loadPlaylistItemsFromDriveFile() throws IOException {
		List<String> playlistItems = new ArrayList<String>();

		Export s = ServicesHandler.drive.files().export(AppProperties.DRIVE_PLAYLIST_FILE_ID, "text/csv");
		InputStream in=s.executeMediaAsInputStream();
		InputStreamReader isr=new InputStreamReader(in,Charsets.UTF_8);
		BufferedReader br = new BufferedReader(isr);
		String line = null;

		while((line = br.readLine()) != null) {
            if(line.contains("Title") || line.contains("Artist") || line.contains("Youtube")) {
                //skip first line
                continue;
            } else {
                String[] res = line.split(",");
                if(res.length >= 3) { //line with title
                    String url = res[2].toString();
                    playlistItems.add(url);
                }
                if(res.length == 2) {
                    String url = res[1].toString();
                    playlistItems.add(url);
                }else if(res.length == 1) {
                    String url = res[0].toString();
                    playlistItems.add(url);
                }
            }
		}
		
		logger.info("playlist items successfully loaded");

		return playlistItems;
	}

}