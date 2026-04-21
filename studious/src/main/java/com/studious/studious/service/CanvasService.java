package com.studious.studious.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Manages Canvas LMS integration state. Token and URL come from application.properties.
// Documentation: https://developerdocs.instructure.com/services/canvas/oauth2/file.oauth
@Service
public class CanvasService {

    private static final Path FLAG_FILE = Paths.get("tokens", "canvas_connected.flag");

    @Value("${canvas.token}")
    private String canvasToken;

    @Value("${canvas.url}")
    private String canvasUrl;

    // Returns true if Canvas has been enabled
    public boolean isConnected() {
        return Files.exists(FLAG_FILE);
    }

    // Enables the Canvas integration by writing a flag file
    public void connect() throws IOException {
        Files.createDirectories(FLAG_FILE.getParent());
        Files.createFile(FLAG_FILE);
    }

    // Disables the Canvas integration by removing the flag file
    public void disconnect() throws IOException {
        Files.deleteIfExists(FLAG_FILE);
    }

    // Returns the Canvas API token from application.properties
    public String getToken() {
        return canvasToken;
    }

    // Returns the Canvas base URL from application.properties
    public String getCanvasUrl() {
        return canvasUrl;
    }
}
