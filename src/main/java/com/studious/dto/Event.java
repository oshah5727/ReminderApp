package com.studious.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Data Transfer Object representing an Event in the Studious application.
 * This class corresponds to the Event JSON schema defined in the project requirements.
 * 
 * <p>It uses proper encapsulation by keeping fields private and providing 
 * public getter and setter methods. This ensures that the internal representation 
 * cannot be altered unexpectedly by external classes, preventing data corruption.</p>
 */
public class Event {

    private String id;
    private String title;
    private String description;
    
    // Using java.time API for better date/time handling instead of raw Strings
    private LocalDate date;
    private LocalTime time;

    /**
     * Default constructor required for JSON deserialization frameworks like Jackson.
     */
    public Event() {
    }

    /**
     * Constructs a new Event with full details.
     *
     * @param id          the unique identifier of the event
     * @param title       the name or title of the event
     * @param description the detailed description of the event
     * @param date        the date the event occurs on
     * @param time        the time the event occurs at
     */
    public Event(String id, String title, String description, LocalDate date, LocalTime time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    /**
     * Retrieves the unique identifier.
     * @return the event id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier.
     * @param id the new event id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the event title.
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the event title. This should not be null.
     * @param title the new title
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
    }

    /**
     * Retrieves the description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the date of the event.
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the event.
     * @param date the new date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Retrieves the time of the event.
     * @return the time
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets the time of the event.
     * @param time the new time
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
