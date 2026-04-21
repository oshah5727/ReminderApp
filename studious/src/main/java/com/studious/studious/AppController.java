package com.studious.studious;

import com.studious.studious.service.GoogleCalendarService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Handles routing for the main app pages
@Controller
public class AppController {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    // Returns the calendar view
    @GetMapping("/calendar")
    public String calendar() {
        return "calendar";
    }

    // Returns the add event view
    @GetMapping("/add-event")
    public String addEvent() {
        return "add-event";
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
        model.addAttribute("canvasConnected", false);

        return "settings";
    }

}
