package com.studious.studious;

import com.studious.studious.model.User;
import com.studious.studious.service.GoogleCalendarService;
import com.studious.studious.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Handles the Login form submission (POST request from the browser)
    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username,      // The username field from the form
            @RequestParam String password,      // The password field from the form
            HttpSession session,                // The user's current browser session
            RedirectAttributes redirectAttributes) {
        try {
            // Ask UserService to verify the credentials against the database.
            // If correct, we get the full User object back.
            User user = userService.authenticate(username, password);
            // Store the user's identity in their session so other pages know who is logged in.
            session.setAttribute("userId", user.getUsername());
            session.setAttribute("loggedInUser", user.getFirstName());
            // Redirect to the calendar page after a successful login
            return "redirect:/calendar";
        } catch (IllegalArgumentException e) {
            // If authentication failed, send an error message back to the login page
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    // Handles the Sign Up form submission (POST request from the browser)
    @PostMapping("/signup")
    public String processSignup(
            @RequestParam String firstname,         // Maps to the "firstname" input field
            @RequestParam String lastname,          // Maps to the "lastname" input field
            @RequestParam String email,             // Maps to the "email" input field
            @RequestParam String username,          // Maps to the "username" input field
            @RequestParam String password,          // Maps to the "password" input field
            @RequestParam String confirmpassword,   // Maps to the "confirmpassword" input field
            RedirectAttributes redirectAttributes) {
        // First, check that both password fields match before touching the database
        if (!password.equals(confirmpassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/auth/signup";
        }
        try {
            // Ask UserService to create the new user in the database.
            // UserService also checks for duplicate usernames/emails and hashes the password.
            userService.register(firstname, lastname, email, username, password);
            // On success, send the user to the login page with a success message
            redirectAttributes.addFlashAttribute("success", "Account created! Please log in.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            // If UserService threw an error (e.g. username taken), show it on the signup page
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/signup";
        }
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
            HttpSession session,
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
            String userId = googleCalendarService.handleCallback(code);
            session.setAttribute("userId", userId);
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
    public String googleDisconnect(HttpSession session, RedirectAttributes redirectAttributes) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "Not signed in");
            return "redirect:/settings";
        }
        try {
            googleCalendarService.disconnect(userId);
            session.removeAttribute("userId");
            redirectAttributes.addFlashAttribute("success", "Google Calendar disconnected successfully");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to disconnect Google Calendar");
        }
        return "redirect:/settings";
    }
}

