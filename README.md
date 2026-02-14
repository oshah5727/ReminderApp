# ReminderApp
## Introduction
  Students need to manage personal, work, and academic schedules across multiple online platforms, which makes it difficult for them to manage time effectively and stay organized. With various dates spread out between different calendars, students may forget assignments, run out of time to study, or procrastinate until the last minute due to not having the ability to simultaneously view and manage multiple tasks and events. 

  Studious is a web based application designed to solve this problem by combining all of the students' calendars into one place. It supports Canvas, Google Calendar, Blackboard, and more. Studious automatically recognizes upcoming tasks and events and displays them in a cohesive format. This format includes links, times, dates, and details relevant to the task or event.

  Users can easily add, edit, or delete events at anytime to keep their schedules up-to-date and personalized. The app also provides users with customizable notifications via text or email, ensuring students recieve reminders about their upcoming tasks and events. The reminder settings can be changed at any time to match a user's preferences, allowing alerts to be sent at specific intervals, like one day before an event, ten minutes prior, or any time in between.

With every task and event organized in one format, Studious empowers students to stay on top of their schedules, reduce stress, and manage their time more efficiently.

## Storyboard
![ReminderAppStoryBoard](https://github.com/user-attachments/assets/f0594e1e-e9bb-4af3-8125-f2172eb89d6e)

## Functional Requirements

## 1) Calendar Aggregation & Display
**As a user, I want** Calendar Aggregation & Display  
**So that I can** view all my academic and personal commitments in one unified interface.

- **Given** I have connected my Canvas, Google Calendar, and Blackboard accounts with valid API keys  
  **When** I open the main dashboard  
  **Then** I see all upcoming events from all three platforms in chronological order with titles, dates, times, and platform source indicators.

- **Given** I have an upcoming assignment due in Canvas within 24 hours  
  **When** my configured notification interval triggers  
  **Then** I receive a text/email notification with the assignment name, due date, and a direct link to the Canvas assignment.

- **Given** I have multiple overlapping events scheduled across different calendars  
  **When** the system syncs calendars and detects a time conflict  
  **Then** the dashboard shows a warning indicator highlighting the conflicting events.

### Notes / Test Coverage
- Single vs. multiple calendar sources
- Invalid API keys, expired tokens, token refresh/revocation
- Rate limits, network failures
- Timezone handling
- OAuth flows for each platform
- Security vulnerabilities (SQL injection, XSS, CSRF)
- Encryption in transit and at rest

---

## 2) Event Management
**As a user, I want** Event Management  
**So that I can** create, edit, and delete events across all connected calendars.

- **Given** I want to create a new study session event  
  **When** I fill out the event form with title, date, time, and platform selection and submit  
  **Then** the event is created on the selected platform and appears in the unified dashboard view.

- **Given** I need to change the time of an existing meeting  
  **When** I edit the event and save the updated time  
  **Then** the event updates on the original platform and the dashboard reflects the change.

- **Given** I want a repeating weekly event  
  **When** I set a recurrence pattern during event creation  
  **Then** all instances of the recurring event appear on the appropriate dates in the calendar view.

### Notes / Test Coverage
- CRUD operations per platform
- Sync correctness after modifications
- Recurring event create/edit/delete
- Platform-specific limitations and error handling

---

## 3) Secure Multi-Platform Authentication
**As a user, I want** Secure Multi-Platform Authentication  
**So that I can** safely connect multiple calendar sources without compromising my credentials.

- **Given** I am a new user connecting Google Calendar  
  **When** I click “Connect Google Calendar” and complete authorization  
  **Then** I am redirected back to the app and my encrypted access token is stored in the database.

- **Given** my Canvas API token has expired  
  **When** the system fetches Canvas events and receives an unauthorized response  
  **Then** I am notified to reauthenticate and prompted to reconnect.

- **Given** the app stores calendar tokens/keys for future sessions  
  **When** credentials are saved  
  **Then** they are encrypted using AES-256 and protected by secure environment-managed keys.

### Notes / Test Coverage
- OAuth flows and permissions
- Token expiration/refresh scenarios
- Penetration testing (SQL injection, XSS, CSRF)
- Encryption standard validation

---

## 4) Customizable Notification System
**As a user, I want** a Customizable Notification System  
**So that I can** receive timely reminders without notification fatigue.

- **Given** I set reminders to “1 day before” and “10 minutes before”  
  **When** an exam is scheduled 24 hours from now  
  **Then** I receive a reminder 24 hours before and another 10 minutes before the exam.

- **Given** I have 8 events in a single day  
  **When** the system prepares reminders for that day  
  **Then** reminders are consolidated into a digest plus urgent reminders as needed to reduce spam.

- **Given** the notification service experiences downtime  
  **When** the system attempts to send a scheduled reminder and it fails  
  **Then** the reminder is queued for retry with exponential backoff and an alternate channel is used if available.

### Notes / Test Coverage
- Email vs. SMS delivery
- Timezone correctness
- Suppression/digest logic
- Failure handling + retry behavior


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
