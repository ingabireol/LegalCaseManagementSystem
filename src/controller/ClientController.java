package controller;

import dao.ClientDao;
import dao.CaseDao;
import model.Client;
import model.Case;

import java.util.List;
import java.time.LocalDate;

/**
 * Controller for client-related operations.
 */
public class ClientController {
    private ClientDao clientDao;
    private CaseDao caseDao;
    
    /**
     * Constructor
     */
    public ClientController() {
        this.clientDao = new ClientDao();
        this.caseDao = new CaseDao();
    }
    
    /**
     * Get all clients
     * 
     * @return List of all clients
     */
    public List<Client> getAllClients() {
        return clientDao.findAllClients();
    }
    
    /**
     * Get a client by ID
     * 
     * @param id The client ID
     * @return The client
     */
    public Client getClientById(int id) {
        return clientDao.findClientById(id);
    }
    
    /**
     * Get a client by client ID
     * 
     * @param clientId The client ID string
     * @return The client
     */
    public Client getClientByClientId(String clientId) {
        return clientDao.findClientByClientId(clientId);
    }
    
    /**
     * Get a client with all cases
     * 
     * @param id The client ID
     * @return The client with all cases loaded
     */
    public Client getClientWithCases(int id) {
        return clientDao.getClientWithCases(id);
    }
    
    /**
     * Find clients by name
     * 
     * @param name Name to search for
     * @return List of matching clients
     */
    public List<Client> findClientsByName(String name) {
        return clientDao.findClientsByName(name);
    }
    
    /**
     * Find clients by type
     * 
     * @param clientType Type to search for
     * @return List of matching clients
     */
    public List<Client> findClientsByType(String clientType) {
        return clientDao.findClientsByType(clientType);
    }
    
    /**
     * Create a new client
     * 
     * @param client The client to create
     * @return true if successful
     */
    public boolean createClient(Client client) {
        // Set registration date if not set
        if (client.getRegistrationDate() == null) {
            client.setRegistrationDate(LocalDate.now());
        }
        
        int result = clientDao.createClient(client);
        return result > 0;
    }
    
    /**
     * Update an existing client
     * 
     * @param client The client to update
     * @return true if successful
     */
    public boolean updateClient(Client client) {
        int result = clientDao.updateClient(client);
        return result > 0;
    }
    
    /**
     * Delete a client
     * 
     * @param clientId The client ID to delete
     * @return true if successful
     */
    public boolean deleteClient(String clientId) {
        Client client = clientDao.findClientByClientId(clientId);
        if (client == null) {
            return false;
        }
        
        // Check if client has cases
        List<Case> cases = caseDao.findCasesByClient(client.getId());
        if (cases != null && !cases.isEmpty()) {
            return false; // Cannot delete client with cases
        }
        
        int result = clientDao.deleteClient(client.getId());
        return result > 0;
    }
    
    /**
     * Get cases for a client
     * 
     * @param clientId The client ID
     * @return List of cases
     */
    public List<Case> getClientCases(int clientId) {
        return caseDao.findCasesByClient(clientId);
    }
}