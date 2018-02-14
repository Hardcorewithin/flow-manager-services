package com.flow.manager.repo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tokens")
public class Token {

/*    @Id
    public String id;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/

    private String refreshToken;
    private String accessToken;

    @Id
    private String userId;

    public Token(){}

    public Token(String refreshToken, String accessToken, String userId) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.userId = userId;
    }

    public Token(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
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

    @Override
    public String toString() {
        return "Token{" +
                ", refreshToken='" + refreshToken + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
