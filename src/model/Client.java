package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a client in the legal system.
 * Can be either an individual or an organization.
 */
public class Client {
    private int id;
    private String clientId;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private String clientType; // Individual or Organization
    private LocalDate registrationDate;
    private List<Case> cases;
    
    /**
     * Default constructor
     */
    public Client() {
        this.cases = new ArrayList<>();
        this.registrationDate = LocalDate.now();
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param clientId Unique client identifier
     * @param name Client name (person or organization)
     * @param email Client email address
     * @param clientType Type of client (Individual or Organization)
     */
    public Client(String clientId, String name, String email, String clientType) {
        this();
        this.clientId = clientId;
        this.name = name;
        this.email = email;
        this.clientType = clientType;
    }
    
    /**
     * Full constructor
     * 
     * @param id Database ID
     * @param clientId Unique client identifier
     * @param name Client name
     * @param contactPerson Contact person (for organizations)
     * @param email Client email address
     * @param phone Client phone number
     * @param address Client address
     * @param clientType Type of client (Individual or Organization)
     * @param registrationDate Date the client was registered
     */
    public Client(int id, String clientId, String name, String contactPerson, String email, 
                  String phone, String address, String clientType, LocalDate registrationDate) {
        this();
        this.id = id;
        this.clientId = clientId;
        this.name = name;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.clientType = clientType;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
    
    /**
     * Check if this client is an individual
     * 
     * @return true if client type is Individual
     */
    public boolean isIndividual() {
        return "Individual".equalsIgnoreCase(clientType);
    }
    
    /**
     * Check if this client is an organization
     * 
     * @return true if client type is Organization
     */
    public boolean isOrganization() {
        return "Organization".equalsIgnoreCase(clientType);
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }
    
    /**
     * Add a case to this client
     * 
     * @param legalCase The case to add
     */
    public void addCase(Case legalCase) {
        this.cases.add(legalCase);
        legalCase.setClient(this);
    }
    
    /**
     * Gets the display name for the client based on type
     * 
     * @return Name with type indicator for display
     */
    public String getDisplayName() {
        if (isOrganization() && contactPerson != null && !contactPerson.isEmpty()) {
            return name + " (Org, Contact: " + contactPerson + ")";
        } else if (isOrganization()) {
            return name + " (Organization)";
        } else {
            return name + " (Individual)";
        }
    }
    
    @Override
    public String toString() {
        return "Client [id=" + id + ", clientId=" + clientId + ", name=" + name + 
               ", type=" + clientType + ", email=" + email + "]";
    }
}