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

- **Given** my Canvas, Google Calendar, and Blackboard accounts are connected with valid access  
  **When** I open the dashboard  
  **Then** I see a single, chronological list of all upcoming events with title, date/time, and source platform.

- **Given** a Canvas assignment is due within 24 hours and reminders are enabled  
  **When** the reminder time I configured is reached  
  **Then** I receive a notification (text/email) with the assignment name, due date/time, and a direct link.

- **Given** two or more events overlap in time across connected calendars  
  **When** the system syncs and evaluates my schedule  
  **Then** the conflicting events are flagged and shown together so I can identify the overlap.

### Notes / Test Coverage
- Verify merge rules: sorting, deduplication, and consistent timezone handling (including DST).
- Validate sync behavior: initial import, incremental updates, and manual refresh.
- Negative cases: invalid/expired credentials, revoked access, partial provider outages, rate limiting, and network failures.
- Security checks: encryption in transit and at rest for tokens/keys; audit logs for connection changes.
- Data quality cases: missing titles, all-day events, recurring series, and events with attachments/links.

---

## 2) Event Management 

- **Given** I am signed in and have at least one calendar connected  
  **When** I create an event with a title, date/time, and selected calendar  
  **Then** the event is saved to that calendar and appears in the unified view.

- **Given** an event exists in the unified calendar  
  **When** I edit its details and save  
  **Then** the changes are applied to the source calendar and reflected in the unified view.

- **Given** I create an event with a weekly recurrence rule  
  **When** I submit the event  
  **Then** the repeating instances appear on future dates according to the recurrence settings.

### Notes / Test Coverage
- CRUD validation: required fields, invalid dates/times, and editing read-only/provider-owned events.
- Recurrence edge cases: editing one instance vs. the whole series, exceptions, and end dates.
- Sync correctness: confirm updates propagate to the source and back into the unified view without duplication.
- Concurrency: two edits at once, conflict resolution, and last-write-wins vs. prompting the user.
- Platform constraints: fields not supported by some sources (location, descriptions, reminders).

---

## 3) Secure Multi-Platform Authentication 

- **Given** I choose to connect Google Calendar  
  **When** I complete the provider authorization and return to the app  
  **Then** my connection is saved and the app can sync my Google events.

- **Given** my Canvas access is expired or revoked  
  **When** the app attempts to sync and receives an unauthorized response  
  **Then** I am prompted to reconnect Canvas and syncing is paused for that source until reconnection.

- **Given** the app stores tokens/keys for future sessions  
  **When** credentials are saved or retrieved  
  **Then** they are protected (encrypted at rest and transmitted securely) and only used for syncing.

### Notes / Test Coverage
- Authorization scope checks: least-privilege permissions and clear consent messaging.
- Token lifecycle: expiration, refresh, revocation, reconnect flow, and safe failure states.
- Storage security: encryption at rest, key management via environment/secret storage, and access controls.
- Threat modeling: SQL injection, XSS, CSRF, session hijacking, and rate-limit abuse.
- Observability: log auth events (connect/disconnect/refresh failures) without logging sensitive secrets.

---

## 4) Customizable Notification System 

- **Given** I set reminder offsets (e.g., 1 day and 10 minutes) and select email or SMS  
  **When** an event reaches each offset time  
  **Then** I receive a reminder at each configured time through my chosen channel.

- **Given** I have many events on the same day  
  **When** reminders are generated for that day  
  **Then** reminders are grouped into a digest when appropriate while urgent reminders still send individually.

- **Given** the notification provider fails to deliver a message  
  **When** the system detects the send failure  
  **Then** the message is retried using backoff and an alternate channel is used if I have one enabled.

### Notes / Test Coverage
- Timing correctness: timezone, DST changes, and reminders for all-day events.
- Channel behavior: email vs. SMS formatting, opt-in/opt-out, and invalid destination handling.
- Spam controls: digest thresholds, suppression rules, and user-configurable quiet hours.
- Reliability: retries, dead-letter queue behavior, idempotency (no duplicate sends), provider downtime.
- User settings: changing offsets/channels and ensuring changes apply to future notifications only (or clearly defined behavior).

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
