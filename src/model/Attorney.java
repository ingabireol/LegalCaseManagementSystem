package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an attorney in the legal system.
 */
public class Attorney {
    private int id;
    private String attorneyId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String specialization;
    private String barNumber;
    private double hourlyRate;
    private List<Case> cases;
    
    /**
     * Default constructor
     */
    public Attorney() {
        this.cases = new ArrayList<>();
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param attorneyId Unique attorney identifier
     * @param firstName Attorney's first name
     * @param lastName Attorney's last name
     * @param email Attorney's email address
     */
    public Attorney(String attorneyId, String firstName, String lastName, String email) {
        this();
        this.attorneyId = attorneyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    /**
     * Full constructor
     * 
     * @param id Database ID
     * @param attorneyId Unique attorney identifier
     * @param firstName Attorney's first name
     * @param lastName Attorney's last name
     * @param email Attorney's email address
     * @param phone Attorney's phone number
     * @param specialization Attorney's area of specialization
     * @param barNumber Attorney's bar number
     * @param hourlyRate Attorney's hourly billing rate
     */
    public Attorney(int id, String attorneyId, String firstName, String lastName, 
                   String email, String phone, String specialization, 
                   String barNumber, double hourlyRate) {
        this();
        this.id = id;
        this.attorneyId = attorneyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.barNumber = barNumber;
        this.hourlyRate = hourlyRate;
    }

    // Getters and Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAttorneyId() {
        return attorneyId;
    }

    public void setAttorneyId(String attorneyId) {
        this.attorneyId = attorneyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Gets the full name of the attorney
     * 
     * @return Full name (first name + last name)
     */
    public String getFullName() {
        return firstName + " " + lastName;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getBarNumber() {
        return barNumber;
    }

    public void setBarNumber(String barNumber) {
        this.barNumber = barNumber;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }
    
    /**
     * Add a case to this attorney
     * 
     * @param legalCase The case to add
     */
    public void addCase(Case legalCase) {
        if (!cases.contains(legalCase)) {
            this.cases.add(legalCase);
            if (!legalCase.getAttorneys().contains(this)) {
                legalCase.addAttorney(this);
            }
        }
    }
    
    /**
     * Get attorney's display name with specialization
     * 
     * @return Formatted name with specialization for display
     */
    public String getDisplayName() {
        if (specialization != null && !specialization.isEmpty()) {
            return getFullName() + " (" + specialization + ")";
        }
        return getFullName();
    }
    
    @Override
    public String toString() {
        return "Attorney [id=" + id + ", attorneyId=" + attorneyId + ", name=" + getFullName() + 
               ", specialization=" + specialization + "]";
    }
}