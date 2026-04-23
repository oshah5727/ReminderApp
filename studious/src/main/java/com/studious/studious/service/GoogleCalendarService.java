package com.studious.studious.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.studious.studious.config.GoogleOAuthConfig;
import com.studious.studious.model.CalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

        /** Extract user identity from the ID token returned by Google */
        GoogleIdToken idToken = GoogleIdToken.parse(GsonFactory.getDefaultInstance(), tokenResponse.getIdToken());
        String userId = idToken.getPayload().getEmail();

        /** Store the credential keyed by the user's email */
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
     * Get Google Calendar events for a user on a specific day.
     * Returns all events that fall within the given date (midnight to midnight in the system default zone),
     * mapped to the shared CalendarEvent type.
     */
    public List<CalendarEvent> getEvents(String userId, LocalDate date) throws IOException {
        Credential credential = flow.loadCredential(userId);
        if (credential == null) {
            return Collections.emptyList();
        }

        Calendar service = new Calendar.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("Studious")
                .build();

        ZoneId zone = ZoneId.systemDefault();
        DateTime timeMin = new DateTime(date.atStartOfDay(zone).toInstant().toEpochMilli());
        DateTime timeMax = new DateTime(date.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli());

        Events events = service.events().list("primary")
                .setTimeMin(timeMin)
                .setTimeMax(timeMax)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<Event> items = events.getItems();
        if (items == null) {
            return Collections.emptyList();
        }

        return items.stream()
                .map(event -> new CalendarEvent(
                        event.getSummary(),
                        toLocalDateTime(event.getStart(), zone),
                        toLocalDateTime(event.getEnd(), zone),
                        "google",
                        event.getHtmlLink(),
                        event.getDescription()))
                .collect(Collectors.toList());
    }

    /** Maps a Google Calendar EventDateTime to LocalDateTime, handling both timed and all-day events. */
    private LocalDateTime toLocalDateTime(EventDateTime edt, ZoneId zone) {
        if (edt == null) return null;
        if (edt.getDateTime() != null) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(edt.getDateTime().getValue()), zone);
        }
        // All-day event: only a date is set, use start of day
        return LocalDate.parse(edt.getDate().toStringRfc3339()).atStartOfDay();
    }

    /**
     * Disconnect Google Calendar for a user
     */
    public void disconnect(String userId) throws IOException {
        flow.getCredentialDataStore().delete(userId);
    }
}
