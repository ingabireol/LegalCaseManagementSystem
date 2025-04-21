package dao;

import model.Document;
import model.Case;

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
 * Data Access Object for Document operations.
 */
public class DocumentDao {
    private String db_url = "jdbc:mysql://localhost:3306/legalcasemgmtdb";
    private String db_username = "root";
    private String db_passwd = "Ornella12345!";
    
    /**
     * Creates a new document in the database
     * 
     * @param document The document to create
     * @return Number of rows affected
     */
    public int createDocument(Document document) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "INSERT INTO documents (document_id, title, description, document_type, " +
                        "file_path, date_added, document_date, case_id, created_by, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, document.getDocumentId());
            pst.setString(2, document.getTitle());
            pst.setString(3, document.getDescription());
            pst.setString(4, document.getDocumentType());
            pst.setString(5, document.getFilePath());
            
            // Handle dates
            pst.setDate(6, Date.valueOf(document.getDateAdded()));
            
            if (document.getDocumentDate() != null) {
                pst.setDate(7, Date.valueOf(document.getDocumentDate()));
            } else {
                pst.setNull(7, java.sql.Types.DATE);
            }
            
            pst.setInt(8, document.getCaseId());
            pst.setInt(9, document.getCreatedBy());
            pst.setString(10, document.getStatus());
            
            // Execute statement
            int rowsAffected = pst.executeUpdate();
            
