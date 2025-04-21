package model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a payment made by a client for an invoice.
 */
public class Payment {
    private int id;
    private String paymentId;
    private int invoiceId;
    private Invoice invoice;
    private int clientId;
    private Client client;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String paymentMethod;
    private String reference;
    private String notes;
    
    /**
     * Default constructor
     */
    public Payment() {
        this.paymentDate = LocalDate.now();
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param paymentId Unique payment identifier
     * @param invoiceId ID of the invoice being paid
     * @param amount Payment amount
     * @param paymentMethod Method of payment
     */
    public Payment(String paymentId, int invoiceId, BigDecimal amount, String paymentMethod) {
        this();
        this.paymentId = paymentId;
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
    
    /**
     * Full constructor
     * 
     * @param id Database ID
     * @param paymentId Unique payment identifier
     * @param invoiceId ID of the invoice being paid
     * @param clientId ID of the client making payment
     * @param paymentDate Date the payment was made
     * @param amount Payment amount
     * @param paymentMethod Method of payment
     * @param reference Reference number (check number, transaction ID, etc.)
     * @param notes Additional notes about the payment
     */
    public Payment(int id, String paymentId, int invoiceId, int clientId, LocalDate paymentDate,
                  BigDecimal amount, String paymentMethod, String reference, String notes) {
        this();
        this.id = id;
        this.paymentId = paymentId;
        this.invoiceId = invoiceId;
        this.clientId = clientId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.reference = reference;
        this.notes = notes;
    }

    // Getters and Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
        if (invoice != null) {
            this.invoiceId = invoice.getId();
            this.clientId = invoice.getClientId();
        }
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

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        // Update invoice payment amount if invoice is set
        if (invoice != null) {
            invoice.recalculateAmountPaid();
        }
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    /**
     * Get formatted display text for the payment
     * 
     * @return Formatted payment information
     */
    public String getDisplayText() {
        return paymentMethod + " payment of " + amount + " on " + paymentDate + 
               (reference != null && !reference.isEmpty() ? " (Ref: " + reference + ")" : "");
    }
    
    @Override
    public String toString() {
        return "Payment [id=" + id + ", paymentId=" + paymentId + ", invoiceId=" + invoiceId +
               ", amount=" + amount + ", date=" + paymentDate + ", method=" + paymentMethod + "]";
    }
}