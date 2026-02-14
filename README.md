# ReminderApp
## Introduction
  Students need to manage personal, work, and academic schedules across multiple online platforms, which makes it difficult for them to manage time effectively and stay organized. With various dates spread out between different calendars, students may forget assignments, run out of time to study, or procrastinate until the last minute due to not having the ability to simultaneously view and manage multiple tasks and events. 

  Studious is a web based application designed to solve this problem by combining all of the students' calendars into one place. It supports Canvas, Google Calendar, Blackboard, and more. Studious automatically recognizes upcoming tasks and events and displays them in a cohesive format. This format includes links, times, dates, and details relevant to the task or event.

  Users can easily add, edit, or delete events at anytime to keep their schedules up-to-date and personalized. The app also provides users with customizable notifications via text or email, ensuring students recieve reminders about their upcoming tasks and events. The reminder settings can be changed at any time to match a user's preferences, allowing alerts to be sent at specific intervals, like one day before an event, ten minutes prior, or any time in between.

With every task and event organized in one format, Studious empowers students to stay on top of their schedules, reduce stress, and manage their time more efficiently.

## Storyboard
![ReminderAppStoryBoard](https://github.com/user-attachments/assets/f0594e1e-e9bb-4af3-8125-f2172eb89d6e)

## Functional Requirements

## I want
Calendar Aggregation & Display

## So that I can
View all my academic and personal commitments in one unified interface

## Elaborate each of these with several examples in this format:

### Given
User has connected their Canvas, Google Calendar, and Blackboard accounts with valid API keys

### When
User opens the main dashboard

### Then
All upcoming events from all three platforms are displayed in chronological order with titles, dates, times, and platform source indicators

---

### Given
User has an upcoming assignment due in Canvas within 24 hours

### When
The notification interval triggers based on user settings

### Then
User receives a text/email notification with the assignment name, due date, and a direct link to the Canvas assignment

---

### Given
User has multiple overlapping events scheduled across different calendars

### When
System detects time conflicts during calendar sync

### Then
Dashboard displays a warning indicator highlighting the conflicting events

---

## Notes:
- Test cases: single/multiple calendar sources, invalid API keys, expired tokens, rate limits, network failures, timezone handling, OAuth flows for each platform, token expiration/revocation/refresh, security vulnerabilities (SQL injection, XSS, CSRF), encryption at rest and in transit

---

## I want
Event Management

## So that I can
Create, edit, and delete events across all connected calendars

## Elaborate each of these with several examples in this format:

### Given
User wants to create a new study session event

### When
User fills out the event form with title, date, time, and platform selection

### Then
Event is created on the selected platform and appears in the unified dashboard view

---

### Given
User needs to change the time of an existing meeting

### When
User clicks edit on the event and updates the time

### Then
Event is updated on the original platform and changes reflect immediately in the dashboard

---

### Given
User creates a recurring weekly event

### When
User sets recurrence pattern in event creation form

### Then
All instances of the recurring event appear on appropriate dates in the calendar view

---

## Notes:
- Test CRUD operations for each platform, verify synchronization after modifications, test recurring event operations, handle platform-specific limitations

---

## I want
Secure Multi-Platform Authentication

## So that I can
Safely connect my various calendar sources without compromising my credentials

## Elaborate each of these with several examples in this format:

### Given
New user creates an account and attempts to connect Google Calendar

### When
User clicks "Connect Google Calendar"

### Then
User is redirected to Google OAuth consent screen, grants permissions, and is redirected back with encrypted access token stored in database

---

### Given
User's Canvas API token expires after 30 days

### When
System attempts to fetch Canvas events and receives 401 Unauthorized response

### Then
User receives notification to reauthenticate, and system prompts user to reconnect

---

### Given
User stores API keys and calendar tokens in the database

### When
Data is written to persistent storage

### Then
All sensitive credentials are encrypted using AES-256 encryption and environment variables protect encryption keys

---

## Notes:
- Test OAuth flows, simulate token expiration scenarios, penetration testing for vulnerabilities, verify encryption standards

---

## I want
Customizable Notification System

## So that I can
Receive timely reminders without notification fatigue

## Elaborate each of these with several examples in this format:

### Given
User sets notification preferences to "1 day before" and "10 minutes before" for all events

### When
An exam is scheduled 24 hours from now

### Then
User receives first reminder via their preferred method (text/email) exactly 24 hours before, then again 10 minutes before the exam

---

### Given
User has 8 events in a single day

### When
System evaluates notification schedule for that day

### Then
Smart notification logic consolidates reminders into a morning digest plus individual urgent reminders to prevent spam

---

### Given
Notification service experiences downtime

### When
System attempts to send scheduled reminder

### Then
Failed notification is queued for retry with exponential backoff, and user is notified via alternate channel if available

---

## Notes:
- Test notification delivery across channels (email, SMS), verify timezone handling, test suppression logic, simulate service failures and retry mechanisms

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