            // Get generated ID
            if (rowsAffected > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    document.setId(rs.getInt(1));
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
     * Updates an existing document in the database
     * 
     * @param document The document to update
     * @return Number of rows affected
     */
    public int updateDocument(Document document) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "UPDATE documents SET document_id = ?, title = ?, description = ?, " +
                        "document_type = ?, file_path = ?, date_added = ?, document_date = ?, " +
                        "case_id = ?, created_by = ?, status = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, document.getDocumentId());
            pst.setString(2, document.getTitle());
            pst.setString(3, document.getDescription());
            pst.setString(4, document.getDocumentType());
            pst.setString(5, document.getFilePath());
            
            // Handle dates
            pst.setDate(6, Date.valueOf(document.getDateAdded()));
            
            if (document.getDocumentDate() != null) {
                pst.setDate(7, Date.valueOf(document.getDocumentDate()));
            } else {
                pst.setNull(7, java.sql.Types.DATE);
            }
            
            pst.setInt(8, document.getCaseId());
            pst.setInt(9, document.getCreatedBy());
            pst.setString(10, document.getStatus());
            pst.setInt(11, document.getId());
            
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
     * Updates the status of a document
     * 
     * @param documentId The ID of the document
     * @param status The new status
     * @return Number of rows affected
     */
    public int updateDocumentStatus(int documentId, String status) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "UPDATE documents SET status = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, status);
            pst.setInt(2, documentId);
            
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
     * Finds documents by date range
     * 
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of matching documents
     */
    public List<Document> findDocumentsByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM documents WHERE document_date BETWEEN ? AND ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDate(1, Date.valueOf(startDate));
            pst.setDate(2, Date.valueOf(endDate));
            
            ResultSet rs = pst.executeQuery();
            List<Document> documentList = new ArrayList<>();
            
            while (rs.next()) {
                Document document = extractDocumentFromResultSet(rs);
                documentList.add(document);
            }
            
            con.close();
            return documentList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Gets all documents
     * 
     * @return List of all documents
     */
    public List<Document> findAllDocuments() {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM documents";
            PreparedStatement pst = con.prepareStatement(sql);
            
            ResultSet rs = pst.executeQuery();
            List<Document> documentList = new ArrayList<>();
            
            while (rs.next()) {
                Document document = extractDocumentFromResultSet(rs);
                documentList.add(document);
            }
            
            con.close();
            return documentList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Extract document data from a ResultSet row
     * 
     * @param rs ResultSet positioned at the document row
     * @return Document object populated with data
     * @throws Exception If an error occurs
     */
    private Document extractDocumentFromResultSet(ResultSet rs) throws Exception {
        Document document = new Document();
        
        document.setId(rs.getInt("id"));
        document.setDocumentId(rs.getString("document_id"));
        document.setTitle(rs.getString("title"));
        document.setDescription(rs.getString("description"));
        document.setDocumentType(rs.getString("document_type"));
        document.setFilePath(rs.getString("file_path"));
        
        // Handle dates
        Date dateAdded = rs.getDate("date_added");
        if (dateAdded != null) {
            document.setDateAdded(dateAdded.toLocalDate());
        }
        
        Date documentDate = rs.getDate("document_date");
        if (documentDate != null) {
            document.setDocumentDate(documentDate.toLocalDate());
        }
        
        document.setCaseId(rs.getInt("case_id"));
        document.setCreatedBy(rs.getInt("created_by"));
        document.setStatus(rs.getString("status"));
        
        return document;
    }
    
    /**
     * Gets a document with its case information
     * 
     * @param documentId The ID of the document
     * @return The document with case loaded
     */
    public Document getDocumentWithCase(int documentId) {
        try {
            // First get the document
            Document document = findDocumentById(documentId);
            if (document == null) {
                return null;
            }
            
            // Then get its case
            CaseDao caseDao = new CaseDao();
            Case legalCase = caseDao.findCaseById(document.getCaseId());
            document.setCase(legalCase);
            
            return document;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Deletes a document from the database
     * 
     * @param documentId The ID of the document to delete
     * @return Number of rows affected
     */
    public int deleteDocument(int documentId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "DELETE FROM documents WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, documentId);
            
            int rowsAffected = pst.executeUpdate();
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
/** a document by ID
     * 
     * @param id The document ID to search for
     * @return The document if found, null otherwise
     */
    public Document findDocumentById(int id) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM documents WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            
            ResultSet rs = pst.executeQuery();
            Document document = null;
            
            if (rs.next()) {
                document = extractDocumentFromResultSet(rs);
            }
            
            con.close();
            return document;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds a document by document ID
     * 
     * @param documentId The document ID to search for
     * @return The document if found, null otherwise
     */
    public Document findDocumentByDocumentId(String documentId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM documents WHERE document_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, documentId);
            
            ResultSet rs = pst.executeQuery();
            Document document = null;
            
            if (rs.next()) {
                document = extractDocumentFromResultSet(rs);
            }
            
            con.close();
            return document;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds documents by title or description
     * 
     * @param searchText The text to search for
     * @return List of matching documents
     */
    public List<Document> findDocumentsByText(String searchText) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM documents WHERE title LIKE ? OR description LIKE ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            String searchPattern = "%" + searchText + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            
            ResultSet rs = pst.executeQuery();
            List<Document> documentList = new ArrayList<>();
            
            while (rs.next()) {
                Document document = extractDocumentFromResultSet(rs);
                documentList.add(document);
            }
            
            con.close();
            return documentList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds documents by case ID
     * 
     * @param caseId The case ID to search for
     * @return List of documents for the case
     */
    public List<Document> findDocumentsByCase(int caseId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM documents WHERE case_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, caseId);
            
            ResultSet rs = pst.executeQuery();
            List<Document> documentList = new ArrayList<>();
            
            while (rs.next()) {
                Document document = extractDocumentFromResultSet(rs);
                documentList.add(document);
            }
            
            con.close();
            return documentList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds documents by document type
     * 
     * @param documentType The document type to search for
     * @return List of matching documents
     */
    public List<Document> findDocumentsByType(String documentType) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM documents WHERE document_type = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, documentType);
            
            ResultSet rs = pst.executeQuery();
            List<Document> documentList = new ArrayList<>();
            
            while (rs.next()) {
                Document document = extractDocumentFromResultSet(rs);
                documentList.add(document);
            }
            
            con.close();
            return documentList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
}
