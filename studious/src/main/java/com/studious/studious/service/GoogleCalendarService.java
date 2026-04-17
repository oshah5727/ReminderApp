package com.studious.studious.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.studious.studious.config.GoogleOAuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GoogleCalendarService {

    @Autowired
    private GoogleAuthorizationCodeFlow flow;

    @Autowired
    private GoogleOAuthConfig config;

    /**
     * Generate the Google OAuth authorization URL
     */
    public String getAuthorizationUrl() {
        GoogleAuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri(config.getRedirectUri());
        return authorizationUrl.build();
    }

    /**
     * Exchange authorization code for tokens and store them
     */
    public void handleCallback(String code, String userId) throws IOException {
        GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                .setRedirectUri(config.getRedirectUri())
                .execute();

        // Store the credential for the user
        flow.createAndStoreCredential(tokenResponse, userId);
    }

    /**
     * Check if user has connected their Google Calendar
     */
    public boolean isConnected(String userId) throws IOException {
        return flow.loadCredential(userId) != null;
    }

    /**
     * Get Google Calendar events for a user
     * TODO: Implement when ready to fetch events
     */
    public List<Event> getEvents(String userId) throws IOException {
        // This is a placeholder - will implement full event fetching later
        // Calendar service = new Calendar.Builder(
        //     new NetHttpTransport(),
        //     GsonFactory.getDefaultInstance(),
        //     flow.loadCredential(userId))
        //     .setApplicationName("Studious")
        //     .build();
        // 
        // Events events = service.events().list("primary")
        //     .setMaxResults(100)
        //     .setTimeMin(new DateTime(System.currentTimeMillis()))
        //     .setOrderBy("startTime")
        //     .setSingleEvents(true)
        //     .execute();
        // 
        // return events.getItems();

        return null;
    }

    /**
     * Disconnect Google Calendar for a user
     */
    public void disconnect(String userId) throws IOException {
        flow.getCredentialDataStore().delete(userId);
    }
}
