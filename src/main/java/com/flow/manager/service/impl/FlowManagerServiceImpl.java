package com.flow.manager.service.impl;

import com.flow.manager.constants.AppProperties;
import com.flow.manager.constants.FlowManagerConstants;
import com.flow.manager.repo.entity.User;
import com.flow.manager.repo.UserRepository;
import com.flow.manager.service.FlowManagerService;
import com.flow.manager.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FlowManagerServiceImpl implements FlowManagerService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public void startRequest(String code) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(FlowManagerConstants.TELEGRAM_ME_URI + AppProperties.BOT_NAME + "?start=" + code)
                .queryParam("start", code);

        RestTemplate restTemplate = new RestTemplate();
        Object response = restTemplate.getForObject(builder.toUriString(), Object.class);
    }


    @Override
    public String saveRandomUserId(String username){

        User user = new User();
        user.setUsername(username);

        String randomId="";
        boolean exit = false;
        while (!exit){
            randomId = Utils.generateRandomString();
            exit = !userRepository.findByRandomId(randomId);
        }
        user.setRandomId(randomId);
        User userCreated = userRepository.save(user);

        return userCreated.getId();
    }


}

