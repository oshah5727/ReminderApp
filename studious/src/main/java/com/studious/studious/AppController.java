package com.studious.studious;

import com.studious.studious.service.GoogleCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @GetMapping("/calendar")
    public String calendar() {
        return "calendar";
    }

    @GetMapping("/add-event")
    public String addEvent() {
        return "add-event";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        // TODO: Replace "default-user" with actual logged-in user ID when auth is implemented
        String userId = "default-user";

        try {
            boolean googleConnected = googleCalendarService.isConnected(userId);
            model.addAttribute("googleConnected", googleConnected);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("googleConnected", false);
        }

        // TODO: Add Canvas connection status check when implemented
        model.addAttribute("canvasConnected", false);

        return "settings";
    }

    // TODO: Add API endpoints for calendar operations
    // @PostMapping("/api/events")
    // @GetMapping("/api/events")
    // @DeleteMapping("/api/events/{id}")
    // @GetMapping("/api/sync-calendars")

}
