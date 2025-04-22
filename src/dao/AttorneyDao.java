package dao;

import model.Attorney;
import model.Case;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data Access Object for Attorney operations.
 */
public class AttorneyDao {
    private String db_url = "jdbc:mysql://localhost:3306/legalcasemgmtdb";
    private String db_username = "root";
    private String db_passwd = "Ornella12345!";
    
    /**
     * Creates a new attorney in the database
     * 
     * @param attorney The attorney to create
     * @return Number of rows affected
     */
    public int createAttorney(Attorney attorney) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "INSERT INTO attorneys (attorney_id, first_name, last_name, email, phone, " +
                        "specialization, bar_number, hourly_rate) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, attorney.getAttorneyId());
            pst.setString(2, attorney.getFirstName());
            pst.setString(3, attorney.getLastName());
            pst.setString(4, attorney.getEmail());
            pst.setString(5, attorney.getPhone());
            pst.setString(6, attorney.getSpecialization());
            pst.setString(7, attorney.getBarNumber());
            pst.setDouble(8, attorney.getHourlyRate());
            
            // Execute statement
            int rowsAffected = pst.executeUpdate();
            
            // Get generated ID
            if (rowsAffected > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    attorney.setId(rs.getInt(1));
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
     * Updates an existing attorney in the database
     * 
     * @param attorney The attorney to update
     * @return Number of rows affected
     */
    public int updateAttorney(Attorney attorney) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "UPDATE attorneys SET attorney_id = ?, first_name = ?, last_name = ?, " +
                        "email = ?, phone = ?, specialization = ?, bar_number = ?, hourly_rate = ? " +
                        "WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, attorney.getAttorneyId());
            pst.setString(2, attorney.getFirstName());
            pst.setString(3, attorney.getLastName());
            pst.setString(4, attorney.getEmail());
            pst.setString(5, attorney.getPhone());
            pst.setString(6, attorney.getSpecialization());
            pst.setString(7, attorney.getBarNumber());
            pst.setDouble(8, attorney.getHourlyRate());
            pst.setInt(9, attorney.getId());
            
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
     * Finds an attorney by ID
     * 
     * @param id The attorney ID to search for
     * @return The attorney if found, null otherwise
     */
    public Attorney findAttorneyById(int id) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM attorneys WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            
            ResultSet rs = pst.executeQuery();
            Attorney attorney = null;
            
            if (rs.next()) {
                attorney = extractAttorneyFromResultSet(rs);
            }
            
            con.close();
            return attorney;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds an attorney by attorney ID
     * 
     * @param attorneyId The attorney ID to search for
     * @return The attorney if found, null otherwise
     */
    public Attorney findAttorneyByAttorneyId(String attorneyId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM attorneys WHERE attorney_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, attorneyId);
            
            ResultSet rs = pst.executeQuery();
            Attorney attorney = null;
            
            if (rs.next()) {
                attorney = extractAttorneyFromResultSet(rs);
            }
            
            con.close();
            return attorney;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds attorneys by name (first name, last name, or full name)
     * 
     * @param name The name to search for
     * @return List of matching attorneys
     */
    public List<Attorney> findAttorneysByName(String name) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM attorneys WHERE first_name LIKE ? OR last_name LIKE ? OR " +
                        "CONCAT(first_name, ' ', last_name) LIKE ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            String searchPattern = "%" + name + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            pst.setString(3, searchPattern);
            
            ResultSet rs = pst.executeQuery();
            List<Attorney> attorneyList = new ArrayList<>();
            
            while (rs.next()) {
                Attorney attorney = extractAttorneyFromResultSet(rs);
                attorneyList.add(attorney);
            }
            
            con.close();
            return attorneyList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds attorneys by specialization
     * 
     * @param specialization The specialization to search for
     * @return List of matching attorneys
     */
    public List<Attorney> findAttorneysBySpecialization(String specialization) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM attorneys WHERE specialization = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, specialization);
            
            ResultSet rs = pst.executeQuery();
            List<Attorney> attorneyList = new ArrayList<>();
            
            while (rs.next()) {
                Attorney attorney = extractAttorneyFromResultSet(rs);
                attorneyList.add(attorney);
            }
            
