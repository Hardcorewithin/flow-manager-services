package com.flow.manager.dto;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class PlaylistDTO implements Serializable{

	private String title;
	private String url;
    private String platform;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }
}
