package com.videoChat.webApplication.model;

import java.security.PrivateKey;

public class ResonseMessage {
    private  String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ResonseMessage(String content) {
        this.content = content;
    }
}
