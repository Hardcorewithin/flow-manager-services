package com.flow.manager;

import com.flow.manager.bot.BotInitializer;
import com.flow.manager.bot.FlowManagerBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableOAuth2Client
public class FlowManagerApplication {

    private static BotInitializer botInitializer;

    @Autowired
    private BotInitializer _botInit;

    @PostConstruct
    private void initBotInitializer () {
        botInitializer = this._botInit;
    }

    public static void main(String[] args) {
		SpringApplication.run(FlowManagerApplication.class, args);
        botInitializer.init();
	}
}
