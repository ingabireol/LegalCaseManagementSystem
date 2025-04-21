package dao;

import model.TimeEntry;
import model.Case;
import model.Attorney;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data Access Object for TimeEntry operations.
 */
public class TimeEntryDao {
    private String db_url = "jdbc:mysql://localhost:3306/legalcasemgmtdb";
    private String db_username = "root";
    private String db_passwd = "Ornella12345!";
    
    // Other DAOs for related entities
    private CaseDao caseDao;
    private AttorneyDao attorneyDao;
    
    /**
     * Constructor
     */
    public TimeEntryDao() {
        this.caseDao = new CaseDao();
        this.attorneyDao = new AttorneyDao();
    }
    
    /**
     * Creates a new time entry in the database
     * 
     * @param timeEntry The time entry to create
     * @return Number of rows affected
     */
    public int createTimeEntry(TimeEntry timeEntry) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "INSERT INTO time_entries (entry_id, case_id, attorney_id, entry_date, hours, " +
                        "description, activity_code, hourly_rate, billed, invoice_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, timeEntry.getEntryId());
            pst.setInt(2, timeEntry.getCaseId());
            pst.setInt(3, timeEntry.getAttorneyId());
            pst.setDate(4, Date.valueOf(timeEntry.getEntryDate()));
            pst.setDouble(5, timeEntry.getHours());
            pst.setString(6, timeEntry.getDescription());
            pst.setString(7, timeEntry.getActivityCode());
            
            // Handle BigDecimal
            if (timeEntry.getHourlyRate() != null) {
                pst.setBigDecimal(8, timeEntry.getHourlyRate());
            } else {
                pst.setNull(8, java.sql.Types.DECIMAL);
            }
            
            pst.setBoolean(9, timeEntry.isBilled());
            
            if (timeEntry.getInvoiceId() > 0) {
                pst.setInt(10, timeEntry.getInvoiceId());
            } else {
                pst.setNull(10, java.sql.Types.INTEGER);
            }
            
            // Execute statement
            int rowsAffected = pst.executeUpdate();
            
            // Get generated ID
            if (rowsAffected > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    timeEntry.setId(rs.getInt(1));
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
     * Updates an existing time entry in the database
     * 
     * @param timeEntry The time entry to update
     * @return Number of rows affected
     */
    public int updateTimeEntry(TimeEntry timeEntry) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "UPDATE time_entries SET entry_id = ?, case_id = ?, attorney_id = ?, " +
                        "entry_date = ?, hours = ?, description = ?, activity_code = ?, " +
                        "hourly_rate = ?, billed = ?, invoice_id = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, timeEntry.getEntryId());
            pst.setInt(2, timeEntry.getCaseId());
            pst.setInt(3, timeEntry.getAttorneyId());
            pst.setDate(4, Date.valueOf(timeEntry.getEntryDate()));
            pst.setDouble(5, timeEntry.getHours());
            pst.setString(6, timeEntry.getDescription());
            pst.setString(7, timeEntry.getActivityCode());
            
            // Handle BigDecimal
            if (timeEntry.getHourlyRate() != null) {
                pst.setBigDecimal(8, timeEntry.getHourlyRate());
            } else {
                pst.setNull(8, java.sql.Types.DECIMAL);
            }
            
            pst.setBoolean(9, timeEntry.isBilled());
            
            if (timeEntry.getInvoiceId() > 0) {
                pst.setInt(10, timeEntry.getInvoiceId());
            } else {
                pst.setNull(10, java.sql.Types.INTEGER);
            }
            
            pst.setInt(11, timeEntry.getId());
            
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
     * Marks a time entry as billed
     * 
     * @param timeEntryId The ID of the time entry
     * @param invoiceId The ID of the invoice
     * @return Number of rows affected
     */
    public int markTimeEntryAsBilled(int timeEntryId, int invoiceId) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "UPDATE time_entries SET billed = TRUE, invoice_id = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setInt(1, invoiceId);
            pst.setInt(2, timeEntryId);
            
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
     * Finds a time entry by ID
     * 
     * @param id The time entry ID to search for
     * @return The time entry if found, null otherwise
     */
    public TimeEntry findTimeEntryById(int id) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM time_entries WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            
            ResultSet rs = pst.executeQuery();
            TimeEntry timeEntry = null;
            
            if (rs.next()) {
                timeEntry = extractTimeEntryFromResultSet(rs);
            }
            
            con.close();
            return timeEntry;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds a time entry by entry ID
     * 
     * @param entryId The entry ID to search for
     * @return The time entry if found, null otherwise
     */
    public TimeEntry findTimeEntryByEntryId(String entry