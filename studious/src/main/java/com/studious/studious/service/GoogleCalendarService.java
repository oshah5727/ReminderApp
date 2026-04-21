package com.studious.studious.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.json.gson.GsonFactory;
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
     * Exchange authorization code for tokens and store them.
     * Returns the authenticated user's email extracted from the ID token.
     */
    public String handleCallback(String code) throws IOException {
        GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                .setRedirectUri(config.getRedirectUri())
                .execute();

        // Extract user identity from the ID token returned by Google
        GoogleIdToken idToken = GoogleIdToken.parse(GsonFactory.getDefaultInstance(), tokenResponse.getIdToken());
        String userId = idToken.getPayload().getEmail();

        // Store the credential keyed by the user's email
        flow.createAndStoreCredential(tokenResponse, userId);
        return userId;
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
        return null;
    }

    /**
     * Disconnect Google Calendar for a user
     */
    public void disconnect(String userId) throws IOException {
        flow.getCredentialDataStore().delete(userId);
    }
}
