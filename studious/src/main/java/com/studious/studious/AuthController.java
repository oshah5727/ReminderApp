package com.studious.studious;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    // TODO: Implement Google OAuth flow
    @GetMapping("/google")
    public String googleAuth() {
        // Redirect to Google OAuth consent screen
        // After authentication, Google will redirect back to callback URL
        return "redirect:/calendar"; // Placeholder for now
    }

    // TODO: Implement Canvas API authentication
    @GetMapping("/canvas")
    public String canvasAuth() {
        // Handle Canvas API token input or OAuth
        return "redirect:/calendar"; // Placeholder for now
    }

    // TODO: Add OAuth callback endpoints
    // @GetMapping("/google/callback")
    // @GetMapping("/canvas/callback")

}
