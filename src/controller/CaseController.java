package controller;

import dao.CaseDao;
import dao.ClientDao;
import dao.AttorneyDao;
import dao.DocumentDao;
import dao.EventDao;
import dao.TimeEntryDao;
import model.Case;
import model.Client;
import model.Attorney;
import model.Document;
import model.Event;
import model.TimeEntry;

import java.util.List;
import java.time.LocalDate;

/**
 * Controller for case-related operations.
 */
public class CaseController {
    private CaseDao caseDao;
    private ClientDao clientDao;
    private AttorneyDao attorneyDao;
    private DocumentDao documentDao;
    private EventDao eventDao;
    private TimeEntryDao timeEntryDao;
    
    /**
     * Constructor
     */
    public CaseController() {
        this.caseDao = new CaseDao();
        this.clientDao = new ClientDao();
        this.attorneyDao = new AttorneyDao();
        this.documentDao = new DocumentDao();
        this.eventDao = new EventDao();
        this.timeEntryDao = new TimeEntryDao();
    }
    
    /**
     * Get all cases
     * 
     * @return List of all cases
     */
    public List<Case> getAllCases() {
        return caseDao.findAllCases();
    }
    
    /**
     * Get a case by ID
     * 
     * @param id The case ID
     * @return The case
     */
    public Case getCaseById(int id) {
        return caseDao.findCaseById(id);
    }
    
    /**
     * Get a case by case number
     * 
     * @param caseNumber The case number
     * @return The case
     */
    public Case getCaseByCaseNumber(String caseNumber) {
        return caseDao.findCaseByCaseNumber(caseNumber);
    }
    
    /**
     * Get a case with all details
     * 
     * @param id The case ID
     * @return The case with all details loaded
     */
    public Case getCaseWithDetails(int id) {
        return caseDao.getCaseWithDetails(id);
    }
    
    /**
     * Find cases by client
     * 
     * @param clientId The client ID
     * @return List of cases
     */
    public List<Case> findCasesByClient(int clientId) {
        return caseDao.findCasesByClient(clientId);
    }
    
    /**
     * Find cases by attorney
     * 
     * @param attorneyId The attorney ID
     * @return List of cases
     */
    public List<Case> findCasesByAttorney(int attorneyId) {
        return caseDao.findCasesByAttorney(attorneyId);
    }
    
    /**
     * Find cases by status
     * 
     * @param status The status
     * @return List of cases
     */
    public List<Case> findCasesByStatus(String status) {
        return caseDao.findCasesByStatus(status);
    }
    
    /**
     * Find cases by type
     * 
     * @param caseType The case type
     * @return List of cases
     */
    public List<Case> findCasesByType(String caseType) {
        return caseDao.findCasesByType(caseType);
    }
    
    /**
     * Find cases by text search
     * 
     * @param searchText The text to search for
     * @return List of cases
     */
    public List<Case> findCasesByText(String searchText) {
        return caseDao.findCasesByText(searchText);
    }
    
    /**
     * Find cases by date range
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return List of cases
     */
    public List<Case> findCasesByDateRange(LocalDate startDate, LocalDate endDate) {
        return caseDao.findCasesByDateRange(startDate, endDate);
    }
    
    /**
     * Create a new case
     * 
     * @param legalCase The case to create
     * @return true if successful
     */
    public boolean createCase(Case legalCase) {
        // Set file date if not set
        if (legalCase.getFileDate() == null) {
            legalCase.setFileDate(LocalDate.now());
        }
        
        int result = caseDao.createCase(legalCase);
        return result > 0;
    }
    
    /**
     * Update an existing case
     * 
     * @param legalCase The case to update
     * @return true if successful
     */
    public boolean updateCase(Case legalCase) {
        int result = caseDao.updateCase(legalCase);
        return result > 0;
    }
    
    /**
     * Update case status
     * 
     * @param caseId The case ID
     * @param status The new status
     * @return true if successful
     */
    public boolean updateCaseStatus(int caseId, String status) {
        int result = caseDao.updateCaseStatus(caseId, status);
        return result > 0;
    }
    
    /**
     * Delete a case
     * 
     * @param caseId The case ID
     * @return true if successful
     */
    public boolean deleteCase(int caseId) {
        int result = caseDao.deleteCase(caseId);
        return result > 0;
    }
    
    /**
     * Get all documents for a case
     * 
     * @param caseId The case ID
     * @return List of documents
     */
    public List<Document> getCaseDocuments(int caseId) {
        return documentDao.findDocumentsByCase(caseId);
    }
    
    /**
     * Get all events for a case
     * 
     * @param caseId The case ID
     * @return List of events
     */
    public List<Event> getCaseEvents(int caseId) {
        return eventDao.findEventsByCase(caseId);
    }
    
    /**
     * Get all time entries for a case
     * 
     * @param caseId The case ID
     * @return List of time entries
     */
    public List<TimeEntry> getCaseTimeEntries(int caseId) {
        return timeEntryDao.findTimeEntriesByCase(caseId);
    }
    
    /**
     * Get all unbilled time entries for a case
     * 
     * @param caseId The case ID
     * @return List of unbilled time entries
     */
    public List<TimeEntry> getUnbilledTimeEntries(int caseId) {
        return timeEntryDao.findUnbilledTimeEntriesByCase(caseId);
    }
    
    /**
     * Get total hours for a case
     * 
     * @param caseId The case ID
     * @return Total hours
     */
    public double getTotalHours(int caseId) {
        return timeEntryDao.getTotalHoursByCase(caseId);
    }
    
    /**
     * Get attorneys assigned to a case
     * 
     * @param caseId The case ID
     * @return List of attorneys
     */
    public List<Attorney> getCaseAttorneys(int caseId) {
        return attorneyDao.findAttorneysByCase(caseId);
    }
    
    /**
     * Assign an attorney to a case
     * 
     * @param caseId The case ID
     * @param attorneyId The attorney ID
     * @return true if successful
     */
    public boolean assignAttorneyToCase(int caseId, int attorneyId) {
        try {
            Case legalCase = caseDao.findCaseById(caseId);
            Attorney attorney = attorneyDao.findAttorneyById(attorneyId);
            
            if (legalCase != null && attorney != null) {
                legalCase.addAttorney(attorney);
                return updateCase(legalCase);
            }
            
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}