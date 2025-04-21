package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents an event or deadline in the legal system.
 * Can be associated with a specific case.
 */
public class Event {
    private int id;
    private String eventId;
    private String title;
    private String description;
    private String eventType;  // Court Date, Meeting, Deadline, etc.
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String status;  // Scheduled, Completed, Cancelled, etc.
    private int caseId;
    private Case associatedCase;
    private boolean reminderSet;
    private int reminderDays;
    
    /**
     * Default constructor
     */
    public Event() {
        this.status = "Scheduled";
        this.reminderSet = true;
        this.reminderDays = 1;
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param eventId Unique event identifier
     * @param title Event title
     * @param eventType Type of event
     * @param eventDate Date of the event
     * @param caseId ID of the associated case
     */
    public Event(String eventId, String title, String eventType, LocalDate eventDate, int caseId) {
        this();
        this.eventId = eventId;
        this.title = title;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.caseId = caseId;
    }
    
    /**
     * Full constructor
     * 
     * @param id Database ID
     * @param eventId Unique event identifier
     * @param title Event title
     * @param description Event description
     * @param eventType Type of event
     * @param eventDate Date of the event
     * @param startTime Start time of the event
     * @param endTime End time of the event
     * @param location Location of the event
     * @param status Event status
     * @param caseId ID of the associated case
     * @param reminderSet Whether a reminder is set for this event
     * @param reminderDays Number of days before the event to send a reminder
     */
    public Event(int id, String eventId, String title, String description, String eventType,
                LocalDate eventDate, LocalTime startTime, LocalTime endTime, String location,
                String status, int caseId, boolean reminderSet, int reminderDays) {
        this();
        this.id = id;
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.status = status;
        this.caseId = caseId;
        this.reminderSet = reminderSet;
        this.reminderDays = reminderDays;
    }

    // Getters and Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public Case getCase() {
        return associatedCase;
    }

    public void setCase(Case associatedCase) {
        this.associatedCase = associatedCase;
        if (associatedCase != null) {
            this.caseId = associatedCase.getId();
        }
    }

    public boolean isReminderSet() {
        return reminderSet;
    }

    public void setReminderSet(boolean reminderSet) {
        this.reminderSet = reminderSet;
    }

    public int getReminderDays() {
        return reminderDays;
    }

    public void setReminderDays(int reminderDays) {
        this.reminderDays = reminderDays;
    }
    
    /**
     * Check if the event is upcoming
     * 
     * @return true if the event date is in the future
     */
    public boolean isUpcoming() {
        return eventDate != null && eventDate.isAfter(LocalDate.now());
    }
    
    /**
     * Check if the event is overdue
     * 
     * @return true if the event date is in the past and status is not Completed or Cancelled
     */
    public boolean isOverdue() {
        return eventDate != null && eventDate.isBefore(LocalDate.now()) 
               && !("Completed".equalsIgnoreCase(status) || "Cancelled".equalsIgnoreCase(status));
    }
    
    /**
     * Get a formatted display string for the event
     * 
     * @return Formatted string with title, date, and type
     */
    public String getDisplayText() {
        String dateStr = eventDate != null ? eventDate.toString() : "No date";
        return title + " (" + dateStr + " - " + eventType + ")";
    }
    
    @Override
    public String toString() {
        return "Event [id=" + id + ", eventId=" + eventId + ", title=" + title + 
               ", date=" + eventDate + ", status=" + status + "]";
    }
}