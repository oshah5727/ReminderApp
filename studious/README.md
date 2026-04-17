# Studious Calendar App - Setup Guide

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- Google Cloud account

## Setup Instructions

### 1. Clone the repository
```bash
git clone <repository-url>
cd studious
```

### 2. Configure Google OAuth

#### Create Google OAuth Credentials:
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing
3. Enable **Google Calendar API**:
   - Go to **APIs & Services > Library**
   - Search for "Google Calendar API"
   - Click **Enable**
4. Create OAuth 2.0 credentials:
   - Go to **APIs & Services > Credentials**
   - Click **Create Credentials > OAuth 2.0 Client ID**
   - Application type: **Web application**
   - Authorized redirect URIs: `http://localhost:8080/auth/google/callback`
   - Click **Create**
   - Copy your **Client ID** and **Client Secret**

#### Configure OAuth Consent Screen:
1. Go to **APIs & Services > OAuth consent screen**
2. Choose **External** user type
3. Fill in required fields (app name, support email)
4. Add scopes:
   - `https://www.googleapis.com/auth/calendar.readonly`
   - `https://www.googleapis.com/auth/calendar.events`
5. Add yourself as a test user

### 3. Set up application.properties

1. Copy the template file:
   ```bash
   cp src/main/resources/application.properties.template src/main/resources/application.properties
   ```

2. Edit `src/main/resources/application.properties`:
   - Replace `YOUR_GOOGLE_CLIENT_ID` with your Client ID
   - Replace `YOUR_GOOGLE_CLIENT_SECRET` with your Client Secret

### 4. Run the application

```bash
mvn spring-boot:run
```

The app will start at: `http://localhost:8080`

## Team Notes

- **DO NOT commit** `application.properties` (it's gitignored)
- Each team member needs their own Google OAuth credentials OR share one set via team communication
- The `tokens/` directory stores OAuth tokens locally (also gitignored)

## Troubleshooting

**Error: "invalid_client"**
- Check that your Client ID and Secret are correct
- Verify redirect URI is exactly: `http://localhost:8080/auth/google/callback`

**Error: "Access blocked"**
- Make sure you enabled Google Calendar API
- Add required scopes in OAuth consent screen
- Add yourself as a test user

## Project Structure
```
studious/
├── src/main/java/com/studious/studious/
│   ├── config/         # Configuration classes
│   ├── service/        # Business logic
│   └── controllers/    # API endpoints
├── src/main/resources/
│   └── templates/      # HTML pages
└── pom.xml            # Maven dependencies
```
