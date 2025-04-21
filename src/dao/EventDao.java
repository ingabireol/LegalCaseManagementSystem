package dao;

import model.Event;
import model.Case;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data Access Object for Event operations.
 */
public class EventDao {
    private String db_url = "jdbc:mysql://localhost:3306/legalcasemgmtdb";
    private String db_username = "root";
    private String db_passwd = "Ornella12345!";
    
    /**
     * Creates a new event in the database
     * 
     * @param event The event to create
     * @return Number of rows affected
     */
    public int createEvent(Event event) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "INSERT INTO events (event_id, title, description, event_type, event_date, " +
                        "start_time, end_time, location, status, case_id, reminder_set, reminder_days) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, event.getEventId());
            pst.setString(2, event.getTitle());
            pst.setString(3, event.getDescription());
            pst.setString(4, event.getEventType());
            pst.setDate(5, Date.valueOf(event.getEventDate()));
            
            // Handle times (can be null)
            if (event.getStartTime() != null) {
                pst.setTime(6, Time.valueOf(event.getStartTime()));
            } else {
                pst.setNull(6, java.sql.Types.TIME);
            }
            
            if (event.getEndTime() != null) {
                pst.setTime(7, Time.valueOf(event.getEndTime()));
            } else {
                pst.setNull(7, java.sql.Types.TIME);
            }
            
            pst.setString(8, event.getLocation());
            pst.setString(9, event.getStatus());
            pst.setInt(10, event.getCaseId());
            pst.setBoolean(11, event.isReminderSet());
            pst.setInt(12, event.getReminderDays());
            
            // Execute statement
            int rowsAffected = pst.executeUpdate();
            
