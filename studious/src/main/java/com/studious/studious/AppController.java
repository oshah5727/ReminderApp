package com.studious.studious;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("/calendar")
    public String calendar() {
        return "calendar";
    }

    @GetMapping("/add-event")
    public String addEvent() {
        return "add-event";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }

    // TODO: Add API endpoints for calendar operations
    // @PostMapping("/api/events")
    // @GetMapping("/api/events")
    // @DeleteMapping("/api/events/{id}")
    // @GetMapping("/api/sync-calendars")

}
