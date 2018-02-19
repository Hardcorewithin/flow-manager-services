package com.flow.manager.dto;

import java.io.Serializable;

import com.flow.manager.constants.FlowManagerConstants;
import org.springframework.stereotype.Component;

@Component
public class PlaylistDTO implements Serializable{

	private String title;
	private String url;
    private String platform;
    private boolean exists;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = FlowManagerConstants.YOUTUBE_PLAYLIST_BASE_URL + url;
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

    public boolean doesExist() {
        return exists;
    }

    public void setExist(boolean exists) {
        this.exists = exists;
    }
}
