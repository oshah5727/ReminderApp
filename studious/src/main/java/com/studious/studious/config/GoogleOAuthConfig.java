package com.studious.studious.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// This configuration class sets up the Google OAuth 2.0 flow for the application. 
// It reads the necessary credentials and settings from the application properties, 
// creates a GoogleAuthorizationCodeFlow bean, and provides a method to retrieve the redirect URI. 
// The tokens will be stored in a local directory named "tokens".
// Documentation: https://developers.google.com/api-client-library/java/google-api-java-client/oauth2

@Configuration
public class GoogleOAuthConfig {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.scopes}")
    private String scopes;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    @Bean
    public GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow() throws IOException {
        List<String> scopeList = Arrays.asList(scopes.split(","));

        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);

        GoogleClientSecrets clientSecrets = new GoogleClientSecrets();
        clientSecrets.setInstalled(details);

        return new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(),
                JSON_FACTORY,
                clientSecrets,
                scopeList)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
