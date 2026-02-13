# ReminderApp

## Introduction

## Storyboard
![ReminderAppStoryBoard](https://github.com/user-attachments/assets/f0594e1e-e9bb-4af3-8125-f2172eb89d6e)

## Functional Requirements

### User Authentication & Management

- **FR-001**: System must create new user accounts when valid email, password, and profile data are provided, then send verification emails
- **FR-002**: System must authenticate registered users via email/password credentials and route them to the main dashboard upon successful login
- **FR-003**: System must generate and email secure password reset links when users request password recovery

### Calendar Integration and Synchronization

- **FR-004**: System must establish secure API connections when users authenticate with external calendar platforms (Google Calendar, Canvas, etc.) and perform initial data synchronization
- **FR-005**: System must automatically refresh the unified calendar within 5 minutes whenever changes occur in connected external platforms
- **FR-006**: System must display events with color-coding by source platform and provide filtering options by calendar type

### Event Management

- **FR-007**: System must create new events from user-provided details (title, date, time, description, location) with optional synchronization to selected external calendars
- **FR-008**: System must update events locally and sync modifications to connected platforms when users edit existing event details

### Notification & Reminder System

- **FR-009**: System must deliver event notifications through user-selected methods (email, SMS, push) based on configured reminder preferences
- **FR-010**: System must dispatch reminders at appropriate times according to event priority and user-defined preferences

### Data Management

- **FR-011**: System must securely store all calendar data and user preferences while maintaining data integrity across all interactions
- **FR-012**: System must generate downloadable backup files in standard formats (iCal, CSV) when users request data export

### System Integration

- **FR-013**: System must securely interface with multiple calendar platform APIs (Google Calendar, Canvas LTI, Blackboard, Outlook) using OAuth 2.0 authentication while maintaining consistent data formatting
- **FR-014**: System must immediately alert users and provide resolution suggestions when detecting scheduling conflicts from overlapping events across calendar sources

### User Experience & Customization

- **FR-015**: System must allow users to personalize calendar views, time zones, and display settings through dashboard configuration options
- **FR-016**: System must synchronize changes across all user devices (mobile, tablet, desktop) in real-time


## Class Diagram

## JSON Schema

## Members and Roles

## Kanban Board