            // Get generated ID
            if (rowsAffected > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    event.setId(rs.getInt(1));
                }
                rs.close();
            }
            
            // Close connection
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Updates an existing event in the database
     * 
     * @param event The event to update
     * @return Number of rows affected
     */
    public int updateEvent(Event event) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "UPDATE events SET event_id = ?, title = ?, description = ?, event_type = ?, " +
                        "event_date = ?, start_time = ?, end_time = ?, location = ?, status = ?, " +
                        "case_id = ?, reminder_set = ?, reminder_days = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, event.getEventId());
            pst.setString(2, event.getTitle());
            pst.setString(3, event.getDescription());
            pst.setString(4, event.getEventType());
            pst.setDate(5, Date.valueOf(event.getEventDate()));
            
            // Handle times (can be null)
            if (event.getStartTime() != null) {
                pst.setTime(6, Time.valueOf(event.getStartTime()));
            } else {
                pst.setNull(6, java.sql.Types.TIME);
            }
            
            if (event.getEndTime() != null) {
                pst.setTime(7, Time.valueOf(event.getEndTime()));
            } else {
                pst.setNull(7, java.sql.Types.TIME);
            }
            
            pst.setString(8, event.getLocation());
            pst.setString(9, event.getStatus());
            pst.setInt(10, event.getCaseId());
            pst.setBoolean(11, event.isReminderSet());
            pst.setInt(12, event.getReminderDays());
            pst.setInt(13, event.getId());
            
            // Execute statement
            int rowsAffected = pst.executeUpdate();
            
            // Close connection
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Updates the status of an event
     * 
     * @param eventId The ID of the event
     * @param status The new status
     * @return Number of rows affected
     */
    public int updateEventStatus(int eventId, String status) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "UPDATE events SET status = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, status);
            pst.setInt(2, eventId);
            
            // Execute statement
            int rowsAffected = pst.executeUpdate();
            
            // Close connection
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Finds an event by ID
     * 
     * @param id The event ID to search for
     * @return The event if found, null otherwise
     */
    public Event findEventById(int id) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM events WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            
            ResultSet rs = pst.executeQuery();
            Event event = null;
            
            if (rs.next()) {
                event = extractEventFromResultSet(rs);
            }
            
            con.close();
            return event;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds an event by event ID
     * 
     * @param eventId The event ID to search for
     * @return The event if found, null otherwise
     */
    public Event findEventByEventId(String eventId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM events WHERE event_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, eventId);
            
            ResultSet rs = pst.executeQuery();
            Event event = null;
            
            if (rs.next()) {
                event = extractEventFromResultSet(rs);
            }
            
            con.close();
            return event;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds events by case ID
     * 
     * @param caseId The case ID to search for
     * @return List of events for the case
     */
    public List<Event> findEventsByCase(int caseId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM events WHERE case_id = ? ORDER BY event_date, start_time";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, caseId);
            
            ResultSet rs = pst.executeQuery();
            List<Event> eventList = new ArrayList<>();
            
            while (rs.next()) {
                Event event = extractEventFromResultSet(rs);
                eventList.add(event);
            }
            
            con.close();
            return eventList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds events by date
     * 
     * @param date The date to search for
     * @return List of events on the date
     */
    public List<Event> findEventsByDate(LocalDate date) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM events WHERE event_date = ? ORDER BY start_time";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDate(1, Date.valueOf(date));
            
            ResultSet rs = pst.executeQuery();
            List<Event> eventList = new ArrayList<>();
            
            while (rs.next()) {
                Event event = extractEventFromResultSet(rs);
                eventList.add(event);
            }
            
            con.close();
            return eventList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds events by date range
     * 
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of events in the date range
     */
    public List<Event> findEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM events WHERE event_date BETWEEN ? AND ? ORDER BY event_date, start_time";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDate(1, Date.valueOf(startDate));
            pst.setDate(2, Date.valueOf(endDate));
            
            ResultSet rs = pst.executeQuery();
            List<Event> eventList = new ArrayList<>();
            
            while (rs.next()) {
                Event event = extractEventFromResultSet(rs);
                eventList.add(event);
            }
            
            con.close();
            return eventList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds events by status
     * 
     * @param status The status to search for
     * @return List of events with the status
     */
    public List<Event> findEventsByStatus(String status) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM events WHERE status = ? ORDER BY event_date, start_time";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, status);
            
            ResultSet rs = pst.executeQuery();
            List<Event> eventList = new ArrayList<>();
            
            while (rs.next()) {
                Event event = extractEventFromResultSet(rs);
                eventList.add(event);
            }
            
            con.close();
            return eventList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds upcoming events with reminders due
     * 
     * @return List of events with due reminders
     */
    public List<Event> findUpcomingEventsWithReminders() {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Find events where reminder is set and the event date minus reminder days equals today
            String sql = "SELECT * FROM events " +
                        "WHERE reminder_set = TRUE " +
                        "AND DATEDIFF(event_date, CURDATE()) = reminder_days " +
                        "AND status != 'Completed' AND status != 'Cancelled' " +
                        "ORDER BY event_date, start_time";
            
            PreparedStatement pst = con.prepareStatement(sql);
            
            ResultSet rs = pst.executeQuery();
            List<Event> eventList = new ArrayList<>();
            
            while (rs.next()) {
                Event event = extractEventFromResultSet(rs);
                eventList.add(event);
            }
            
            con.close();
            return eventList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds all events
     * 
     * @return List of all events
     */
    public List<Event> findAllEvents() {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM events ORDER BY event_date, start_time";
            PreparedStatement pst = con.prepareStatement(sql);
            
            ResultSet rs = pst.executeQuery();
            List<Event> eventList = new ArrayList<>();
            
            while (rs.next()) {
                Event event = extractEventFromResultSet(rs);
                eventList.add(event);
            }
            
            con.close();
            return eventList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Extract event data from a ResultSet row
     * 
     * @param rs ResultSet positioned at the event row
     * @return Event object populated with data
     * @throws Exception If an error occurs
     */
    private Event extractEventFromResultSet(ResultSet rs) throws Exception {
        Event event = new Event();
        
        event.setId(rs.getInt("id"));
        event.setEventId(rs.getString("event_id"));
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setEventType(rs.getString("event_type"));
        
        // Handle date and times
        Date eventDate = rs.getDate("event_date");
        if (eventDate != null) {
            event.setEventDate(eventDate.toLocalDate());
        }
        
        Time startTime = rs.getTime("start_time");
        if (startTime != null) {
            event.setStartTime(startTime.toLocalTime());
        }
        
        Time endTime = rs.getTime("end_time");
        if (endTime != null) {
            event.setEndTime(endTime.toLocalTime());
        }
        
        event.setLocation(rs.getString("location"));
        event.setStatus(rs.getString("status"));
        event.setCaseId(rs.getInt("case_id"));
        event.setReminderSet(rs.getBoolean("reminder_set"));
        event.setReminderDays(rs.getInt("reminder_days"));
        
        return event;
    }
    
    /**
     * Gets an event with its case information
     * 
     * @param eventId The ID of the event
     * @return The event with case loaded
     */
    public Event getEventWithCase(int eventId) {
        try {
            // First get the event
            Event event = findEventById(eventId);
            if (event == null) {
                return null;
            }
            
            // Then get its case
            CaseDao caseDao = new CaseDao();
            Case legalCase = caseDao.findCaseById(event.getCaseId());
            event.setCase(legalCase);
            
            return event;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Deletes an event from the database
     * 
     * @param eventId The ID of the event to delete
     * @return Number of rows affected
     */
    public int deleteEvent(int eventId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "DELETE FROM events WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, eventId);
            
            int rowsAffected = pst.executeUpdate();
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}