            con.close();
            return attorneyList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds attorneys by case ID
     * 
     * @param caseId The case ID to search for
     * @return List of attorneys assigned to the case
     */
    public List<Attorney> findAttorneysByCase(int caseId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT a.* FROM attorneys a " +
                        "JOIN case_attorneys ca ON a.id = ca.attorney_id " +
                        "WHERE ca.case_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, caseId);
            
            ResultSet rs = pst.executeQuery();
            List<Attorney> attorneyList = new ArrayList<>();
            
            while (rs.next()) {
                Attorney attorney = extractAttorneyFromResultSet(rs);
                attorneyList.add(attorney);
            }
            
            con.close();
            return attorneyList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Gets all attorneys
     * 
     * @return List of all attorneys
     */
    public List<Attorney> findAllAttorneys() {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM attorneys";
            PreparedStatement pst = con.prepareStatement(sql);
            
            ResultSet rs = pst.executeQuery();
            List<Attorney> attorneyList = new ArrayList<>();
            
            while (rs.next()) {
                Attorney attorney = extractAttorneyFromResultSet(rs);
                attorneyList.add(attorney);
            }
            
            con.close();
            return attorneyList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Extract attorney data from a ResultSet row
     * 
     * @param rs ResultSet positioned at the attorney row
     * @return Attorney object populated with data
     * @throws Exception If an error occurs
     */
    private Attorney extractAttorneyFromResultSet(ResultSet rs) throws Exception {
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
        
        return attorney;
    }
    
    /**
     * Gets an attorney with all their cases
     * 
     * @param attorneyId The ID of the attorney
     * @return The attorney with cases loaded
     */
    public Attorney getAttorneyWithCases(int attorneyId) {
        try {
            // First get the attorney
            Attorney attorney = findAttorneyById(attorneyId);
            if (attorney == null) {
                return null;
            }
            
            // Then get their cases
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT c.* FROM cases c " +
                        "JOIN case_attorneys ca ON c.id = ca.case_id " +
                        "WHERE ca.attorney_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, attorneyId);
            
            ResultSet rs = pst.executeQuery();
            List<Case> cases = new ArrayList<>();
            
            while (rs.next()) {
                Case legalCase = new Case();
                legalCase.setId(rs.getInt("id"));
                legalCase.setCaseNumber(rs.getString("case_number"));
                legalCase.setTitle(rs.getString("title"));
                legalCase.setCaseType(rs.getString("case_type"));
                legalCase.setStatus(rs.getString("status"));
                legalCase.setDescription(rs.getString("description"));
                
                // Handle dates
                java.sql.Date fileDate = rs.getDate("file_date");
                if (fileDate != null) {
                    legalCase.setFileDate(fileDate.toLocalDate());
                }
                
                java.sql.Date closingDate = rs.getDate("closing_date");
                if (closingDate != null) {
                    legalCase.setClosingDate(closingDate.toLocalDate());
                }
                
                legalCase.setCourt(rs.getString("court"));
                legalCase.setJudge(rs.getString("judge"));
                legalCase.setOpposingParty(rs.getString("opposing_party"));
                legalCase.setOpposingCounsel(rs.getString("opposing_counsel"));
                legalCase.setClientId(rs.getInt("client_id"));
                
                cases.add(legalCase);
            }
            
            attorney.setCases(cases);
            con.close();
            return attorney;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Deletes an attorney from the database
     * 
     * @param attorneyId The ID of the attorney to delete
     * @return Number of rows affected
     */
    public int deleteAttorney(int attorneyId) {
        Connection con = null;
        try {
            // Create connection
            con = DriverManager.getConnection(db_url, db_username, db_passwd);
            con.setAutoCommit(false);  // Start transaction
            
            // First delete from case_attorneys junction table
            String sql = "DELETE FROM case_attorneys WHERE attorney_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, attorneyId);
            pst.executeUpdate();
            
            // Then delete the attorney
            sql = "DELETE FROM attorneys WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, attorneyId);
            
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
    /**
 * Finds cases by attorney ID
 * 
 * @param attorneyId The attorney ID to search for
 * @return List of cases for the attorney
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

}