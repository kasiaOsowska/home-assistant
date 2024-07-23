package com.github.kasiaOsowska.homeassistant.library.service;

import com.github.kasiaOsowska.homeassistant.library.model.AppUser;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    private static final Map<String, AppUser> sessions = new ConcurrentHashMap<>();

    public String createSession(AppUser user) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user);
        return sessionId;
    }

    public static AppUser getUserBySessionId(String sessionId) {
        return sessions.get(sessionId);
    }
}
