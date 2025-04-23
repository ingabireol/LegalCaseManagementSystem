package controller;

import dao.TimeEntryDao;
import dao.AttorneyDao;
import dao.CaseDao;
import model.TimeEntry;
import model.Attorney;

import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import model.Case;

/**
 * Controller for time entry operations.
 */
public class TimeEntryController {
    private TimeEntryDao timeEntryDao;
    private AttorneyDao attorneyDao;
    
    /**
     * Constructor
     */
    public TimeEntryController() {
        this.timeEntryDao = new TimeEntryDao();
        this.attorneyDao = new AttorneyDao();
    }
    
    /**
     * Get all time entries
     * 
     * @return List of all time entries
     */
    public List<TimeEntry> getAllTimeEntries() {
        return timeEntryDao.findAllTimeEntries();
    }
    
    /**
     * Get a time entry by ID
     * 
     * @param id The time entry ID
     * @return The time entry
     */
    public TimeEntry getTimeEntryById(int id) {
        return timeEntryDao.findTimeEntryById(id);
    }
    
    /**
     * Get a time entry by entry ID
     * 
     * @param entryId The entry ID string
     * @return The time entry
     */
    public TimeEntry getTimeEntryByEntryId(String entryId) {
        return timeEntryDao.findTimeEntryByEntryId(entryId);
    }
    
    /**
     * Get a time entry with details
     * 
     * @param id The time entry ID
     * @return The time entry with details loaded
     */
    public TimeEntry getTimeEntryWithDetails(int id) {
        return timeEntryDao.getTimeEntryWithDetails(id);
    }
    
    /**
     * Find time entries by case
     * 
     * @param caseId The case ID
     * @return List of time entries for the case
     */
    public List<TimeEntry> findTimeEntriesByCase(int caseId) {
        return timeEntryDao.findTimeEntriesByCase(caseId);
    }
    
    /**
     * Find time entries by attorney
     * 
     * @param attorneyId The attorney ID
     * @return List of time entries for the attorney
     */
    public List<TimeEntry> findTimeEntriesByAttorney(int attorneyId) {
        return timeEntryDao.findTimeEntriesByAttorney(attorneyId);
    }
    
    /**
     * Find time entries by date range
     * 
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of time entries in the date range
     */
    public List<TimeEntry> findTimeEntriesByDateRange(LocalDate startDate, LocalDate endDate) {
        return timeEntryDao.findTimeEntriesByDateRange(startDate, endDate);
    }
    
    /**
     * Find unbilled time entries for a case
     * 
     * @param caseId The case ID
     * @return List of unbilled time entries
     */
    public List<TimeEntry> findUnbilledTimeEntries(int caseId) {
        return timeEntryDao.findUnbilledTimeEntriesByCase(caseId);
    }
    
    /**
     * Find time entries by invoice
     * 
     * @param invoiceId The invoice ID
     * @return List of time entries for the invoice
     */
    public List<TimeEntry> findTimeEntriesByInvoice(int invoiceId) {
        return timeEntryDao.findTimeEntriesByInvoice(invoiceId);
    }
    
    /**
     * Create a new time entry
     * 
     * @param timeEntry The time entry to create
     * @return true if successful
     */
    public boolean createTimeEntry(TimeEntry timeEntry) {
        // Set entry date if not set
        if (timeEntry.getEntryDate() == null) {
            timeEntry.setEntryDate(LocalDate.now());
        }
        
        // Set hourly rate based on attorney if not set
        if (timeEntry.getHourlyRate() == null && timeEntry.getAttorneyId() > 0) {
            Attorney attorney = attorneyDao.findAttorneyById(timeEntry.getAttorneyId());
            if (attorney != null) {
                timeEntry.setHourlyRate(new BigDecimal(attorney.getHourlyRate()));
            }
        }
        
        int result = timeEntryDao.createTimeEntry(timeEntry);
        return result > 0;
    }
    
    /**
     * Update an existing time entry
     * 
     * @param timeEntry The time entry to update
     * @return true if successful
     */
    public boolean updateTimeEntry(TimeEntry timeEntry) {
        int result = timeEntryDao.updateTimeEntry(timeEntry);
        return result > 0;
    }
    
    /**
     * Mark a time entry as billed
     * 
     * @param timeEntryId The time entry ID
     * @param invoiceId The invoice ID
     * @return true if successful
     */
    public boolean markTimeEntryAsBilled(int timeEntryId, int invoiceId) {
        int result = timeEntryDao.markTimeEntryAsBilled(timeEntryId, invoiceId);
        return result > 0;
    }
    
    /**
     * Mark all time entries for a case as billed
     * 
     * @param caseId The case ID
     * @param invoiceId The invoice ID
     * @return true if successful
     */
    public boolean markCaseTimeEntriesAsBilled(int caseId, int invoiceId) {
        int result = timeEntryDao.markCaseTimeEntriesAsBilled(caseId, invoiceId);
        return result > 0;
    }
    
    /**
     * Delete a time entry
     * 
     * @param timeEntryId The time entry ID
     * @return true if successful
     */
    public boolean deleteTimeEntry(int timeEntryId) {
        int result = timeEntryDao.deleteTimeEntry(timeEntryId);
        return result > 0;
    }
    
    /**
     * Get total hours for a case
     * 
     * @param caseId The case ID
     * @return Total hours
     */
    public double getTotalHoursByCase(int caseId) {
        return timeEntryDao.getTotalHoursByCase(caseId);
    }
    
    /**
     * Get total billable amount for a case
     * 
     * @param caseId The case ID
     * @return Total billable amount
     */
    public BigDecimal getTotalAmountByCase(int caseId) {
        return timeEntryDao.getTotalAmountByCase(caseId);
    }
    
    /**
     * Get available activity codes
     * 
     * @return Array of activity codes
     */
    public String[] getActivityCodes() {
        return new String[] {
            "RES", // Research
            "DRA", // Drafting
            "REV", // Review
            "COM", // Communication
            "MEE", // Meeting
            "HEA", // Hearing
            "TRI", // Trial
            "DEP", // Deposition
            "TRA", // Travel
            "NEG", // Negotiation
            "OTH"  // Other
        };
    }

   /**
 * Get all time entries for a specific case
 * 
 * @param caseId The case ID
 * @return List of time entries for the case
 */
public List<TimeEntry> getCaseTimeEntries(int caseId) {
    if (caseId <= 0) {
        throw new IllegalArgumentException("Invalid case ID: " + caseId);
    }
    CaseDao caseDao = new CaseDao();
    // Validate that the case exists
    Case legalCase = caseDao.findCaseById(caseId);
    if (legalCase == null) {
        throw new IllegalArgumentException("Case not found with ID: " + caseId);
    }
    
    // Use the existing DAO method to fetch time entries for this case
    return timeEntryDao.findTimeEntriesByCase(caseId);
}

/**
 * Get unbilled time entries for a specific case
 * 
 * @param caseId The case ID
 * @return List of unbilled time entries for the case
 */
public List<TimeEntry> getUnbilledTimeEntries(int caseId) {
    if (caseId <= 0) {
        throw new IllegalArgumentException("Invalid case ID: " + caseId);
    }
    CaseDao caseDao = new CaseDao();
    
    // Validate that the case exists
    Case legalCase = caseDao.findCaseById(caseId);
    if (legalCase == null) {
        throw new IllegalArgumentException("Case not found with ID: " + caseId);
    }
    
    // Use the existing DAO method to fetch unbilled time entries for this case
    return timeEntryDao.findUnbilledTimeEntriesByCase(caseId);
}
}