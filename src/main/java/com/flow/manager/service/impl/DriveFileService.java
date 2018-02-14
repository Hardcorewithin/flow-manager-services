package com.flow.manager.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.flow.manager.dto.FileDTO;
import com.flow.manager.service.ServicesHandler;
import com.google.api.client.util.Charsets;
import com.google.api.services.drive.Drive.Files.Export;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@Service
public class DriveFileService {

	@Value("${drive.playlist.file.id}")
	private String playlistDriveFileId;
	
	private static final Logger logger =
			LoggerFactory.getLogger(DriveFileService.class);
	
	@Autowired
	private ServicesHandler servicesHandler;
	
	/**
	 * Print a file's metadata.
	 *
	 * @param service Drive API service instance.
	 * @param fileId ID of the file to print metadata for.
	 * @throws IOException 
	 */
/*	public void printFileList() throws IOException {
		// Print the names and IDs for up to 10 files.
		FileList result = servicesHandler.getDriveService().files().list()
				.setPageSize(10)
				.setFields("nextPageToken, files(id, name)")
				.execute();
		List<File> files = result.getFiles();
		if (files == null || files.size() == 0) {
			System.out.println("No files found.");
		} else {
			System.out.println("Files:");
			for (File file : files) {
				System.out.printf("%s (%s)\n", file.getName(), file.getId());
			}
		}
	}

	public void printCurrentPlayList() throws IOException {
		Export s = servicesHandler.getDriveService().files().export(playlistDriveFileId, "text/csv");
		InputStream in=s.executeMediaAsInputStream();
		InputStreamReader isr=new InputStreamReader(in,Charsets.UTF_8);
		BufferedReader br = new BufferedReader(isr);
		String line = null;

		while((line = br.readLine()) != null) {
			logger.info(line);
		}
		
	}*/
	
	public List<String> loadPlaylistItems(FileDTO fileDTO) throws IOException {
		List<String> playlistItems = new ArrayList<String>();

		Export s = servicesHandler.getDriveService().files().export(fileDTO.getFileId(), "text/csv");
		InputStream in=s.executeMediaAsInputStream();
		InputStreamReader isr=new InputStreamReader(in,Charsets.UTF_8);
		BufferedReader br = new BufferedReader(isr);
		String line = null;

		while((line = br.readLine()) != null) {
			String[] res = line.split(",");
			if(res.length > 1) {
				String url = res[1].toString();
				playlistItems.add(url);
			}else if(res.length == 1) {
				String url = res[0].toString();
				playlistItems.add(url);
			}
		}
		
		logger.info("playlist items successfully loaded");

		return playlistItems;
	}

	public List<String> loadPlaylistItems() throws IOException {
		FileDTO fileDto = new FileDTO();
		fileDto.setFileId(playlistDriveFileId);
		return loadPlaylistItems(fileDto);
	}
	

}