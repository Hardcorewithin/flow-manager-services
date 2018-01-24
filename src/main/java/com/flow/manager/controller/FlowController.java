package com.flow.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.flow.manager.dto.PlaylistDTO;
import com.flow.manager.dto.ServiceInfoDTO;
import com.flow.manager.service.auth.DriveAuth;
import com.flow.manager.service.auth.YouTubeAuth;
import com.flow.manager.service.playlist.PlaylistService;
import com.google.api.services.drive.Drive;
import com.google.api.services.youtube.YouTube;


@CrossOrigin
@RestController
@RequestMapping("/flow")
public class FlowController {

	@Autowired
	private PlaylistService playlistService;

	@Autowired
	private ServiceInfoDTO serviceInfoDTO;

	@Autowired
	private PlaylistDTO playlistDTO;

	@Autowired
	private YouTubeAuth youtubeAuth;

	@Autowired
	private DriveAuth driveAuth;

	private YouTube youtubeClient;

	private Drive driveClient;

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public ResponseEntity<String> getInfo() { 
		return new ResponseEntity<String>("Flow Manager is running!", HttpStatus.OK);
	}

	@RequestMapping(value = "/login/youtube", method = RequestMethod.GET)
	public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
		return new RedirectView(youtubeAuth.authorize());
	}

	@RequestMapping(value = "/login/youtubeCallback", method = RequestMethod.GET, params = "code")
	public ResponseEntity<String> youtubeOauth2Callback(@RequestParam(value = "code") String code) {
		
		String res = "";
		try {
			youtubeClient = youtubeAuth.getService(code);
			res="YOUTUBE CLIENT OK";
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@RequestMapping(value = "/login/driveCallback", method = RequestMethod.GET, params = "code")
	public ResponseEntity<String> driveOauth2Callback(@RequestParam(value = "code") String code) {
		
		String res = "";
		if(driveClient == null){
			res="DRIVE CLIENT NULL";
			return new ResponseEntity<String>(res, HttpStatus.OK);
		}
		
		try {
			driveClient = driveAuth.getService(code);
			res = "DRIVE CLIENT OK!";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@RequestMapping(value = "/playlist/youtube/create", method = RequestMethod.POST)
	public ResponseEntity<String> createPlaylist(@RequestParam(value = "title", defaultValue = "") String title, @RequestParam(value = "platform", defaultValue = "") String platform) {

		String res = "";
		if(youtubeClient == null){
			res="YOUTUBE CLIENT NULL";
			return new ResponseEntity<String>(res, HttpStatus.OK);
		}
		
		playlistDTO.setTitle(title);
		serviceInfoDTO.setPlatform(platform);
		res = playlistService.create(serviceInfoDTO,playlistDTO);
		
		return new ResponseEntity<String>(res, HttpStatus.OK);
	}
}
