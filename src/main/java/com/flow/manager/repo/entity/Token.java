package com.flow.manager.repo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "tokens")
public class Token {

    private String refreshToken;
    private String accessToken;

    private Long chatId;

    @Id
    private String userId;

    public Token(){}

    public Token(String refreshToken, String accessToken, String userId, Long chatId) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.userId = userId;
        this.chatId = chatId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "Token{" +
                "refreshToken='" + refreshToken + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", chatId=" + chatId +
                ", userId='" + userId + '\'' +
                '}';
    }
}
