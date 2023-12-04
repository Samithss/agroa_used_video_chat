package com.videoChat.webApplication.services;

import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;
public class UserHandshakeHandler extends DefaultHandshakeHandler {
    private final Logger LOG=LoggerFactory.getLogger(UserHandshakeHandler.class);
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        String randomID = UUID.randomUUID().toString();
        LOG.info("User with userID '{}' opened the page" + randomID);
        return new UserPrincipal(randomID);
    }
}
