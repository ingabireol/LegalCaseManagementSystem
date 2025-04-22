package controller;

import dao.DocumentDao;
import dao.CaseDao;
import model.Document;
import model.Case;

import java.util.List;
import java.time.LocalDate;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Controller for document-related operations.
 */
public class DocumentController {
    private DocumentDao documentDao;
    private CaseDao caseDao;
    private final String UPLOAD_DIRECTORY = "uploads/documents/";
    
    /**
     * Constructor
     */
    public DocumentController() {
        this.documentDao = new DocumentDao();
        this.caseDao = new CaseDao();
        
        // Ensure upload directory exists
        File directory = new File(UPLOAD_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    /**
     * Get all documents
     * 
     * @return List of all documents
     */
    public List<Document> getAllDocuments() {
        return documentDao.findAllDocuments();
    }
    
    /**
     * Get a document by ID
     * 
     * @param id The document ID
     * @return The document
     */
    public Document getDocumentById(int id) {
        return documentDao.findDocumentById(id);
    }
    
    /**
     * Get a document by document ID
     * 
     * @param documentId The document ID string
     * @return The document
     */
    public Document getDocumentByDocumentId(String documentId) {
        return documentDao.findDocumentByDocumentId(documentId);
    }
    
    /**
     * Get a document with case information
     * 
     * @param id The document ID
     * @return The document with case loaded
     */
    public Document getDocumentWithCase(int id) {
        return documentDao.getDocumentWithCase(id);
    }
    
    /**
     * Find documents by text
     * 
     * @param searchText Text to search for
     * @return List of matching documents
     */
    public List<Document> findDocumentsByText(String searchText) {
        return documentDao.findDocumentsByText(searchText);
    }
    
    /**
     * Find documents by type
     * 
     * @param documentType Type to search for
     * @return List of matching documents
     */
    public List<Document> findDocumentsByType(String documentType) {
        return documentDao.findDocumentsByType(documentType);
    }
    
    /**
     * Find documents by case
     * 
     * @param caseId The case ID
     * @return List of documents for the case
     */
    public List<Document> findDocumentsByCase(int caseId) {
        return documentDao.findDocumentsByCase(caseId);
    }
    
    /**
     * Find documents by date range
     * 
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of documents in the date range
     */
    public List<Document> findDocumentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return documentDao.findDocumentsByDateRange(startDate, endDate);
    }
    
    /**
     * Create a new document record and save the file
     * 
     * @param document The document to create
     * @param file The uploaded file
     * @return true if successful
     */
    public boolean createDocument(Document document, File file) {
        try {
            // Generate file path
            String fileName = System.currentTimeMillis() + "_" + file.getName();
            String filePath = UPLOAD_DIRECTORY + fileName;
            
            // Copy file to upload directory
            Path targetPath = Paths.get(filePath);
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            // Set file path in document
            document.setFilePath(filePath);
            
            // Set date added if not set
            if (document.getDateAdded() == null) {
                document.setDateAdded(LocalDate.now());
            }
            
            // Save document record
            int result = documentDao.createDocument(document);
            return result > 0;
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update an existing document
     * 
     * @param document The document to update
     * @return true if successful
     */
    public boolean updateDocument(Document document) {
        int result = documentDao.updateDocument(document);
        return result > 0;
    }
    
    /**
     * Update document status
     * 
     * @param documentId The document ID
     * @param status The new status
     * @return true if successful
     */
    public boolean updateDocumentStatus(int documentId, String status) {
        int result = documentDao.updateDocumentStatus(documentId, status);
        return result > 0;
    }
    
    /**
     * Delete a document
     * 
     * @param documentId The document ID
     * @return true if successful
     */
    public boolean deleteDocument(int documentId) {
        try {
            // Get document to get file path
            Document document = documentDao.findDocumentById(documentId);
            if (document == null) {
                return false;
            }
            
            // Delete file if exists
            String filePath = document.getFilePath();
            if (filePath != null && !filePath.isEmpty()) {
                Path path = Paths.get(filePath);
                Files.deleteIfExists(path);
            }
            
            // Delete database record
            int result = documentDao.deleteDocument(documentId);
            return result > 0;
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get the file content of a document
     * 
     * @param documentId The document ID
     * @return The file content as byte array, or null if error
     */
    public byte[] getDocumentContent(int documentId) {
        try {
            Document document = documentDao.findDocumentById(documentId);
            if (document == null || document.getFilePath() == null) {
                return null;
            }
            
            Path path = Paths.get(document.getFilePath());
            return Files.readAllBytes(path);
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get available document types
     * 
     * @return Array of document types
     */
    public String[] getDocumentTypes() {
        return new String[] {
            "Pleading", "Motion", "Brief", "Contract", "Letter", 
            "Email", "Invoice", "Receipt", "Court Order",
            "Settlement Agreement", "Evidence", "Other"
        };
    }
}