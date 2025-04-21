package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an invoice in the legal system.
 * Used for billing clients for legal services.
 */
public class Invoice {
    private int id;
    private String invoiceNumber;
    private int clientId;
    private Client client;
    private int caseId;
    private Case legalCase;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private BigDecimal amount;
    private BigDecimal amountPaid;
    private String status;
    private String notes;
    private List<TimeEntry> timeEntries;
    private List<Payment> payments;
    
    // Status constants
    public static final String STATUS_DRAFT = "Draft";
    public static final String STATUS_ISSUED = "Issued";
    public static final String STATUS_PAID = "Paid";
    public static final String STATUS_PARTIALLY_PAID = "Partially Paid";
    public static final String STATUS_OVERDUE = "Overdue";
    public static final String STATUS_CANCELLED = "Cancelled";
    
    /**
     * Default constructor
     */
    public Invoice() {
        this.timeEntries = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.issueDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(30); // Default due date: 30 days
        this.amount = BigDecimal.ZERO;
        this.amountPaid = BigDecimal.ZERO;
        this.status = STATUS_DRAFT;
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param invoiceNumber Unique invoice identifier
     * @param clientId ID of the client to bill
     * @param caseId ID of the associated case
     * @param amount Invoice amount
     */
    public Invoice(String invoiceNumber, int clientId, int caseId, BigDecimal amount) {
        this();
        this.invoiceNumber = invoiceNumber;
        this.clientId = clientId;
        this.caseId = caseId;
        this.amount = amount;
    }
    
    /**
     * Full constructor
     * 
     * @param id Database ID
     * @param invoiceNumber Unique invoice identifier
     * @param clientId ID of the client to bill
     * @param caseId ID of the associated case
     * @param issueDate Date the invoice was issued
     * @param dueDate Date the invoice is due
     * @param amount Invoice amount
     * @param amountPaid Amount that has been paid
     * @param status Invoice status
     * @param notes Additional notes about the invoice
     */
    public Invoice(int id, String invoiceNumber, int clientId, int caseId, LocalDate issueDate,
                  LocalDate dueDate, BigDecimal amount, BigDecimal amountPaid, String status, String notes) {
        this();
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.clientId = clientId;
        this.caseId = caseId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.amount = amount;
        this.amountPaid = amountPaid;
        this.status = status;
        this.notes = notes;
    }

    // Getters and Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        if (client != null) {
            this.clientId = client.getId();
        }
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public Case getCase() {
        return legalCase;
    }

    public void setCase(Case legalCase) {
        this.legalCase = legalCase;
        if (legalCase != null) {
            this.caseId = legalCase.getId();
        }
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        updateStatus();
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
        updateStatus();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    public void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
        // Mark all time entries as billed
        for (TimeEntry entry : timeEntries) {
            entry.setBilled(true);
            entry.setInvoiceId(this.id);
        }
        // Recalculate amount
        recalculateAmount();
    }
    
    /**
     * Add a time entry to this invoice
     * 
     * @param entry The time entry to add
     */
    public void addTimeEntry(TimeEntry entry) {
        this.timeEntries.add(entry);
        entry.setBilled(true);
        entry.setInvoiceId(this.id);
        recalculateAmount();
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
        recalculateAmountPaid();
    }
    
    /**
     * Add a payment to this invoice
     * 
     * @param payment The payment to add
     */
    public void addPayment(Payment payment) {
        this.payments.add(payment);
        recalculateAmountPaid();
    }
    
    /**
     * Recalculate the invoice amount based on time entries
     */
    private void recalculateAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (TimeEntry entry : timeEntries) {
            total = total.add(entry.getAmount());
        }
        this.amount = total;
        updateStatus();
    }
    
    /**
     * Recalculate the amount paid based on payments
     */
    public void recalculateAmountPaid() {
        BigDecimal total = BigDecimal.ZERO;
        for (Payment payment : payments) {
            total = total.add(payment.getAmount());
        }
        this.amountPaid = total;
        updateStatus();
    }
    
    /**
     * Calculate the remaining balance on this invoice
     * 
     * @return Balance remaining to be paid
     */
    public BigDecimal getBalance() {
        return amount.subtract(amountPaid);
    }
    
    /**
     * Check if the invoice is fully paid
     * 
     * @return true if amount paid equals or exceeds invoice amount
     */
    public boolean isFullyPaid() {
        return amountPaid.compareTo(amount) >= 0;
    }
    
    /**
     * Check if the invoice is overdue
     * 
     * @return true if due date is in the past and invoice is not paid
     */
    public boolean isOverdue() {
        return dueDate.isBefore(LocalDate.now()) && !isFullyPaid() && 
               !STATUS_CANCELLED.equals(status);
    }
    
    /**
     * Updates the invoice status based on payments and due date
     */
    public void updateStatus() {
        if (STATUS_DRAFT.equals(status) || STATUS_CANCELLED.equals(status)) {
            // Don't change draft or cancelled status
            return;
        }
        
        if (isFullyPaid()) {
            this.status = STATUS_PAID;
        } else if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
            this.status = STATUS_PARTIALLY_PAID;
        } else if (isOverdue()) {
            this.status = STATUS_OVERDUE;
        } else {
            this.status = STATUS_ISSUED;
        }
    }
    
    @Override
    public String toString() {
        return "Invoice [id=" + id + ", invoiceNumber=" + invoiceNumber + 
               ", amount=" + amount + ", status=" + status + "]";
    }
}