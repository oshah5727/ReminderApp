package com.studious.studious.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.studious.studious.model.CalendarEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages Canvas LMS integration state. Token and URL come from application.properties.
 * Documentation: https://developerdocs.instructure.com/services/canvas/oauth2/file.oauth
 */
@Service
public class CanvasService {

    private static final Path FLAG_FILE = Paths.get("tokens", "canvas_connected.flag");

    @Value("${canvas.token}")
    private String canvasToken;

    @Value("${canvas.url}")
    private String canvasUrl;

    /** Returns true if Canvas has been enabled */
    public boolean isConnected() {
        return Files.exists(FLAG_FILE);
    }

    /** Enables the Canvas integration by writing a flag file */
    public void connect() throws IOException {
        Files.createDirectories(FLAG_FILE.getParent());
        Files.createFile(FLAG_FILE);
    }

    /** Disables the Canvas integration by removing the flag file */
    public void disconnect() throws IOException {
        Files.deleteIfExists(FLAG_FILE);
    }

    /** Returns the Canvas API token from application.properties */
    public String getToken() {
        return canvasToken;
    }

    /** Returns the Canvas base URL from application.properties */
    public String getCanvasUrl() {
        return canvasUrl;
    }

    /**
     * Fetch Canvas LMS calendar events for a specific day.
     * Uses the Canvas API's start_date/end_date params to filter to exactly that date.
     * Returns events mapped to the shared CalendarEvent type.
     */
    public List<CalendarEvent> getCalendarEvents(LocalDate date) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + canvasToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = canvasUrl + "/api/v1/calendar_events?type=event&per_page=50&start_date=" + date + "&end_date=" + date;
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        List<Map<String, Object>> body = response.getBody();
        if (body == null) {
            return Collections.emptyList();
        }

        return body.stream()
                .map(event -> new CalendarEvent(
                        (String) event.get("title"),
                        parseDateTime((String) event.get("start_at")),
                        parseDateTime((String) event.get("end_at")),
                        "canvas",
                        (String) event.get("html_url"),
                        (String) event.get("description")))
                .collect(Collectors.toList());
    }

    /** Canvas ISO 8601 datetime string (e.g. "2026-04-21T10:00:00Z") to LocalDateTime. */
    private LocalDateTime parseDateTime(String iso) {
        if (iso == null) return null;
        return OffsetDateTime.parse(iso).toLocalDateTime();
    }
}
