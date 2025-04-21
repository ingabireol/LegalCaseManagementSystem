package model;

import java.time.LocalDate;

/**
 * Represents a legal document in the system.
 */
public class Document {
    private int id;
    private String documentId;
    private String title;
    private String description;
    private String documentType;
    private String filePath;
    private LocalDate dateAdded;
    private LocalDate documentDate;
    private int caseId;
    private Case associatedCase;
    private int createdBy;
    private String status;
    
    /**
     * Default constructor
     */
    public Document() {
        this.dateAdded = LocalDate.now();
        this.status = "Active";
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param documentId Unique document identifier
     * @param title Document title
     * @param documentType Type of document
     * @param caseId ID of the associated case
     */
    public Document(String documentId, String title, String documentType, int caseId) {
        this();
        this.documentId = documentId;
        this.title = title;
        this.documentType = documentType;
        this.caseId = caseId;
    }
    
    /**
     * Full constructor
     * 
     * @param id Database ID
     * @param documentId Unique document identifier
     * @param title Document title
     * @param description Document description
     * @param documentType Type of document
     * @param filePath Path to the document file
     * @param dateAdded Date the document was added to the system
     * @param documentDate Date of the document itself
     * @param caseId ID of the associated case
     * @param createdBy ID of the user who created/uploaded the document
     * @param status Document status
     */
    public Document(int id, String documentId, String title, String description, 
                   String documentType, String filePath, LocalDate dateAdded, 
                   LocalDate documentDate, int caseId, int createdBy, String status) {
        this();
        this.id = id;
        this.documentId = documentId;
        this.title = title;
        this.description = description;
        this.documentType = documentType;
        this.filePath = filePath;
        this.dateAdded = dateAdded;
        this.documentDate = documentDate;
        this.caseId = caseId;
        this.createdBy = createdBy;
        this.status = status;
    }

    // Getters and Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
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

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Check if the document is active
     * 
     * @return true if the document status is "Active"
     */
    public boolean isActive() {
        return "Active".equalsIgnoreCase(status);
    }
    
    /**
     * Get the file extension from the file path
     * 
     * @return File extension or empty string if not available
     */
    public String getFileExtension() {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filePath.length() - 1) {
            return filePath.substring(lastDotIndex + 1).toUpperCase();
        }
        
        return "";
    }
    
    /**
     * Get the display name for the document
     * 
     * @return Formatted string with title and type
     */
    public String getDisplayName() {
        String extension = getFileExtension();
        if (!extension.isEmpty()) {
            return title + " (" + extension + ")";
        }
        return title;
    }
    
    @Override
    public String toString() {
        return "Document [id=" + id + ", documentId=" + documentId + ", title=" + title + 
               ", type=" + documentType + ", dateAdded=" + dateAdded + "]";
    }
}