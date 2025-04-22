package controller;

import dao.AttorneyDao;
import model.Attorney;
import model.Case;

import java.util.List;

/**
 * Controller for attorney-related operations.
 */
public class AttorneyController {
    private AttorneyDao attorneyDao;
    
    /**
     * Constructor
     */
    public AttorneyController() {
        this.attorneyDao = new AttorneyDao();
    }
    
    /**
     * Get all attorneys
     * 
     * @return List of all attorneys
     */
    public List<Attorney> getAllAttorneys() {
        return attorneyDao.findAllAttorneys();
    }
    
    /**
     * Get an attorney by ID
     * 
     * @param id The attorney ID
     * @return The attorney
     */
    public Attorney getAttorneyById(int id) {
        return attorneyDao.findAttorneyById(id);
    }
    
    /**
     * Get an attorney by attorney ID
     * 
     * @param attorneyId The attorney ID string
     * @return The attorney
     */
    public Attorney getAttorneyByAttorneyId(String attorneyId) {
        return attorneyDao.findAttorneyByAttorneyId(attorneyId);
    }
    
    /**
     * Get an attorney with all cases
     * 
     * @param id The attorney ID
     * @return The attorney with all cases loaded
     */
    public Attorney getAttorneyWithCases(int id) {
        return attorneyDao.getAttorneyWithCases(id);
    }
    
    /**
     * Find attorneys by name
     * 
     * @param name Name to search for
     * @return List of matching attorneys
     */
    public List<Attorney> findAttorneysByName(String name) {
        return attorneyDao.findAttorneysByName(name);
    }
    
    /**
     * Find attorneys by specialization
     * 
     * @param specialization Specialization to search for
     * @return List of matching attorneys
     */
    public List<Attorney> findAttorneysBySpecialization(String specialization) {
        return attorneyDao.findAttorneysBySpecialization(specialization);
    }
    
    /**
     * Create a new attorney
     * 
     * @param attorney The attorney to create
     * @return true if successful
     */
    public boolean createAttorney(Attorney attorney) {
        int result = attorneyDao.createAttorney(attorney);
        return result > 0;
    }
    
    /**
     * Update an existing attorney
     * 
     * @param attorney The attorney to update
     * @return true if successful
     */
    public boolean updateAttorney(Attorney attorney) {
        int result = attorneyDao.updateAttorney(attorney);
        return result > 0;
    }
    
    /**
     * Delete an attorney
     * 
     * @param attorneyId The attorney ID to delete
     * @return true if successful
     */
    public boolean deleteAttorney(String attorneyId) {
        Attorney attorney = attorneyDao.findAttorneyByAttorneyId(attorneyId);
        if (attorney == null) {
            return false;
        }
        
        int result = attorneyDao.deleteAttorney(attorney.getId());
        return result > 0;
    }
    
    /**
     * Get cases for an attorney
     * 
     * @param attorneyId The attorney ID
     * @return List of cases
     */
    public List<Case> getAttorneyCases(int attorneyId) {
        return attorneyDao.findCasesByAttorney(attorneyId);
    }
}