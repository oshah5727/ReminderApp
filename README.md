# ReminderApp
## Introduction
Many students have personal, work, and school calendars scattered across different online platforms. This makes it hard to manage their time effectively and keep track of their weekly schedules, and they can't be everywhere at once. Students may forget homework, run out of time to study, and save everything for the last minute as they don't have proper resources to view and manage their tasks and events. This is Studious, a web app that accepts keys from various calendar sources and merges them into one interface. Has support for Canvas, Google Calendar, Blackboard, and more. It will cohesively collect all upcoming events from each imported calendar and display them with working links, times, dates, and information. Users will be able to edit, add, or delete events as needed. Upcoming events will send the user a notification (text or email based on user settings) so they have consistent reminders for their obligations. These reminders can be changed in settings to be sent at certain intervals such as one day before, 10 minutes before, and so on. 

## Storyboard
![ReminderAppStoryBoard](https://github.com/user-attachments/assets/f0594e1e-e9bb-4af3-8125-f2172eb89d6e)

## Project Link
[Studious App Git Project](https://github.com/oshah5727/ReminderApp)

## Functional Requirements

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
## Members and Roles
Shah Opal - Product Owner \
Chris Vu - Scrum Master \
Rohit Vijai - Developer \
Jacob Dice - Developer 
## Kanban Board
To stay up to date on current development and tasks, please refer to the Projects tab of Github!
## Meeting Dates
Meeting 1: 2/11/26 2:00 pm
Meeting 2: 2/13/26 1:40 pm
