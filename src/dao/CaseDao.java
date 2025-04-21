package dao;

import model.Case;
import model.Client;
import model.Attorney;
import model.Document;
import model.Event;
import model.TimeEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data Access Object for Case operations.
 */
public class CaseDao {
    private String db_url = "jdbc:mysql://localhost:3306/legalcasemgmtdb";
    private String db_username = "root";
    private String db_passwd = "Ornella12345!";
    
    // Other DAOs for related entities
    private ClientDao clientDao;
    private AttorneyDao attorneyDao;
    private DocumentDao documentDao;
    private EventDao eventDao;
    private TimeEntryDao timeEntryDao;
    
    /**
     * Constructor
     */
    public CaseDao() {
        this.clientDao = new ClientDao();
        this.attorneyDao = new AttorneyDao();
        this.documentDao = new DocumentDao();
        this.eventDao = new EventDao();
        this.timeEntryDao = new TimeEntryDao();
    }
    
    /**
     * Creates a new case in the database
     * 
     * @param legalCase The case to create
     * @return Number of rows affected
     */
    public int createCase(Case legalCase) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "INSERT INTO cases (case_number, title, case_type, status, description, " +
                        "file_date, closing_date, court, judge, opposing_party, opposing_counsel, client_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, legalCase.getCaseNumber());
            pst.setString(2, legalCase.getTitle());
            pst.setString(3, legalCase.getCaseType());
            pst.setString(4, legalCase.getStatus());
            pst.setString(5, legalCase.getDescription());
            
            // Handle dates - convert LocalDate to java.sql.Date
            if (legalCase.getFileDate() != null) {
                pst.setDate(6, Date.valueOf(legalCase.getFileDate()));
            } else {
                pst.setNull(6, java.sql.Types.DATE);
            }
            
            if (legalCase.getClosingDate() != null) {
                pst.setDate(7, Date.valueOf(legalCase.getClosingDate()));
            } else {
                pst.setNull(7, java.sql.Types.DATE);
            }
            
            pst.setString(8, legalCase.getCourt());
            pst.setString(9, legalCase.getJudge());
            pst.setString(10, legalCase.getOpposingParty());
            pst.setString(11, legalCase.getOpposingCounsel());
            pst.setInt(12, legalCase.getClientId());
            
            // Execute statement
            int rowsAffected = pst.executeUpdate();
            
            // Get generated ID
            if (rowsAffected > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    legalCase.setId(rs.getInt(1));
                }
                rs.close();
                
                // Insert case attorneys (many-to-many relationship)
                if (legalCase.getAttorneys() != null && !legalCase.getAttorneys().isEmpty()) {
                    insertCaseAttorneys(con, legalCase.getId(), legalCase.getAttorneys());
                }
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
     * Insert case-attorney relationships in the junction table
     * 
     * @param con Database connection
     * @param caseId ID of the case
     * @param attorneys List of attorneys assigned to the case
     * @throws Exception If an error occurs
     */
    private void insertCaseAttorneys(Connection con, int caseId, List<Attorney> attorneys) throws Exception {
        String sql = "INSERT INTO case_attorneys (case_id, attorney_id) VALUES (?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        
        for (Attorney attorney : attorneys) {
            pst.setInt(1, caseId);
            pst.setInt(2, attorney.getId());
            pst.executeUpdate();
        }
        
        pst.close();
    }
    
