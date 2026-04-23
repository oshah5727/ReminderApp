package com.studious.studious.model;

import java.time.LocalDateTime;

/**
 * Shared representation of a calendar event from any source (Google Calendar or Canvas).
 * Used to present a unified list to the calendar view regardless of origin.
 */
public class CalendarEvent {

    private final String title;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    /** Either "google" or "canvas" — identifies which service the event came from. */
    private final String source;
    private final String url;
    private final String description;

    public CalendarEvent(String title, LocalDateTime startTime, LocalDateTime endTime,
                         String source, String url, String description) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.source = source;
        this.url = url;
        this.description = description;
    }

    public String getTitle() { return title; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getSource() { return source; }
    public String getUrl() { return url; }
    public String getDescription() { return description; }
}
