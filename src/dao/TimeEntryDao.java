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
    public TimeEntry findTimeEntryByEntryId(String entryId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM time_entries WHERE entry_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, entryId);
            
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
     * Finds time entries by case ID
     * 
     * @param caseId The case ID to search for
     * @return List of time entries for the case
     */
    public List<TimeEntry> findTimeEntriesByCase(int caseId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM time_entries WHERE case_id = ? ORDER BY entry_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, caseId);
            
            ResultSet rs = pst.executeQuery();
            List<TimeEntry> timeEntryList = new ArrayList<>();
            
            while (rs.next()) {
                TimeEntry timeEntry = extractTimeEntryFromResultSet(rs);
                timeEntryList.add(timeEntry);
            }
            
            con.close();
            return timeEntryList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds time entries by attorney ID
     * 
     * @param attorneyId The attorney ID to search for
     * @return List of time entries for the attorney
     */
    public List<TimeEntry> findTimeEntriesByAttorney(int attorneyId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM time_entries WHERE attorney_id = ? ORDER BY entry_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, attorneyId);
            
            ResultSet rs = pst.executeQuery();
            List<TimeEntry> timeEntryList = new ArrayList<>();
            
            while (rs.next()) {
                TimeEntry timeEntry = extractTimeEntryFromResultSet(rs);
                timeEntryList.add(timeEntry);
            }
            
            con.close();
            return timeEntryList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds time entries by date range
     * 
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of time entries in the date range
     */
    public List<TimeEntry> findTimeEntriesByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM time_entries WHERE entry_date BETWEEN ? AND ? ORDER BY entry_date";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDate(1, Date.valueOf(startDate));
            pst.setDate(2, Date.valueOf(endDate));
            
            ResultSet rs = pst.executeQuery();
            List<TimeEntry> timeEntryList = new ArrayList<>();
            
            while (rs.next()) {
                TimeEntry timeEntry = extractTimeEntryFromResultSet(rs);
                timeEntryList.add(timeEntry);
            }
            
            con.close();
            return timeEntryList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds unbilled time entries for a case
     * 
     * @param caseId The case ID to search for
     * @return List of unbilled time entries for the case
     */
    public List<TimeEntry> findUnbilledTimeEntriesByCase(int caseId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM time_entries WHERE case_id = ? AND billed = FALSE ORDER BY entry_date";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, caseId);
            
            ResultSet rs = pst.executeQuery();
            List<TimeEntry> timeEntryList = new ArrayList<>();
            
            while (rs.next()) {
                TimeEntry timeEntry = extractTimeEntryFromResultSet(rs);
                timeEntryList.add(timeEntry);
            }
            
            con.close();
            return timeEntryList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds time entries by invoice ID
     * 
     * @param invoiceId The invoice ID to search for
     * @return List of time entries for the invoice
     */
    public List<TimeEntry> findTimeEntriesByInvoice(int invoiceId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM time_entries WHERE invoice_id = ? ORDER BY entry_date";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, invoiceId);
            
            ResultSet rs = pst.executeQuery();
            List<TimeEntry> timeEntryList = new ArrayList<>();
            
            while (rs.next()) {
                TimeEntry timeEntry = extractTimeEntryFromResultSet(rs);
                timeEntryList.add(timeEntry);
            }
            
            con.close();
            return timeEntryList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds all time entries
     * 
     * @return List of all time entries
     */
    public List<TimeEntry> findAllTimeEntries() {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM time_entries ORDER BY entry_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            
            ResultSet rs = pst.executeQuery();
            List<TimeEntry> timeEntryList = new ArrayList<>();
            
            while (rs.next()) {
                TimeEntry timeEntry = extractTimeEntryFromResultSet(rs);
                timeEntryList.add(timeEntry);
            }
            
            con.close();
            return timeEntryList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Extract time entry data from a ResultSet row
     * 
     * @param rs ResultSet positioned at the time entry row
     * @return TimeEntry object populated with data
     * @throws Exception If an error occurs
     */
    private TimeEntry extractTimeEntryFromResultSet(ResultSet rs) throws Exception {
        TimeEntry timeEntry = new TimeEntry();
        
        timeEntry.setId(rs.getInt("id"));
        timeEntry.setEntryId(rs.getString("entry_id"));
        timeEntry.setCaseId(rs.getInt("case_id"));
        timeEntry.setAttorneyId(rs.getInt("attorney_id"));
        
        // Handle date
        Date entryDate = rs.getDate("entry_date");
        if (entryDate != null) {
            timeEntry.setEntryDate(entryDate.toLocalDate());
        }
        
        timeEntry.setHours(rs.getDouble("hours"));
        timeEntry.setDescription(rs.getString("description"));
        timeEntry.setActivityCode(rs.getString("activity_code"));
        
        // Handle BigDecimal
        BigDecimal hourlyRate = rs.getBigDecimal("hourly_rate");
        timeEntry.setHourlyRate(hourlyRate);
        
        timeEntry.setBilled(rs.getBoolean("billed"));
        timeEntry.setInvoiceId(rs.getInt("invoice_id"));
        
        return timeEntry;
    }
    
    /**
     * Gets a time entry with case and attorney information
     * 
     * @param timeEntryId The ID of the time entry
     * @return The time entry with case and attorney loaded
     */
    public TimeEntry getTimeEntryWithDetails(int timeEntryId) {
        try {
            // First get the time entry
            TimeEntry timeEntry = findTimeEntryById(timeEntryId);
            if (timeEntry == null) {
                return null;
            }
            
            // Load case information
            Case legalCase = caseDao.findCaseById(timeEntry.getCaseId());
            timeEntry.setCase(legalCase);
            
            // Load attorney information
            Attorney attorney = attorneyDao.findAttorneyById(timeEntry.getAttorneyId());
            timeEntry.setAttorney(attorney);
            
            return timeEntry;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Gets the total hours worked on a case
     * 
     * @param caseId The ID of the case
     * @return Total hours worked
     */
    public double getTotalHoursByCase(int caseId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT SUM(hours) AS total_hours FROM time_entries WHERE case_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, caseId);
            
            ResultSet rs = pst.executeQuery();
            double totalHours = 0.0;
            
            if (rs.next()) {
                totalHours = rs.getDouble("total_hours");
            }
            
            con.close();
            return totalHours;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }
    
    /**
     * Gets the total billable amount for a case
     * 
     * @param caseId The ID of the case
     * @return Total billable amount
     */
    public BigDecimal getTotalAmountByCase(int caseId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT SUM(hours * hourly_rate) AS total_amount FROM time_entries WHERE case_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, caseId);
            
            ResultSet rs = pst.executeQuery();
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            if (rs.next()) {
                totalAmount = rs.getBigDecimal("total_amount");
                if (totalAmount == null) {
                    totalAmount = BigDecimal.ZERO;
                }
            }
            
            con.close();
            return totalAmount;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Deletes a time entry from the database
     * 
     * @param timeEntryId The ID of the time entry to delete
     * @return Number of rows affected
     */
    public int deleteTimeEntry(int timeEntryId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Check if time entry is billed
            String checkSql = "SELECT billed FROM time_entries WHERE id = ?";
            PreparedStatement checkPst = con.prepareStatement(checkSql);
            checkPst.setInt(1, timeEntryId);
            
            ResultSet rs = checkPst.executeQuery();
            boolean isBilled = false;
            
            if (rs.next()) {
                isBilled = rs.getBoolean("billed");
            }
            
            if (isBilled) {
                // Cannot delete billed time entry
                con.close();
                return 0;
            }
            
            // Delete the time entry
            String sql = "DELETE FROM time_entries WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, timeEntryId);
            
            int rowsAffected = pst.executeUpdate();
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Marks all time entries for a case as billed and assigns them to an invoice
     * 
     * @param caseId The ID of the case
     * @param invoiceId The ID of the invoice
     * @return Number of rows affected
     */
    public int markCaseTimeEntriesAsBilled(int caseId, int invoiceId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Update all unbilled time entries for the case
            String sql = "UPDATE time_entries SET billed = TRUE, invoice_id = ? " +
                        "WHERE case_id = ? AND billed = FALSE";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setInt(1, invoiceId);
            pst.setInt(2, caseId);
            
            int rowsAffected = pst.executeUpdate();
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}