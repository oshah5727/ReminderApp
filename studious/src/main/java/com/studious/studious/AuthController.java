package com.studious.studious;

import com.studious.studious.service.GoogleCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    /**
     * Initiate Google OAuth flow
     * Redirects user to Google's consent screen
     */
    @GetMapping("/google")
    public String googleAuth() {
        try {
            String authUrl = googleCalendarService.getAuthorizationUrl();
            return "redirect:" + authUrl;
        } catch (Exception e) {
            e.printStackTrace();
            // If OAuth fails, redirect back to settings with error
            return "redirect:/settings?error=google_auth_failed";
        }
    }

    /**
     * Google OAuth callback endpoint
     * Google redirects here after user grants/denies permission
     */
    @GetMapping("/google/callback")
    public String googleCallback(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String error,
            RedirectAttributes redirectAttributes) {

        if (error != null) {
            // User denied permission
            redirectAttributes.addFlashAttribute("error", "Google Calendar connection was denied");
            return "redirect:/settings";
        }

        if (code == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid authorization code");
            return "redirect:/settings";
        }

        try {
            // TODO: Replace "default-user" with actual logged-in user ID when auth is implemented
            String userId = "default-user";
            googleCalendarService.handleCallback(code, userId);
            redirectAttributes.addFlashAttribute("success", "Google Calendar connected successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to connect Google Calendar");
        }

        return "redirect:/settings";
    }

    /**
     * TODO: Implement Canvas API authentication
     */
    @GetMapping("/canvas")
    public String canvasAuth() {
        // Canvas authentication will be implemented later
        return "redirect:/settings?error=canvas_not_implemented";
    }

    /**
     * Disconnect Google Calendar
     */
    @GetMapping("/google/disconnect")
    public String googleDisconnect(RedirectAttributes redirectAttributes) {
        try {
            // TODO: Replace "default-user" with actual logged-in user ID when auth is implemented
            String userId = "default-user";
            googleCalendarService.disconnect(userId);
            redirectAttributes.addFlashAttribute("success", "Google Calendar disconnected successfully");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to disconnect Google Calendar");
        }
        return "redirect:/settings";
    }
}

