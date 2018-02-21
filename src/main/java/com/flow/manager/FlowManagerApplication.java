package com.flow.manager;

import com.flow.manager.bot.BotInitializer;
import com.flow.manager.repo.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableOAuth2Client
public class FlowManagerApplication {

    private static final Logger logger =
            LoggerFactory.getLogger(FlowManagerApplication.class);

    private static BotInitializer botInitializer;

    @Autowired
    private BotInitializer _botInit;

    @PostConstruct
    private void initBotInitializer() {

        botInitializer = this._botInit;
    }

    public static void main(String[] args) {
        SpringApplication.run(FlowManagerApplication.class, args);
        botInitializer.init();
    }

    @Service
    class PingDatabase {

        @Autowired
        private TokenRepository tokenRepository;

        /**
         * avoid shutting down mLab connection by inactivity
         */
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture scheduledFuture =
                scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                                                                 @Override
                                                                 public void run() {
                                                                     logger.info("Ping database");
                                                                     tokenRepository.findByUserId("ping");
                                                                 }
                                                             },
                        1, 10,
                        TimeUnit.MINUTES
                );

    }

}
