package dao;

import model.Client;
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
 * Data Access Object for Client operations.
 */
public class ClientDao {
    private String db_url = "jdbc:mysql://localhost:3306/legalcasemgmtdb";
    private String db_username = "root";
    private String db_passwd = "Ornella12345!";
    
    /**
     * Creates a new client in the database
     * 
     * @param client The client to create
     * @return Number of rows affected
     */
    public int createClient(Client client) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "INSERT INTO clients (client_id, name, contact_person, email, phone, address, " +
                         "client_type, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, client.getClientId());
            pst.setString(2, client.getName());
            pst.setString(3, client.getContactPerson());
            pst.setString(4, client.getEmail());
            pst.setString(5, client.getPhone());
            pst.setString(6, client.getAddress());
            pst.setString(7, client.getClientType());
            
            // Handle registration date
            if (client.getRegistrationDate() != null) {
                pst.setDate(8, Date.valueOf(client.getRegistrationDate()));
            } else {
                pst.setDate(8, Date.valueOf(LocalDate.now()));
            }
            
            // Execute statement
            int rowsAffected = pst.executeUpdate();
            
            // Get generated ID
            if (rowsAffected > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    client.setId(rs.getInt(1));
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
     * Updates an existing client in the database
     * 
     * @param client The client to update
     * @return Number of rows affected
     */
    public int updateClient(Client client) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "UPDATE clients SET client_id = ?, name = ?, contact_person = ?, " +
                         "email = ?, phone = ?, address = ?, client_type = ?, registration_date = ? " +
                         "WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, client.getClientId());
            pst.setString(2, client.getName());
            pst.setString(3, client.getContactPerson());
            pst.setString(4, client.getEmail());
            pst.setString(5, client.getPhone());
            pst.setString(6, client.getAddress());
            pst.setString(7, client.getClientType());
            
            // Handle registration date
            if (client.getRegistrationDate() != null) {
                pst.setDate(8, Date.valueOf(client.getRegistrationDate()));
            } else {
                pst.setDate(8, Date.valueOf(LocalDate.now()));
            }
            
            pst.setInt(9, client.getId());
            
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
     * Finds a client by ID
     * 
     * @param id The client ID to search for
     * @return The client if found, null otherwise
     */
    public Client findClientById(int id) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM clients WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            
            ResultSet rs = pst.executeQuery();
            Client client = null;
            
            if (rs.next()) {
                client = extractClientFromResultSet(rs);
            }
            
            con.close();
            return client;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds a client by client ID
     * 
     * @param clientId The client ID to search for
     * @return The client if found, null otherwise
     */
    public Client findClientByClientId(String clientId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM clients WHERE client_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, clientId);
            
            ResultSet rs = pst.executeQuery();
            Client client = null;
            
            if (rs.next()) {
                client = extractClientFromResultSet(rs);
            }
            
            con.close();
            return client;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds clients by name or contact person
     * 
     * @param name The name to search for
     * @return List of matching clients
     */
    public List<Client> findClientsByName(String name) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM clients WHERE name LIKE ? OR contact_person LIKE ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            String searchPattern = "%" + name + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            
            ResultSet rs = pst.executeQuery();
            List<Client> clientList = new ArrayList<>();
            
            while (rs.next()) {
                Client client = extractClientFromResultSet(rs);
                clientList.add(client);
            }
            
            con.close();
            return clientList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds a client by email
     * 
     * @param email The email to search for
     * @return The client if found, null otherwise
     */
    public Client findClientByEmail(String email) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM clients WHERE email = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, email);
            
            ResultSet rs = pst.executeQuery();
            Client client = null;
            
            if (rs.next()) {
                client = extractClientFromResultSet(rs);
            }
            
            con.close();
            return client;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds clients by client type
     * 
     * @param clientType The client type to search for
     * @return List of matching clients
     */
    public List<Client> findClientsByType(String clientType) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM clients WHERE client_type = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, clientType);
            
            ResultSet rs = pst.executeQuery();
            List<Client> clientList = new ArrayList<>();
            
            while (rs.next()) {
                Client client = extractClientFromResultSet(rs);
                clientList.add(client);
            }
            
            con.close();
            return clientList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Gets all clients
     * 
     * @return List of all clients
     */
    public List<Client> findAllClients() {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM clients";
            PreparedStatement pst = con.prepareStatement(sql);
            
            ResultSet rs = pst.executeQuery();
            List<Client> clientList = new ArrayList<>();
            
            while (rs.next()) {
                Client client = extractClientFromResultSet(rs);
                clientList.add(client);
            }
            
            con.close();
            return clientList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Extract client data from a ResultSet row
     * 
     * @param rs ResultSet positioned at the client row
     * @return Client object populated with data
     * @throws Exception If an error occurs
     */
    private Client extractClientFromResultSet(ResultSet rs) throws Exception {
        Client client = new Client();
        
        client.setId(rs.getInt("id"));
        client.setClientId(rs.getString("client_id"));
        client.setName(rs.getString("name"));
        client.setContactPerson(rs.getString("contact_person"));
        client.setEmail(rs.getString("email"));
        client.setPhone(rs.getString("phone"));
        client.setAddress(rs.getString("address"));
        client.setClientType(rs.getString("client_type"));
        
        // Handle registration date
        Date registrationDate = rs.getDate("registration_date");
        if (registrationDate != null) {
            client.setRegistrationDate(registrationDate.toLocalDate());
        }
        
        return client;
    }
    
    /**
     * Gets a client with all their cases
     * 
     * @param clientId The ID of the client
     * @return The client with cases loaded
     */
    public Client getClientWithCases(int clientId) {
        try {
            // First get the client
            Client client = findClientById(clientId);
            if (client == null) {
                return null;
            }
            
            // Then get their cases
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM cases WHERE client_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, clientId);
            
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
                legalCase.setClientId(clientId);
                legalCase.setClient(client);
                
                cases.add(legalCase);
            }
            
            client.setCases(cases);
            con.close();
            return client;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Deletes a client from the database
     * 
     * @param clientId The ID of the client to delete
     * @return Number of rows affected
     */
    public int deleteClient(int clientId) {
        Connection con = null;
        try {
            // Check if client has cases
            CaseDao caseDao = new CaseDao();
            List<Case> clientCases = caseDao.findCasesByClient(clientId);
            if (clientCases != null && !clientCases.isEmpty()) {
                // Cannot delete client with cases
                return 0;
            }
            
            // Create connection
            con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "DELETE FROM clients WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, clientId);
            
            int rowsAffected = pst.executeUpdate();
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}