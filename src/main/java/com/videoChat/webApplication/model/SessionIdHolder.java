package com.videoChat.webApplication.model;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class SessionIdHolder {

    private Map<String, String> emailToSessionIdMap = new HashMap<>();

    public void storeSessionId(String email, String sessionId) {
        emailToSessionIdMap.put(email, sessionId);
    }

    public String getSessionIdByEmail(String email) {
        return emailToSessionIdMap.get(email);
    }

    public void removeSessionId(String email) {
        emailToSessionIdMap.remove(email);
    }

}