    /**
     * Updates an existing case in the database
     * 
     * @param legalCase The case to update
     * @return Number of rows affected
     */
    public int updateCase(Case legalCase) {
        Connection con = null;
        try {
            // Create connection
            con = DriverManager.getConnection(db_url, db_username, db_passwd);
            con.setAutoCommit(false);  // Start transaction
            
            // Prepare statement
            String sql = "UPDATE cases SET case_number = ?, title = ?, case_type = ?, status = ?, " +
                        "description = ?, file_date = ?, closing_date = ?, court = ?, judge = ?, " +
                        "opposing_party = ?, opposing_counsel = ?, client_id = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, legalCase.getCaseNumber());
            pst.setString(2, legalCase.getTitle());
            pst.setString(3, legalCase.getCaseType());
            pst.setString(4, legalCase.getStatus());
            pst.setString(5, legalCase.getDescription());
            
            // Handle dates
            if (legalCase.getFileDate() != null) {
                pst.setDate(6, Date.valueOf(legalCase.getFileDate()));
            } else {
                pst.setNull(6, java.sql.Types.DATE);
            }
            
            if (legalCase.getClosingDate() != null) {
                pst.setDate(7, Date.valueOf(legalCase.getClosingDate()));
            } else {
                pst.setNull(7, java.sql.Types.DATE);
            }
            
            pst.setString(8, legalCase.getCourt());
            pst.setString(9, legalCase.getJudge());
            pst.setString(10, legalCase.getOpposingParty());
            pst.setString(11, legalCase.getOpposingCounsel());
            pst.setInt(12, legalCase.getClientId());
            pst.setInt(13, legalCase.getId());
            
            // Execute statement
            int rowsAffected = pst.executeUpdate();
            
            // Update case attorneys
            if (legalCase.getAttorneys() != null) {
                // First delete existing relationships
                sql = "DELETE FROM case_attorneys WHERE case_id = ?";
                PreparedStatement delPst = con.prepareStatement(sql);
                delPst.setInt(1, legalCase.getId());
                delPst.executeUpdate();
                delPst.close();
                
                // Then insert new ones
                insertCaseAttorneys(con, legalCase.getId(), legalCase.getAttorneys());
            }
            
            // Commit transaction
            con.commit();
            
            // Close connection
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            try {
                // Rollback transaction on error
                if (con != null && !con.getAutoCommit()) {
                    con.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            
            ex.printStackTrace();
            return 0;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
    
    /**
     * Updates the status of a case
     * 
     * @param caseId The ID of the case
     * @param status The new status
     * @return Number of rows affected
     */
    public int updateCaseStatus(int caseId, String status) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "UPDATE cases SET status = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, status);
            pst.setInt(2, caseId);
            
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
     * Finds a case by ID
     * 
     * @param id The case ID to search for
     * @return The case if found, null otherwise
     */
    public Case findCaseById(int id) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM cases WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            
            ResultSet rs = pst.executeQuery();
            Case legalCase = null;
            
            if (rs.next()) {
                legalCase = extractCaseFromResultSet(rs);
            }
            
            con.close();
            return legalCase;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds a case by case number
     * 
     * @param caseNumber The case number to search for
     * @return The case if found, null otherwise
     */
    public Case findCaseByCaseNumber(String caseNumber) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM cases WHERE case_number = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, caseNumber);
            
            ResultSet rs = pst.executeQuery();
            Case legalCase = null;
            
            if (rs.next()) {
                legalCase = extractCaseFromResultSet(rs);
            }
            
            con.close();
            return legalCase;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds cases by title or description
     * 
     * @param searchText The text to search for
     * @return List of matching cases
     */
    public List<Case> findCasesByText(String searchText) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM cases WHERE title LIKE ? OR description LIKE ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            String searchPattern = "%" + searchText + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            
            ResultSet rs = pst.executeQuery();
            List<Case> caseList = new ArrayList<>();
            
            while (rs.next()) {
                Case legalCase = extractCaseFromResultSet(rs);
                caseList.add(legalCase);
            }
            
            con.close();
            return caseList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds cases by client ID
     * 
     * @param clientId The client ID to search for
     * @return List of matching cases
     */
    public List<Case> findCasesByClient(int clientId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM cases WHERE client_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, clientId);
            
            ResultSet rs = pst.executeQuery();
            List<Case> caseList = new ArrayList<>();
            
            while (rs.next()) {
                Case legalCase = extractCaseFromResultSet(rs);
                caseList.add(legalCase);
            }
            
            con.close();
            return caseList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds cases by attorney ID
     * 
     * @param attorneyId The attorney ID to search for
     * @return List of matching cases
     */
    public List<Case> findCasesByAttorney(int attorneyId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT c.* FROM cases c " + 
                         "JOIN case_attorneys ca ON c.id = ca.case_id " +
                         "WHERE ca.attorney_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, attorneyId);
            
            ResultSet rs = pst.executeQuery();
            List<Case> caseList = new ArrayList<>();
            
            while (rs.next()) {
                Case legalCase = extractCaseFromResultSet(rs);
                caseList.add(legalCase);
            }
            
            con.close();
            return caseList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds cases by status
     * 
     * @param status The status to search for
     * @return List of matching cases
     */
    public List<Case> findCasesByStatus(String status) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM cases WHERE status = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, status);
            
            ResultSet rs = pst.executeQuery();
            List<Case> caseList = new ArrayList<>();
            
            while (rs.next()) {
                Case legalCase = extractCaseFromResultSet(rs);
                caseList.add(legalCase);
            }
            
            con.close();
            return caseList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds cases by case type
     * 
     * @param caseType The case type to search for
     * @return List of matching cases
     */
    public List<Case> findCasesByType(String caseType) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM cases WHERE case_type = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, caseType);
            
            ResultSet rs = pst.executeQuery();
            List<Case> caseList = new ArrayList<>();
            
            while (rs.next()) {
                Case legalCase = extractCaseFromResultSet(rs);
                caseList.add(legalCase);
            }
            
            con.close();
            return caseList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds cases by filing date range
     * 
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of matching cases
     */
    public List<Case> findCasesByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM cases WHERE file_date BETWEEN ? AND ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDate(1, java.sql.Date.valueOf(startDate));
            pst.setDate(2, java.sql.Date.valueOf(endDate));
            
            ResultSet rs = pst.executeQuery();
            List<Case> caseList = new ArrayList<>();
            
            while (rs.next()) {
                Case legalCase = extractCaseFromResultSet(rs);
                caseList.add(legalCase);
            }
            
            con.close();
            return caseList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Gets all cases
     * 
     * @return List of all cases
     */
    public List<Case> findAllCases() {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM cases";
            PreparedStatement pst = con.prepareStatement(sql);
            
            ResultSet rs = pst.executeQuery();
            List<Case> caseList = new ArrayList<>();
            
            while (rs.next()) {
                Case legalCase = extractCaseFromResultSet(rs);
                caseList.add(legalCase);
            }
            
            con.close();
            return caseList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Extract case data from a ResultSet row
     * 
     * @param rs ResultSet positioned at the case row
     * @return Case object populated with data
     * @throws Exception If an error occurs
     */
    private Case extractCaseFromResultSet(ResultSet rs) throws Exception {
        Case legalCase = new Case();
        
        legalCase.setId(rs.getInt("id"));
        legalCase.setCaseNumber(rs.getString("case_number"));
        legalCase.setTitle(rs.getString("title"));
        legalCase.setCaseType(rs.getString("case_type"));
        legalCase.setStatus(rs.getString("status"));
        legalCase.setDescription(rs.getString("description"));
        
        // Handle dates - convert java.sql.Date to LocalDate
        Date fileDate = rs.getDate("file_date");
        if (fileDate != null) {
            legalCase.setFileDate(fileDate.toLocalDate());
        }
        
        Date closingDate = rs.getDate("closing_date");
        if (closingDate != null) {
            legalCase.setClosingDate(closingDate.toLocalDate());
        }
        
        legalCase.setCourt(rs.getString("court"));
        legalCase.setJudge(rs.getString("judge"));
        legalCase.setOpposingParty(rs.getString("opposing_party"));
        legalCase.setOpposingCounsel(rs.getString("opposing_counsel"));
        legalCase.setClientId(rs.getInt("client_id"));
        
        return legalCase;
    }
    
    /**
     * Gets a case with all its details including client, attorneys, documents, events, and time entries
     * 
     * @param caseId The ID of the case
     * @return The case with all details loaded
     */
    public Case getCaseWithDetails(int caseId) {
        try {
            // First get the case
            Case legalCase = findCaseById(caseId);
            if (legalCase == null) {
                return null;
            }
            
            // Load client
            Client client = clientDao.findClientById(legalCase.getClientId());
            legalCase.setClient(client);
            
            // Load attorneys
            loadCaseAttorneys(legalCase);
            
            // Load documents
            List<Document> documents = documentDao.findDocumentsByCase(caseId);
            legalCase.setDocuments(documents);
            
            // Load events
            List<Event> events = eventDao.findEventsByCase(caseId);
            legalCase.setEvents(events);
            
            // Load time entries
            List<TimeEntry> timeEntries = timeEntryDao.findTimeEntriesByCase(caseId);
            legalCase.setTimeEntries(timeEntries);
            
            return legalCase;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Loads the attorneys associated with a case
     * 
     * @param legalCase The case to load attorneys for
     * @throws Exception If an error occurs
     */
    private void loadCaseAttorneys(Case legalCase) throws Exception {
        Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
        String sql = "SELECT a.* FROM attorneys a " +
                     "JOIN case_attorneys ca ON a.id = ca.attorney_id " +
                     "WHERE ca.case_id = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, legalCase.getId());
        
        ResultSet rs = pst.executeQuery();
        List<Attorney> attorneys = new ArrayList<>();
        
        while (rs.next()) {
            Attorney attorney = new Attorney();
            attorney.setId(rs.getInt("id"));
            attorney.setAttorneyId(rs.getString("attorney_id"));
            attorney.setFirstName(rs.getString("first_name"));
            attorney.setLastName(rs.getString("last_name"));
            attorney.setEmail(rs.getString("email"));
            attorney.setPhone(rs.getString("phone"));
            attorney.setSpecialization(rs.getString("specialization"));
            attorney.setBarNumber(rs.getString("bar_number"));
            attorney.setHourlyRate(rs.getDouble("hourly_rate"));
            
            attorneys.add(attorney);
        }
        
        legalCase.setAttorneys(attorneys);
        con.close();
    }
    
    /**
     * Deletes a case from the database
     * 
     * @param caseId The ID of the case to delete
     * @return Number of rows affected
     */
    public int deleteCase(int caseId) {
        Connection con = null;
        try {
            // Create connection
            con = DriverManager.getConnection(db_url, db_username, db_passwd);
            con.setAutoCommit(false);  // Start transaction
            
            // First delete related records in junction tables
            String sql = "DELETE FROM case_attorneys WHERE case_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, caseId);
            pst.executeUpdate();
            
            // Then delete the case
            sql = "DELETE FROM cases WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, caseId);
            
            int rowsAffected = pst.executeUpdate();
            
            // Commit transaction
            con.commit();
            
            // Close connection
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            try {
                // Rollback transaction on error
                if (con != null && !con.getAutoCommit()) {
                    con.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            
            ex.printStackTrace();
            return 0;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
}