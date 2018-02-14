package com.flow.manager.bot;

import com.flow.manager.constants.AppProperties;
import com.flow.manager.dto.PlaylistDTO;
import com.flow.manager.service.impl.AuthServiceImpl;
import com.flow.manager.service.ServicesHandler;
import com.flow.manager.service.PlaylistServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;

import static java.lang.Math.toIntExact;

@Component
public class FlowManagerBot extends TelegramLongPollingBot {

    private static final Logger logger =
            LoggerFactory.getLogger(FlowManagerBot.class);

    @Autowired
    private ServicesHandler servicesHandler;

    @Autowired
    private PlaylistDTO playlistDTO;

    @Autowired
    private PlaylistServiceHandler playlistServiceHandler;

    static {
        ApiContextInitializer.init();
    }

    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            if (update.getMessage().getText().equals("/start")) {

                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Clicca su \"Authorize me\" per autorizzare \"FlowManagerBot\"");

                String authorizeUrl = null;
                try {
                    authorizeUrl = AuthServiceImpl.authorize();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                message.setText(authorizeUrl);
                /*InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton()
                        .setText(authorizeUrl)
                        .setCallbackData("authorization_callback"));
                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);*/
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("/playlist")) {

                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Youtube");
                try{

                    if(servicesHandler.getYouTubeService() != null){

                        String playlistTitle= "FlowTestTelegram";
                        String platform = "youtube";
                        playlistDTO.setPlatform(platform);//get the platform
                        playlistDTO.setTitle(playlistTitle);
                        playlistDTO = playlistServiceHandler.create(playlistDTO);

                        message.setChatId(chat_id)
                                .setText("playlist creata: " + playlistDTO.getUrl());
                    }else{
                        message.setChatId(chat_id)
                                .setText("Operazione non effettuata. " +
                                        "Riprova a rieffettuare l'autorizzazione dal comando /start.");
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }


                /*SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Seleziona la piattaforma della playlist");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton()
                        .setText("Youtube")
                        .setCallbackData("playlist_youtube"));
                rowInline.add(new InlineKeyboardButton()
                        .setText("Deezer")
                        .setCallbackData("playlist_deezer"));
                rowInline.add(new InlineKeyboardButton()
                        .setText("Spotify")
                        .setCallbackData("playlist_spotify"));
                rowInline.add(new InlineKeyboardButton()
                        .setText("Tutte")
                        .setCallbackData("playlist_all"));
                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);*/

                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else{
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Comando sconosciuto");
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery()) {

            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if(call_data.equals("authorization_callback")){
                String answer = "Updated message text";
                EditMessageText new_message = new EditMessageText()
                        .setChatId(chat_id)
                        .setMessageId(toIntExact(message_id))
                        .setText(answer);
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (call_data.contains("playlist")) {
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("");
                try{
                    if(servicesHandler.getYouTubeService() != null){

                        String platform = call_data.split("_")[1];
                        playlistDTO.setPlatform(platform);//get the platform
                        playlistDTO = playlistServiceHandler.create(playlistDTO);

                        message.setChatId(chat_id)
                                .setText("playlist creata: " + playlistDTO.getUrl());
                    }else{
                        message.setChatId(chat_id)
                                .setText("Operazione non effettuata. " +
                                        "Riprova a rieffettuare l'autorizzazione dal comando /start.");
                    }
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

/*
    private void log(Update update) {

        String user_first_name = update.getMessage().getChat().getFirstName();
        String user_last_name = update.getMessage().getChat().getLastName();
        String user_username = update.getMessage().getChat().getUserName();
        long chat_id = update.getMessage().getChatId();

        long user_id = update.getMessage().getChat().getId();
        String message_text = update.getMessage().getText();
        String bot_answer = message_text;

        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Chat id: " +chat_id+ " - Message from " + user_first_name + " " + user_last_name + ". (id = " + user_id + " - username: " + user_username + " ) \n Text - " + message_text);
        System.out.println("Bot answer: \n Text - " + bot_answer);
    }*/

    @Override
    public String getBotUsername() {
        return AppProperties.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return AppProperties.BOT_TOKEN;
    }

}

