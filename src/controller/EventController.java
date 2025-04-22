package controller;

import dao.EventDao;
import dao.CaseDao;
import model.Event;
import model.Case;

import java.util.List;
import java.time.LocalDate;

/**
 * Controller for event-related operations.
 */
public class EventController {
    private EventDao eventDao;
    private CaseDao caseDao;
    
    /**
     * Constructor
     */
    public EventController() {
        this.eventDao = new EventDao();
        this.caseDao = new CaseDao();
    }
    
    /**
     * Get all events
     * 
     * @return List of all events
     */
    public List<Event> getAllEvents() {
        return eventDao.findAllEvents();
    }
    
    /**
     * Get an event by ID
     * 
     * @param id The event ID
     * @return The event
     */
    public Event getEventById(int id) {
        return eventDao.findEventById(id);
    }
    
    /**
     * Get an event by event ID
     * 
     * @param eventId The event ID string
     * @return The event
     */
    public Event getEventByEventId(String eventId) {
        return eventDao.findEventByEventId(eventId);
    }
    
    /**
     * Get an event with case information
     * 
     * @param id The event ID
     * @return The event with case loaded
     */
    public Event getEventWithCase(int id) {
        return eventDao.getEventWithCase(id);
    }
    
    /**
     * Find events by case
     * 
     * @param caseId The case ID
     * @return List of events for the case
     */
    public List<Event> findEventsByCase(int caseId) {
        return eventDao.findEventsByCase(caseId);
    }
    
    /**
     * Find events by date
     * 
     * @param date The date to search for
     * @return List of events on the date
     */
    public List<Event> findEventsByDate(LocalDate date) {
        return eventDao.findEventsByDate(date);
    }
    
    /**
     * Find events by date range
     * 
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of events in the date range
     */
    public List<Event> findEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        return eventDao.findEventsByDateRange(startDate, endDate);
    }
    
    /**
     * Find events by status
     * 
     * @param status The status
     * @return List of events with the status
     */
    public List<Event> findEventsByStatus(String status) {
        return eventDao.findEventsByStatus(status);
    }
    
    /**
     * Find upcoming events with reminders due
     * 
     * @return List of events with due reminders
     */
    public List<Event> findUpcomingEventsWithReminders() {
        return eventDao.findUpcomingEventsWithReminders();
    }
    
    /**
     * Create a new event
     * 
     * @param event The event to create
     * @return true if successful
     */
    public boolean createEvent(Event event) {
        int result = eventDao.createEvent(event);
        return result > 0;
    }
    
    /**
     * Update an existing event
     * 
     * @param event The event to update
     * @return true if successful
     */
    public boolean updateEvent(Event event) {
        int result = eventDao.updateEvent(event);
        return result > 0;
    }
    
    /**
     * Update event status
     * 
     * @param eventId The event ID
     * @param status The new status
     * @return true if successful
     */
    public boolean updateEventStatus(int eventId, String status) {
        int result = eventDao.updateEventStatus(eventId, status);
        return result > 0;
    }
    
    /**
     * Delete an event
     * 
     * @param eventId The event ID
     * @return true if successful
     */
    public boolean deleteEvent(int eventId) {
        int result = eventDao.deleteEvent(eventId);
        return result > 0;
    }
    
    /**
     * Get available event types
     * 
     * @return Array of event types
     */
    public String[] getEventTypes() {
        return new String[] {
            "Court Appearance", "Hearing", "Meeting", "Deposition", 
            "Trial", "Deadline", "Filing", "Conference Call", "Other"
        };
    }
    
    /**
     * Get available event statuses
     * 
     * @return Array of event statuses
     */
    public String[] getEventStatuses() {
        return new String[] {
            "Scheduled", "Completed", "Cancelled", "Postponed", "Rescheduled"
        };
    }
}