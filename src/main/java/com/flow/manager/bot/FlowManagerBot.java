package com.flow.manager.bot;

import com.flow.manager.constants.AppProperties;
import com.flow.manager.dto.PlaylistDto;
import com.flow.manager.service.PlaylistService;
import com.flow.manager.service.impl.AuthServiceImpl;
import com.flow.manager.service.ServicesHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FlowManagerBot extends TelegramLongPollingBot {

    private static final Logger logger =
            LoggerFactory.getLogger(FlowManagerBot.class);

    @Autowired
    private ServicesHandler servicesHandler;

    @Autowired
    private PlaylistDto playlistDto;

    @Autowired
    private PlaylistService playlistServiceImpl;

    static {
        ApiContextInitializer.init();
    }

    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {

            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            String userId = update.getMessage().getFrom().getUserName();

            if (update.getMessage().getText().equals("/start")) {

                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id)
                            .setText("Clicca su \"Authorize me\" per autorizzare \""+AppProperties.BOT_NAME+"\"");

                    String authorizeUrl = null;
                    try {
                        authorizeUrl = AuthServiceImpl.authorize(userId, chat_id);
                    } catch (Exception e) {
                        logger.error("Errore durante la creazione dell'url per l'autorizazzione",e);
                    }

                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();

                    rowInline.add(new InlineKeyboardButton()
                            .setText("Authorize me")
                            .setUrl(authorizeUrl));

                    // Set the keyboard to the markup
                    rowsInline.add(rowInline);

                    // Add it to the message
                    markupInline.setKeyboard(rowsInline);
                    message.setReplyMarkup(markupInline);

                    try {
                        execute(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        logger.error("Errore durante l'invio del messaggio /start",e);
                    }

            } else if (message_text.equals("/playlist")) {

                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Youtube");
                try{
                    if(servicesHandler.initServices(userId)){

                        playlistDto = new PlaylistDto();

                        String textMessage ="";
                        //playlistDto.setPlatform(platform);//TODO: To change when there will be more than 1 platform for generating the playlist
                        playlistDto = playlistServiceImpl.create(playlistDto);

                        if(playlistDto.doesExist()){
                            textMessage = "playlist gia esistente: " + playlistDto.getUrl();
                        }else if(!playlistDto.doesExist()){
                            textMessage = "playlist creata: " + playlistDto.getUrl();
                        }
                        else{
                            textMessage = "errore durante la creazione della playlist";
                        }

                        message.setChatId(chat_id)
                                .setText(textMessage);
                    }else{
                        message.setChatId(chat_id)
                                .setText("Errore durante il recupero delle credenziali." +
                                        "Riprova ad effettuare l'autorizzazione dal comando /start.");
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
                        .setCallbackData("playlist_callback_youtube"));
                rowInline.add(new InlineKeyboardButton()
                        .setText("Deezer")
                        .setCallbackData("playlist_callback_deezer"));
                rowInline.add(new InlineKeyboardButton()
                        .setText("Spotify")
                        .setCallbackData("playlist_callback_spotify"));
                rowInline.add(new InlineKeyboardButton()
                        .setText("Tutte")
                        .setCallbackData("playlist_callback_all"));
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
            String userId = update.getMessage().getFrom().getUserName();

            if (call_data.contains("playlist_callback")) {
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("");
                try{

                    if(servicesHandler.initServices(userId)){
                        playlistDto = new PlaylistDto();

                        String platform = call_data.split("_")[1];
                        playlistDto.setPlatform(platform);//get the platform
                        playlistDto = playlistServiceImpl.create(playlistDto);

                        message.setChatId(chat_id)
                                .setText("playlist creata: " + playlistDto.getUrl());
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

    /**
     * Send a message when the authorization went fine
     *
     * @param state
     */
    public void authOk(String state){

        String userId = state.split("#")[0];
        Long chatId = Long.valueOf(state.split("#")[1]);

        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chatId)
                .setText("Autorizzazione effettuata con successo (\'"+userId+"\'), ora puoi usare " + AppProperties.BOT_NAME);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Errore durante l'invio del messaggio /start", e);
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

