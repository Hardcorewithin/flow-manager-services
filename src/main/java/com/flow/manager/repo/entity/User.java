package com.flow.manager.repo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    public String id;

    public String username;
    public String chatId;
    public String randomId;

    public User() {}

    public User(String username, String chatId) {
        this.username = username;
        this.chatId = chatId;
    }
    
    public User(String username, String chatId, String randomId) {
        this.username = username;
        this.chatId = chatId;
        this.randomId = randomId;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getRandomId() {
        return randomId;
    }

    public void setRandomId(String randomId) {
        this.randomId = randomId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", chatId='" + chatId + '\'' +
                ", randomId='" + randomId + '\'' +
                '}';
    }
}
