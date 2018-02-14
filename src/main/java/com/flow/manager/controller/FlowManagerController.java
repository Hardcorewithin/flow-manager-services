package com.flow.manager.controller;


import javax.servlet.http.HttpServletResponse;

import com.flow.manager.constants.AppProperties;
import com.flow.manager.constants.FlowManagerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.flow.manager.service.impl.AuthServiceImpl;


@CrossOrigin
@RestController
@RequestMapping("/flow-manager")
public class FlowManagerController {

    @Autowired
    private AuthServiceImpl authService;

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public ResponseEntity<String> getInfo() { 
		return new ResponseEntity<String>("Flow Manager is running!", HttpStatus.OK);
	}

	@RequestMapping(value = "/oauth2Callback", method = RequestMethod.GET, params = "code")
	public void oauth2Callback(@RequestParam(value = "code") String code, HttpServletResponse response) {

		try {
            authService.storeCredentials(code);
			response.sendRedirect(FlowManagerConstants.TELEGRAM_ME_URI+AppProperties.BOT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
