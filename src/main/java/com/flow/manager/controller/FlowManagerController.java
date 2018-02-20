package com.flow.manager.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flow.manager.bot.FlowManagerBot;
import com.flow.manager.constants.AppProperties;
import com.flow.manager.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.flow.manager.service.impl.AuthServiceImpl;


@CrossOrigin
@RestController
@RequestMapping("/flow-manager")
public class FlowManagerController {

    private static final Logger logger =
            LoggerFactory.getLogger(FlowManagerController.class);

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private FlowManagerBot flowManagerBot;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseEntity<String> getInfo() {
        return new ResponseEntity<String>("Flow Manager is running!", HttpStatus.OK);
    }

    @RequestMapping(value = "/oauth2Callback", method = RequestMethod.GET, params = "code")
    public void oauth2Callback(@RequestParam(value = "code") String code, @RequestParam(value = "state") String state,
                               HttpServletResponse response,
                               HttpServletRequest request) {

        try {
            authService.storeCredentials(code, state);
            flowManagerBot.authOk(state);

            response.sendRedirect(AppConstants.TELEGRAM_ME_URI + AppProperties.BOT_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
