# ReminderApp
## Introduction
  Students need to manage personal, work, and academic schedules across multiple online platforms, which makes it difficult for them to manage time effectively and stay organized. With various dates spread out between different calendars, students may forget assignments, run out of time to study, or procrastinate until the last minute due to not having the ability to simultaneously view and manage multiple tasks and events. 

  Studious is a web based application designed to solve this problem by combining all of the students' calendars into one place. It supports Canvas, Google Calendar, Blackboard, and more. Studious automatically recognizes upcoming tasks and events and displays them in a cohesive format. This format includes links, times, dates, and details relevant to the task or event.

  Users can easily add, edit, or delete events at anytime to keep their schedules up-to-date and personalized. The app also provides users with customizable notifications via text or email, ensuring students recieve reminders about their upcoming tasks and events. The reminder settings can be changed at any time to match a user's preferences, allowing alerts to be sent at specific intervals, like one day before an event, ten minutes prior, or any time in between.

With every task and event organized in one format, Studious empowers students to stay on top of their schedules, reduce stress, and manage their time more efficiently.

## Storyboard
![ReminderAppStoryBoard](https://github.com/user-attachments/assets/f0594e1e-e9bb-4af3-8125-f2172eb89d6e)

## Functional Requirements

# I want
Calendar Aggregation & Display, Event Management, Secure Multi-Platform Authentication, and Customizable Notification System

## So that I can
View and manage all my academic and personal commitments in one unified, secure interface with timely reminders

## I will elaborate each of these with several examples in this format:

### Given
User has connected their Canvas, Google Calendar, and Blackboard accounts with valid API keys

### When
User opens the main dashboard

### Then
All upcoming events from all three platforms are displayed in chronological order with titles, dates, times, and platform source indicators

---

### Given
User has multiple overlapping events scheduled across different calendars

### When
System detects time conflicts during calendar sync

### Then
Dashboard displays a warning indicator highlighting the conflicting events with specific time overlap details

---

### Given
User has an upcoming assignment due in Canvas within 24 hours

### When
The notification interval triggers based on user settings

### Then
User receives a text/email notification with the assignment name, due date, course name, and a direct clickable link to the Canvas assignment

---

### Given
User wants to create a new study session event

### When
User fills out the event form with title, date, time, duration, and platform selection

### Then
Event is created on the selected platform and appears in the unified dashboard view with confirmation message

---

### Given
User needs to change the time of an existing meeting

### When
User clicks edit on the event and updates the time from 2 PM to 4 PM

### Then
Event is updated on the original platform and changes reflect immediately in the dashboard with updated timestamp

---

### Given
User creates a recurring weekly event for Monday office hours

### When
User sets recurrence pattern to repeat every Monday at 3 PM for 12 weeks

### Then
All 12 instances of the recurring event appear on appropriate dates in the calendar view

---

### Given
User wants to delete a cancelled class

### When
User selects delete on the event and confirms deletion

### Then
Event is removed from the source platform and no longer appears in the unified view, with deletion confirmation shown

---

### Given
New user creates an account and attempts to connect Google Calendar

### When
User clicks "Connect Google Calendar" button

### Then
User is redirected to Google OAuth consent screen, grants calendar read/write permissions, and is redirected back with encrypted access token stored securely in database

---

### Given
User's Canvas API token expires after 30 days

### When
System attempts to fetch Canvas events and receives 401 Unauthorized response

### Then
User receives notification to reauthenticate with clear instructions, and system prompts user to reconnect Canvas with one-click reconnection option

---

### Given
User stores API keys and calendar tokens in the database

### When
Data is written to persistent storage

### Then
All sensitive credentials are encrypted using AES-256 encryption, environment variables protect encryption keys, and tokens are stored with expiration timestamps

---

### Given
Malicious actor attempts SQL injection through login form

### When
System receives suspicious input patterns like `'; DROP TABLE users;--`

### Then
Input is sanitized, parameterized queries prevent injection, attempt is logged with IP address for security monitoring

---

### Given
User sets notification preferences to "1 day before" and "10 minutes before" for all events

### When
An exam is scheduled 24 hours from now

### Then
User receives first reminder via their preferred method (text/email) exactly 24 hours before with exam details, then again 10 minutes before the exam starts

---

### Given
User has 8 events in a single day between 9 AM and 6 PM

### When
System evaluates notification schedule for that day at 8 AM

### Then
Smart notification logic consolidates reminders into a single morning digest listing all events plus individual urgent reminders sent 10 minutes before high-priority events to prevent notification spam

---

### Given
User changes notification preference from email to SMS in settings

### When
User saves updated notification settings

### Then
All future notifications are delivered via SMS to the phone number on file and previous email preference is overridden with confirmation message

---

### Given
Notification service like Twilio experiences downtime

### When
System attempts to send scheduled reminder at 9 AM

### Then
Failed notification is queued for retry with exponential backoff (1 min, 5 min, 15 min), and user is notified via alternate channel (email if SMS failed) if available

---

## Notes

### Test Cases
- Single/multiple calendar sources
- Invalid API keys
- Expired tokens
- Rate limits
- Network failures
- Timezone handling across different geographic locations
- OAuth flows for Google/Canvas/Blackboard
- Token management and refresh cycles
- CRUD operations for all event types
- Recurring events (daily/weekly/monthly)
- Platform synchronization delays
- Security vulnerabilities (SQL injection, XSS, CSRF)
- Encryption standards (AES-256, TLS 1.3)
- Notification delivery across channels (email via SendGrid, SMS via Twilio)
- Service failure retry mechanisms with exponential backoff


## Class Diagram
<img width="2816" height="1536" alt="UML Diagram" src="https://github.com/user-attachments/assets/472b1424-f770-4d45-a2cf-3bcecc2467a2" />

## JSON Schema
```json
{
    "$schema": "http://json-schema.org/draft-06/schema#",
    "$ref": "#/definitions/Studious",
    "definitions": {
        "Studious": {
            "type": "object",
            "additionalProperties": false,
            "properties": {
                "$id": {
                    "type": "string",
                    "format": "uri",
                    "qt-uri-protocols": [
                        "http"
                    ]
                },
                "$schema": {
                    "type": "string",
                    "format": "uri",
                    "qt-uri-protocols": [
                        "http"
                    ]
                },
                "description": {
                    "type": "string"
                },
                "type": {
                    "type": "string"
                },
                "required": {
                    "type": "array",
                    "items": {
                        "type": "string"
                    }
                },
                "properties": {
                    "$ref": "#/definitions/StudiousProperties"
                },
                "definitions": {
                    "$ref": "#/definitions/Definitions"
                }
            },
            "required": [
                "$id",
                "$schema",
                "definitions",
                "description",
                "properties",
                "required",
                "type"
            ],
            "title": "Studious"
        },
        "Definitions": {
            "type": "object",
            "additionalProperties": false,
            "properties": {
                "Event": {
                    "$ref": "#/definitions/Event"
                }
            },
            "required": [
                "Event"
            ],
            "title": "Definitions"
        },
        "Event": {
            "type": "object",
            "additionalProperties": false,
            "properties": {
                "type": {
                    "type": "string"
                },
                "required": {
                    "type": "array",
                    "items": {
                        "type": "string"
                    }
                },
                "properties": {
                    "$ref": "#/definitions/EventProperties"
                }
            },
            "required": [
                "properties",
                "required",
                "type"
            ],
            "title": "Event"
        },
        "EventProperties": {
            "type": "object",
            "additionalProperties": false,
            "properties": {
                "id": {
                    "$ref": "#/definitions/ID"
                },
                "title": {
                    "$ref": "#/definitions/Name"
                },
                "description": {
                    "$ref": "#/definitions/ID"
                },
                "date": {
                    "$ref": "#/definitions/Date"
                },
                "time": {
                    "$ref": "#/definitions/ID"
                }
            },
            "required": [
                "date",
                "description",
                "id",
                "time",
                "title"
            ],
            "title": "EventProperties"
        },
        "Date": {
            "type": "object",
            "additionalProperties": false,
            "properties": {
                "type": {
                    "type": "string"
                },
                "format": {
                    "type": "string"
                }
            },
            "required": [
                "format",
                "type"
            ],
            "title": "Date"
        },
        "ID": {
            "type": "object",
            "additionalProperties": false,
            "properties": {
                "type": {
                    "type": "string"
                }
            },
            "required": [
                "type"
            ],
            "title": "ID"
        },
        "Name": {
            "type": "object",
            "additionalProperties": false,
            "properties": {
                "type": {
                    "type": "string"
                },
                "minLength": {
                    "type": "integer"
                }
            },
            "required": [
                "minLength",
                "type"
            ],
            "title": "Name"
        },
        "StudiousProperties": {
            "type": "object",
            "additionalProperties": false,
            "properties": {
                "id": {
                    "$ref": "#/definitions/ID"
                },
                "username": {
                    "$ref": "#/definitions/Name"
                },
                "password": {
                    "$ref": "#/definitions/Name"
                },
                "name": {
                    "$ref": "#/definitions/Name"
                }
            },
            "required": [
                "id",
                "name",
                "password",
                "username"
            ],
            "title": "StudiousProperties"
        }
    }
}


```
## Scrum Roles
Opal Shah - Product Owner \
Chris Vu - Scrum Master \
Rohit Vijai - Developer \
Jacob Dice - Developer

## Project Link
[Studious App Git Project](https://github.com/oshah5727/ReminderApp)

## Kanban Board
To stay up to date on current development and tasks, please refer to the "Projects" tab in GitHub!
## Meeting Dates & Times
Meeting 1: 2/11/26 at 2:00pm \
Meeting 2: 2/13/26 at 1:40pm
