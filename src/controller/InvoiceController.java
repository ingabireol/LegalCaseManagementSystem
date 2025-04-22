package controller;

import dao.InvoiceDao;
import dao.PaymentDao;
import dao.TimeEntryDao;
import model.Invoice;
import model.Payment;
import model.TimeEntry;

import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * Controller for invoice-related operations.
 */
public class InvoiceController {
    private InvoiceDao invoiceDao;
    private PaymentDao paymentDao;
    private TimeEntryDao timeEntryDao;
    
    /**
     * Constructor
     */
    public InvoiceController() {
        this.invoiceDao = new InvoiceDao();
        this.paymentDao = new PaymentDao();
        this.timeEntryDao = new TimeEntryDao();
    }
    
    /**
     * Get all invoices
     * 
     * @return List of all invoices
     */
    public List<Invoice> getAllInvoices() {
        return invoiceDao.findAllInvoices();
    }
    
    /**
     * Get an invoice by ID
     * 
     * @param id The invoice ID
     * @return The invoice
     */
    public Invoice getInvoiceById(int id) {
        return invoiceDao.findInvoiceById(id);
    }
    
    /**
     * Get an invoice by invoice number
     * 
     * @param invoiceNumber The invoice number
     * @return The invoice
     */
    public Invoice getInvoiceByInvoiceNumber(String invoiceNumber) {
        return invoiceDao.findInvoiceByInvoiceNumber(invoiceNumber);
    }
    
    /**
     * Get an invoice with all details
     * 
     * @param id The invoice ID
     * @return The invoice with all details loaded
     */
    public Invoice getInvoiceWithDetails(int id) {
        return invoiceDao.getInvoiceWithDetails(id);
    }
    
    /**
     * Find invoices by client
     * 
     * @param clientId The client ID
     * @return List of invoices for the client
     */
    public List<Invoice> findInvoicesByClient(int clientId) {
        return invoiceDao.findInvoicesByClient(clientId);
    }
    
    /**
     * Find invoices by case
     * 
     * @param caseId The case ID
     * @return List of invoices for the case
     */
    public List<Invoice> findInvoicesByCase(int caseId) {
        return invoiceDao.findInvoicesByCase(caseId);
    }
    
    /**
     * Find invoices by status
     * 
     * @param status The status
     * @return List of invoices with the status
     */
    public List<Invoice> findInvoicesByStatus(String status) {
        return invoiceDao.findInvoicesByStatus(status);
    }
    
    /**
     * Find invoices by date range
     * 
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of invoices in the date range
     */
    public List<Invoice> findInvoicesByDateRange(LocalDate startDate, LocalDate endDate) {
        return invoiceDao.findInvoicesByDateRange(startDate, endDate);
    }
    
    /**
     * Find overdue invoices
     * 
     * @return List of overdue invoices
     */
    public List<Invoice> findOverdueInvoices() {
        return invoiceDao.findOverdueInvoices();
    }
    
    /**
     * Create a new invoice
     * 
     * @param invoice The invoice to create
     * @return true if successful
     */
    public boolean createInvoice(Invoice invoice) {
        // Set issue date and due date if not set
        if (invoice.getIssueDate() == null) {
            invoice.setIssueDate(LocalDate.now());
        }
        
        if (invoice.getDueDate() == null) {
            invoice.setDueDate(invoice.getIssueDate().plusDays(30)); // Default due date: 30 days
        }
        
        int result = invoiceDao.createInvoice(invoice);
        return result > 0;
    }
    
    /**
     * Create a new invoice from unbilled time entries
     * 
     * @param caseId The case ID
     * @param dueDate The due date
     * @return The created invoice, or null if creation failed
     */
    public Invoice createInvoiceFromTimeEntries(int caseId, LocalDate dueDate) {
        // Generate invoice number
        String invoiceNumber = invoiceDao.generateNextInvoiceNumber();
        
        return invoiceDao.createInvoiceFromUnbilledTimeEntries(caseId, invoiceNumber, dueDate);
    }
    
    /**
     * Update an existing invoice
     * 
     * @param invoice The invoice to update
     * @return true if successful
     */
    public boolean updateInvoice(Invoice invoice) {
        int result = invoiceDao.updateInvoice(invoice);
        return result > 0;
    }
    
    /**
     * Update invoice status
     * 
     * @param invoiceId The invoice ID
     * @param status The new status
     * @return true if successful
     */
    public boolean updateInvoiceStatus(int invoiceId, String status) {
        int result = invoiceDao.updateInvoiceStatus(invoiceId, status);
        return result > 0;
    }
    
    /**
     * Delete an invoice
     * 
     * @param invoiceId The invoice ID
     * @return true if successful
     */
    public boolean deleteInvoice(int invoiceId) {
        int result = invoiceDao.deleteInvoice(invoiceId);
        return result > 0;
    }
    
    /**
     * Update the paid amount for an invoice
     * 
     * @param invoiceId The invoice ID
     * @return true if successful
     */
    public boolean updateInvoicePaidAmount(int invoiceId) {
        int result = invoiceDao.updateInvoicePaidAmount(invoiceId);
        return result > 0;
    }
    
    /**
     * Get time entries for an invoice
     * 
     * @param invoiceId The invoice ID
     * @return List of time entries
     */
    public List<TimeEntry> getInvoiceTimeEntries(int invoiceId) {
        return timeEntryDao.findTimeEntriesByInvoice(invoiceId);
    }
    
    /**
     * Get payments for an invoice
     * 
     * @param invoiceId The invoice ID
     * @return List of payments
     */
    public List<Payment> getInvoicePayments(int invoiceId) {
        return paymentDao.findPaymentsByInvoice(invoiceId);
    }
    
    /**
     * Record a payment for an invoice
     * 
     * @param payment The payment to record
     * @return true if successful
     */
    public boolean recordPayment(Payment payment) {
        // Generate payment ID if not set
        if (payment.getPaymentId() == null || payment.getPaymentId().isEmpty()) {
            payment.setPaymentId(paymentDao.generateNextPaymentId());
        }
        
        // Set payment date if not set
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }
        
        int result = paymentDao.createPayment(payment);
        
        if (result > 0) {
            // Update invoice paid amount
            updateInvoicePaidAmount(payment.getInvoiceId());
            return true;
        }
        
        return false;
    }
    
    /**
     * Delete a payment
     * 
     * @param paymentId The payment ID
     * @return true if successful
     */
    public boolean deletePayment(int paymentId) {
        int result = paymentDao.deletePayment(paymentId);
        return result > 0;
    }
    
    /**
     * Get available payment methods
     * 
     * @return Array of payment methods
     */
    public String[] getPaymentMethods() {
        return new String[] {
            "Cash", "Check", "Credit Card", "Bank Transfer", "Wire Transfer", "PayPal", "Other"
        };
    }
}