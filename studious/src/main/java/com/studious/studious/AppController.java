package com.studious.studious;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.studious.studious.model.CalendarEvent;
import com.studious.studious.service.CanvasService;
import com.studious.studious.service.GoogleCalendarService;

import jakarta.servlet.http.HttpSession;

// Handles routing for the main app pages
@Controller
public class AppController {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private CanvasService canvasService;

    // Returns the calendar view. Accepts an optional ?date=YYYY-MM-DD param; defaults to today.
    @GetMapping("/calendar")
    public String calendar(@RequestParam(required = false) String date,
                           HttpSession session, Model model) {
        LocalDate selected;
        try {
            selected = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        } catch (Exception e) {
            selected = LocalDate.now();
        }

        String userId = (String) session.getAttribute("userId");
        List<CalendarEvent> events = new ArrayList<>();

        if (userId != null) {
            try {
                events.addAll(googleCalendarService.getEvents(userId, selected));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (canvasService.isConnected()) {
            try {
                events.addAll(canvasService.getCalendarEvents(selected));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Include manually-added events stored in session
        @SuppressWarnings("unchecked")
        List<CalendarEvent> manualEvents = (List<CalendarEvent>) session.getAttribute("manualEvents");
        if (manualEvents != null) {
            for (CalendarEvent e : manualEvents) {
                if (e.getStartTime() != null && e.getStartTime().toLocalDate().equals(selected)) {
                    events.add(e);
                }
            }
        }

        events.sort(Comparator.comparing(CalendarEvent::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())));

        // Build the month grid as a list of weeks (each week = 7 LocalDates, null = empty cell)
        YearMonth month = YearMonth.from(selected);
        int startOffset = month.atDay(1).getDayOfWeek().getValue() % 7; // Sunday = 0
        List<List<LocalDate>> weeks = new ArrayList<>();
        List<LocalDate> week = new ArrayList<>(Arrays.asList(new LocalDate[7]));
        int dayOfMonth = 1;
        for (int cell = 0; cell < 42; cell++) {
            int col = cell % 7;
            if (cell >= startOffset && dayOfMonth <= month.lengthOfMonth()) {
                week.set(col, month.atDay(dayOfMonth++));
            }
            if (col == 6) {
                if (weeks.isEmpty() || week.stream().anyMatch(d -> d != null)) {
                    weeks.add(new ArrayList<>(week));
                }
                week = new ArrayList<>(Arrays.asList(new LocalDate[7]));
            }
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        model.addAttribute("selectedDate", selected);
        model.addAttribute("events", events);
        model.addAttribute("calendarWeeks", weeks);
        model.addAttribute("monthLabel", month.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        model.addAttribute("prevMonth", month.minusMonths(1).atDay(1).format(fmt));
        model.addAttribute("nextMonth", month.plusMonths(1).atDay(1).format(fmt));
        return "calendar";
    }

    // Returns the add event view
    @GetMapping("/add-event")
    public String addEvent() {
        return "add-event";
    }

    // Handles manual event submission from the add-event form
    @PostMapping("/add-event")
    public String addEventSubmit(
            @RequestParam String eventName,
            @RequestParam String eventDateTime,
            @RequestParam(required = false) String eventDescription,
            HttpSession session) {

        LocalDate eventDate = LocalDate.now();
        java.time.LocalDateTime dateTime;
        try {
            dateTime = java.time.LocalDateTime.parse(eventDateTime,
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            eventDate = dateTime.toLocalDate();
        } catch (Exception e) {
            dateTime = java.time.LocalDateTime.now();
        }

        CalendarEvent event = new CalendarEvent(
            UUID.randomUUID().toString(),
                eventName,
                dateTime,
                dateTime.plusHours(1),
                "manual",
                null,
                eventDescription
        );

        @SuppressWarnings("unchecked")
        List<CalendarEvent> manualEvents = (List<CalendarEvent>) session.getAttribute("manualEvents");
        if (manualEvents == null) {
            manualEvents = new ArrayList<>();
        }
        manualEvents.add(event);
        session.setAttribute("manualEvents", manualEvents);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "redirect:/calendar?date=" + eventDate.format(fmt);
    }

    // Returns the edit event view for a manually-added event
    @GetMapping("/event/edit")
    public String editEvent(@RequestParam String eventId,
                            @RequestParam(required = false) String date,
                            HttpSession session,
                            Model model) {
        @SuppressWarnings("unchecked")
        List<CalendarEvent> manualEvents = (List<CalendarEvent>) session.getAttribute("manualEvents");
        if (manualEvents == null) {
            return "redirect:/calendar";
        }

        CalendarEvent selectedEvent = null;
        for (CalendarEvent event : manualEvents) {
            if (eventId.equals(event.getId())) {
                selectedEvent = event;
                break;
            }
        }

        if (selectedEvent == null) {
            return (date != null) ? "redirect:/calendar?date=" + date : "redirect:/calendar";
        }

        DateTimeFormatter inputFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        model.addAttribute("eventId", selectedEvent.getId());
        model.addAttribute("eventName", selectedEvent.getTitle());
        model.addAttribute("eventDateTime", selectedEvent.getStartTime() != null ? selectedEvent.getStartTime().format(inputFmt) : "");
        model.addAttribute("eventDescription", selectedEvent.getDescription() != null ? selectedEvent.getDescription() : "");
        model.addAttribute("selectedDate", date);
        return "edit-event";
    }

    // Handles manual event update from the edit-event form
    @PostMapping("/event/update")
    public String updateEventSubmit(
            @RequestParam String eventId,
            @RequestParam String eventName,
            @RequestParam String eventDateTime,
            @RequestParam(required = false) String eventDescription,
            HttpSession session) {

        LocalDate eventDate = LocalDate.now();
        java.time.LocalDateTime dateTime;
        try {
            dateTime = java.time.LocalDateTime.parse(eventDateTime,
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            eventDate = dateTime.toLocalDate();
        } catch (Exception e) {
            dateTime = java.time.LocalDateTime.now();
        }

        @SuppressWarnings("unchecked")
        List<CalendarEvent> manualEvents = (List<CalendarEvent>) session.getAttribute("manualEvents");
        if (manualEvents != null) {
            for (int i = 0; i < manualEvents.size(); i++) {
                CalendarEvent current = manualEvents.get(i);
                if (eventId.equals(current.getId())) {
                    manualEvents.set(i, new CalendarEvent(
                            eventId,
                            eventName,
                            dateTime,
                            dateTime.plusHours(1),
                            "manual",
                            null,
                            eventDescription
                    ));
                    break;
                }
            }
            session.setAttribute("manualEvents", manualEvents);
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "redirect:/calendar?date=" + eventDate.format(fmt);
    }

    // Deletes a manually-added event by ID
    @PostMapping("/event/delete")
    public String deleteEvent(
            @RequestParam String eventId,
            @RequestParam(required = false) String selectedDate,
            HttpSession session) {

        @SuppressWarnings("unchecked")
        List<CalendarEvent> manualEvents = (List<CalendarEvent>) session.getAttribute("manualEvents");
        if (manualEvents != null) {
            manualEvents.removeIf(event -> eventId.equals(event.getId()));
            session.setAttribute("manualEvents", manualEvents);
        }

        if (selectedDate != null && !selectedDate.isBlank()) {
            return "redirect:/calendar?date=" + selectedDate;
        }
        return "redirect:/calendar";
    }

    // Returns the settings view, checking which integrations are connected
    @GetMapping("/settings")
    public String settings(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            userId = "default-user";
        }

        try {
            // Check if the user has connected their Google Calendar
            boolean googleConnected = googleCalendarService.isConnected(userId);
            model.addAttribute("googleConnected", googleConnected);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("googleConnected", false);
        }

        // Canvas integration not yet implemented
        model.addAttribute("canvasConnected", canvasService.isConnected());

        return "settings";
    }

    // Enables the Canvas integration using the token configured in application.properties
    @PostMapping("/canvas/connect")
    public String canvasConnect() {
        try {
            canvasService.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/settings";
    }

    // Removes the saved Canvas token
    @PostMapping("/canvas/disconnect")
    public String canvasDisconnect() {
        try {
            canvasService.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/settings";
    }

}
