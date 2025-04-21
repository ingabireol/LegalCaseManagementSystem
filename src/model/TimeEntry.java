package model;

import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * Represents a time entry for billing purposes.
 * Records time spent on a case by an attorney.
 */
public class TimeEntry {
    private int id;
    private String entryId;
    private int caseId;
    private Case associatedCase;
    private int attorneyId;
    private Attorney attorney;
    private LocalDate entryDate;
    private double hours;
    private String description;
    private String activityCode;
    private BigDecimal hourlyRate;
    private boolean billed;
    private int invoiceId; // If billed, reference to invoice
    
    /**
     * Default constructor
     */
    public TimeEntry() {
        this.entryDate = LocalDate.now();
        this.billed = false;
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param entryId Unique time entry identifier
     * @param caseId ID of the associated case
     * @param attorneyId ID of the attorney who worked
     * @param hours Number of hours worked
     * @param description Description of work performed
     */
    public TimeEntry(String entryId, int caseId, int attorneyId, double hours, String description) {
        this();
        this.entryId = entryId;
        this.caseId = caseId;
        this.attorneyId = attorneyId;
        this.hours = hours;
        this.description = description;
    }
    
    /**
     * Full constructor
     * 
     * @param id Database ID
     * @param entryId Unique time entry identifier
     * @param caseId ID of the associated case
     * @param attorneyId ID of the attorney who worked
     * @param entryDate Date the work was performed
     * @param hours Number of hours worked
     * @param description Description of work performed
     * @param activityCode Code categorizing the type of work
     * @param hourlyRate Rate at which work was billed
     * @param billed Whether this time has been billed
     * @param invoiceId ID of the invoice if billed
     */
    public TimeEntry(int id, String entryId, int caseId, int attorneyId, LocalDate entryDate,
                    double hours, String description, String activityCode, BigDecimal hourlyRate,
                    boolean billed, int invoiceId) {
        this();
        this.id = id;
        this.entryId = entryId;
        this.caseId = caseId;
        this.attorneyId = attorneyId;
        this.entryDate = entryDate;
        this.hours = hours;
        this.description = description;
        this.activityCode = activityCode;
        this.hourlyRate = hourlyRate;
        this.billed = billed;
        this.invoiceId = invoiceId;
    }

    // Getters and Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
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

    public int getAttorneyId() {
        return attorneyId;
    }

    public void setAttorneyId(int attorneyId) {
        this.attorneyId = attorneyId;
    }

    public Attorney getAttorney() {
        return attorney;
    }

    public void setAttorney(Attorney attorney) {
        this.attorney = attorney;
        if (attorney != null) {
            this.attorneyId = attorney.getId();
            // Update hourly rate if not set
            if (this.hourlyRate == null) {
                this.hourlyRate = new BigDecimal(attorney.getHourlyRate());
            }
        }
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public boolean isBilled() {
        return billed;
    }

    public void setBilled(boolean billed) {
        this.billed = billed;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
        if (invoiceId > 0) {
            this.billed = true;
        }
    }
    
    /**
     * Calculate the amount for this time entry
     * 
     * @return Hours multiplied by hourly rate
     */
    public BigDecimal getAmount() {
        if (hourlyRate == null) {
            return BigDecimal.ZERO;
        }
        return hourlyRate.multiply(new BigDecimal(hours));
    }
    
    /**
     * Format hours as a string with quarter-hour precision
     * 
     * @return Formatted hours string
     */
    public String getFormattedHours() {
        int hours = (int) this.hours;
        int minutes = (int) Math.round((this.hours - hours) * 60);
        return String.format("%d:%02d", hours, minutes);
    }
    
    @Override
    public String toString() {
        return "TimeEntry [id=" + id + ", entryId=" + entryId + ", case=" + caseId + 
               ", attorney=" + attorneyId + ", hours=" + hours + ", billed=" + billed + "]";
    }
}