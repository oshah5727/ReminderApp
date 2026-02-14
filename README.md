# ReminderApp
## Introduction
  Students need to manage personal, work, and academic schedules across multiple online platforms, which makes it difficult for them to manage time effectively and stay organized. With various dates spread out between different calendars, students may forget assignments, run out of time to study, or procrastinate until the last minute due to not having the ability to simultaneously view and manage multiple tasks and events. 

  Studious is a web based application designed to solve this problem by combining all of the students' calendars into one place. It supports Canvas, Google Calendar, Blackboard, and more. Studious automatically recognizes upcoming tasks and events and displays them in a cohesive format. This format includes links, times, dates, and details relevant to the task or event.

  Users can easily add, edit, or delete events at anytime to keep their schedules up-to-date and personalized. The app also provides users with customizable notifications via text or email, ensuring students recieve reminders about their upcoming tasks and events. The reminder settings can be changed at any time to match a user's preferences, allowing alerts to be sent at specific intervals, like one day before an event, ten minutes prior, or any time in between.

With every task and event organized in one format, Studious empowers students to stay on top of their schedules, reduce stress, and manage their time more efficiently.

## Storyboard
![ReminderAppStoryBoard](https://github.com/user-attachments/assets/f0594e1e-e9bb-4af3-8125-f2172eb89d6e)

## Functional Requirements

### 1) Unified Calendar View
**As a** student  
**I want** to connect multiple calendar sources and view them in one schedule  
**So that I can** see all my academic, work, and personal commitments in one place.

**Examples (Given / When / Then):**
- **Given** I am signed in, my Canvas, Google Calendar, and Blackboard accounts are connected with valid access, and at least one source contains upcoming events  
  **When** I open the dashboard, the app requests events from each source, normalizes timezones to my profile timezone, and merges results into one list  
  **Then** I see a single chronological schedule with each event’s title, date, start/end time (or all-day), source label, and a working link to open the event/assignment in the original platform.

- **Given** I have connected multiple sources and one source returns zero events for the selected week while other sources contain events  
  **When** I select that week and refresh the schedule  
  **Then** I still see all events from the other sources, and the empty source is clearly indicated as “no events found” (not an error), with the rest of the dashboard functioning normally.

- **Given** one of my connected sources has expired/invalid access (e.g., token revoked) but other sources are still valid  
  **When** the system syncs and receives an unauthorized/failed response from that source  
  **Then** events from valid sources still display, the failing source shows a clear “reconnect required” message, and the app provides a reconnect action without blocking access to my unified schedule.

**Notes:**
- Unit tests should include: multiple sources, empty results, partial failures (one source down), timezone/DST conversion, and verifying source links remain correct after merging.

---

### 2) Event Create/Edit/Delete
**As a** student  
**I want** to create, edit, and delete events in the unified calendar  
**So that I can** keep my schedule accurate without managing events separately across platforms.

**Examples (Given / When / Then):**
- **Given** I am signed in, at least one calendar is connected, and I have permission to write to the selected target calendar  
  **When** I open “Create event,” enter a title, start date/time, end date/time (or all-day), optional description/location, choose a target calendar, and click “Save”  
  **Then** the event is created in the selected source calendar, a confirmation message is shown, and the new event appears in my unified view with the correct time, details, and source label.

- **Given** an event exists in my unified view and it is editable (not read-only from the provider)  
  **When** I select the event, change details (e.g., time from 2:00 PM to 4:00 PM), and save changes  
  **Then** the update is sent to the original source, the unified view refreshes to show the new details, and the event keeps the same source label and link back to the provider.

- **Given** I enter invalid event data (e.g., missing title, end time before start time, or an invalid date)  
  **When** I click “Save”  
  **Then** the app prevents submission, highlights the invalid fields, and shows a clear error explaining what must be corrected.

**Notes:**
- Unit tests should include: valid create/edit/delete, invalid input validation, write failures from provider APIs, and ensuring no duplicate events occur on double-submit.

---

### 3) Secure Multi-Platform Authentication
**As a** student  
**I want** to securely connect and manage calendar integrations  
**So that I can** link my accounts safely without exposing credentials or losing control of access.

**Examples (Given / When / Then):**
- **Given** I am signed in and choose “Connect Google Calendar”  
  **When** I complete the provider authorization flow, approve requested permissions, and return to the app  
  **Then** the integration is saved, the app confirms the connection, and the next sync successfully imports events without the app storing my Google password.

- **Given** my Canvas access is expired/revoked and the app attempts to sync Canvas events  
  **When** the provider responds with an unauthorized error  
  **Then** the app pauses syncing for Canvas only, shows a reconnect prompt with clear instructions, and continues syncing/displaying events from other sources.

- **Given** I decide to remove a connected calendar source  
  **When** I click “Disconnect,” confirm the action, and the app processes the request  
  **Then** that source stops syncing immediately, stored access credentials for that source are removed/invalidated, and events from that source no longer appear after the next refresh (based on the app’s defined retention behavior).

**Notes:**
- Unit tests should include: connect/reconnect/disconnect flows, unauthorized handling, and verifying disconnected sources cannot sync until reauthorized.

---

### 4) Configurable Reminders
**As a** student  
**I want** configurable reminders for events and deadlines  
**So that I can** stay on track with timely alerts without being overwhelmed.

**Examples (Given / When / Then):**
- **Given** I have reminders enabled, I select email or SMS, and I set offsets (e.g., 1 day before and 10 minutes before) for events  
  **When** an event approaches each offset time and the reminder scheduler runs  
  **Then** I receive a reminder at each configured offset containing the event title, date/time, and a link to view details (and reminders are sent in my timezone).

- **Given** I choose SMS reminders but my phone number is missing or fails validation  
  **When** I try to save notification settings  
  **Then** the app blocks saving, explains what is wrong, and prompts me to add/verify a valid number before SMS can be enabled.

- **Given** the notification provider temporarily fails to deliver a message  
  **When** the system attempts to send a scheduled reminder and receives a failure response  
  **Then** the reminder is queued for retry using backoff rules, the system avoids sending duplicates, and (if configured) falls back to an alternate channel such as email.

**Notes:**
- Unit tests should include: multiple offsets per event, timezone/DST correctness, provider downtime + retries, and rescheduling reminders when an event time changes.

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
