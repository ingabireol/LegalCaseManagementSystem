package dao;

import model.Invoice;
import model.Client;
import model.Case;
import model.TimeEntry;
import model.Payment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data Access Object for Invoice operations.
 */
public class InvoiceDao {
    private String db_url = "jdbc:mysql://localhost:3306/legalcasemgmtdb";
    private String db_username = "root";
    private String db_passwd = "Ornella12345!";
    
    // Other DAOs for related entities
    private ClientDao clientDao;
    private CaseDao caseDao;
    private TimeEntryDao timeEntryDao;
    
    /**
     * Constructor
     */
    public InvoiceDao() {
        this.clientDao = new ClientDao();
        this.caseDao = new CaseDao();
        this.timeEntryDao = new TimeEntryDao();
    }
    
    /**
     * Creates a new invoice in the database
     * 
     * @param invoice The invoice to create
     * @return Number of rows affected
     */
    public int createInvoice(Invoice invoice) {
        Connection con = null;
        try {
            // Create connection
            con = DriverManager.getConnection(db_url, db_username, db_passwd);
            con.setAutoCommit(false);  // Start transaction
            
            // Prepare statement
            String sql = "INSERT INTO invoices (invoice_number, client_id, case_id, issue_date, " +
                        "due_date, amount, amount_paid, status, notes) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, invoice.getInvoiceNumber());
            pst.setInt(2, invoice.getClientId());
            pst.setInt(3, invoice.getCaseId());
            pst.setDate(4, Date.valueOf(invoice.getIssueDate()));
            pst.setDate(5, Date.valueOf(invoice.getDueDate()));
            pst.setBigDecimal(6, invoice.getAmount());
            pst.setBigDecimal(7, invoice.getAmountPaid());
            pst.setString(8, invoice.getStatus());
            pst.setString(9, invoice.getNotes());
            
            // Execute statement
            int rowsAffected = pst.executeUpdate();
            
            // Get generated ID
            if (rowsAffected > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    invoice.setId(rs.getInt(1));
                }
                rs.close();
                
                // Update time entries if they are associated with this invoice
                if (invoice.getTimeEntries() != null && !invoice.getTimeEntries().isEmpty()) {
                    for (TimeEntry timeEntry : invoice.getTimeEntries()) {
                        timeEntryDao.markTimeEntryAsBilled(timeEntry.getId(), invoice.getId());
                    }
                }
            }
            
            // Commit transaction
            con.commit();
            
            // Close connection
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            try {
                // Rollback transaction on error
                if (con != null && !con.getAutoCommit()) {
                    con.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            
            ex.printStackTrace();
            return 0;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
    
    /**
     * Updates an existing invoice in the database
     * 
     * @param invoice The invoice to update
     * @return Number of rows affected
     */
    public int updateInvoice(Invoice invoice) {
        Connection con = null;
        try {
            // Create connection
            con = DriverManager.getConnection(db_url, db_username, db_passwd);
            con.setAutoCommit(false);  // Start transaction
            
            // Prepare statement
            String sql = "UPDATE invoices SET invoice_number = ?, client_id = ?, case_id = ?, " +
                        "issue_date = ?, due_date = ?, amount = ?, amount_paid = ?, status = ?, " +
                        "notes = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, invoice.getInvoiceNumber());
            pst.setInt(2, invoice.getClientId());
            pst.setInt(3, invoice.getCaseId());
            pst.setDate(4, Date.valueOf(invoice.getIssueDate()));
            pst.setDate(5, Date.valueOf(invoice.getDueDate()));
            pst.setBigDecimal(6, invoice.getAmount());
            pst.setBigDecimal(7, invoice.getAmountPaid());
            pst.setString(8, invoice.getStatus());
            pst.setString(9, invoice.getNotes());
            pst.setInt(10, invoice.getId());
            
            // Execute statement
            int rowsAffected = pst.executeUpdate();
            
            // Commit transaction
            con.commit();
            
            // Close connection
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            try {
                // Rollback transaction on error
                if (con != null && !con.getAutoCommit()) {
                    con.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            
            ex.printStackTrace();
            return 0;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
    
    /**
     * Updates the status of an invoice
     * 
     * @param invoiceId The ID of the invoice
     * @param status The new status
     * @return Number of rows affected
     */
    public int updateInvoiceStatus(int invoiceId, String status) {
        try {
            // Create connection
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            
            // Prepare statement
            String sql = "UPDATE invoices SET status = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, status);
            pst.setInt(2, invoiceId);
            
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
     * Finds an invoice by ID
     * 
     * @param id The invoice ID to search for
     * @return The invoice if found, null otherwise
     */
    public Invoice findInvoiceById(int id) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM invoices WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            
            ResultSet rs = pst.executeQuery();
            Invoice invoice = null;
            
            if (rs.next()) {
                invoice = extractInvoiceFromResultSet(rs);
            }
            
            con.close();
            return invoice;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds an invoice by invoice number
     * 
     * @param invoiceNumber The invoice number to search for
     * @return The invoice if found, null otherwise
     */
    public Invoice findInvoiceByInvoiceNumber(String invoiceNumber) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM invoices WHERE invoice_number = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, invoiceNumber);
            
            ResultSet rs = pst.executeQuery();
            Invoice invoice = null;
            
            if (rs.next()) {
                invoice = extractInvoiceFromResultSet(rs);
            }
            
            con.close();
            return invoice;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds invoices by client ID
     * 
     * @param clientId The client ID to search for
     * @return List of invoices for the client
     */
    public List<Invoice> findInvoicesByClient(int clientId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM invoices WHERE client_id = ? ORDER BY issue_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, clientId);
            
            ResultSet rs = pst.executeQuery();
            List<Invoice> invoiceList = new ArrayList<>();
            
            while (rs.next()) {
                Invoice invoice = extractInvoiceFromResultSet(rs);
                invoiceList.add(invoice);
            }
            
            con.close();
            return invoiceList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds invoices by case ID
     * 
     * @param caseId The case ID to search for
     * @return List of invoices for the case
     */
    public List<Invoice> findInvoicesByCase(int caseId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM invoices WHERE case_id = ? ORDER BY issue_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, caseId);
            
            ResultSet rs = pst.executeQuery();
            List<Invoice> invoiceList = new ArrayList<>();
            
            while (rs.next()) {
                Invoice invoice = extractInvoiceFromResultSet(rs);
                invoiceList.add(invoice);
            }
            
            con.close();
            return invoiceList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds invoices by status
     * 
     * @param status The status to search for
     * @return List of invoices with the status
     */
    public List<Invoice> findInvoicesByStatus(String status) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM invoices WHERE status = ? ORDER BY issue_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, status);
            
            ResultSet rs = pst.executeQuery();
            List<Invoice> invoiceList = new ArrayList<>();
            
            while (rs.next()) {
                Invoice invoice = extractInvoiceFromResultSet(rs);
                invoiceList.add(invoice);
            }
            
            con.close();
            return invoiceList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds invoices by date range
     * 
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of invoices in the date range
     */
    public List<Invoice> findInvoicesByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM invoices WHERE issue_date BETWEEN ? AND ? ORDER BY issue_date";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDate(1, Date.valueOf(startDate));
            pst.setDate(2, Date.valueOf(endDate));
            
            ResultSet rs = pst.executeQuery();
            List<Invoice> invoiceList = new ArrayList<>();
            
            while (rs.next()) {
                Invoice invoice = extractInvoiceFromResultSet(rs);
                invoiceList.add(invoice);
            }
            
            con.close();
            return invoiceList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds overdue invoices
     * 
     * @return List of overdue invoices
     */
    public List<Invoice> findOverdueInvoices() {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM invoices WHERE due_date < CURDATE() AND " +
                        "status != ? AND status != ? ORDER BY due_date";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, Invoice.STATUS_PAID);
            pst.setString(2, Invoice.STATUS_CANCELLED);
            
            ResultSet rs = pst.executeQuery();
            List<Invoice> invoiceList = new ArrayList<>();
            
            while (rs.next()) {
                Invoice invoice = extractInvoiceFromResultSet(rs);
                invoiceList.add(invoice);
            }
            
            con.close();
            return invoiceList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Gets all invoices
     * 
     * @return List of all invoices
     */
    public List<Invoice> findAllInvoices() {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM invoices ORDER BY issue_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            
            ResultSet rs = pst.executeQuery();
            List<Invoice> invoiceList = new ArrayList<>();
            
            while (rs.next()) {
                Invoice invoice = extractInvoiceFromResultSet(rs);
                invoiceList.add(invoice);
            }
            
            con.close();
            return invoiceList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Extract invoice data from a ResultSet row
     * 
     * @param rs ResultSet positioned at the invoice row
     * @return Invoice object populated with data
     * @throws Exception If an error occurs
     */
    private Invoice extractInvoiceFromResultSet(ResultSet rs) throws Exception {
        Invoice invoice = new Invoice();
        
        invoice.setId(rs.getInt("id"));
        invoice.setInvoiceNumber(rs.getString("invoice_number"));
        invoice.setClientId(rs.getInt("client_id"));
        invoice.setCaseId(rs.getInt("case_id"));
        
        // Handle dates
        Date issueDate = rs.getDate("issue_date");
        if (issueDate != null) {
            invoice.setIssueDate(issueDate.toLocalDate());
        }
        
        Date dueDate = rs.getDate("due_date");
        if (dueDate != null) {
            invoice.setDueDate(dueDate.toLocalDate());
        }
        
        invoice.setAmount(rs.getBigDecimal("amount"));
        invoice.setAmountPaid(rs.getBigDecimal("amount_paid"));
        invoice.setStatus(rs.getString("status"));
        invoice.setNotes(rs.getString("notes"));
        
        return invoice;
    }
    
    /**
     * Gets an invoice with all related details (client, case, time entries, payments)
     * 
     * @param invoiceId The ID of the invoice
     * @return The invoice with all details loaded
     */
    public Invoice getInvoiceWithDetails(int invoiceId) {
        try {
            // First get the invoice
            Invoice invoice = findInvoiceById(invoiceId);
            if (invoice == null) {
                return null;
            }
            
            // Load client
            Client client = clientDao.findClientById(invoice.getClientId());
            invoice.setClient(client);
            
            // Load case
            Case legalCase = caseDao.findCaseById(invoice.getCaseId());
            invoice.setCase(legalCase);
            
            // Load time entries
            List<TimeEntry> timeEntries = timeEntryDao.findTimeEntriesByInvoice(invoiceId);
            invoice.setTimeEntries(timeEntries);
            PaymentDao dao = new PaymentDao();
            // Load payments
            List<Payment> payments = dao.findPaymentsByInvoice(invoiceId);
            invoice.setPayments(payments);
            
            return invoice;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Updates the paid amount for an invoice based on payments
     * 
     * @param invoiceId The ID of the invoice
     * @return Number of rows affected
     */
    public int updateInvoicePaidAmount(int invoiceId) {
        try {
            // Get the invoice with its payments
            Invoice invoice = getInvoiceWithDetails(invoiceId);
            if (invoice == null) {
                return 0;
            }
            
            // Recalculate amount paid
            invoice.recalculateAmountPaid();
            
            // Update the invoice
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "UPDATE invoices SET amount_paid = ?, status = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setBigDecimal(1, invoice.getAmountPaid());
            pst.setString(2, invoice.getStatus());
            pst.setInt(3, invoiceId);
            
            int rowsAffected = pst.executeUpdate();
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Creates a new invoice from unbilled time entries for a case
     * 
     * @param caseId The ID of the case
     * @param invoiceNumber The invoice number to use
     * @param dueDate The due date for the invoice
     * @return The created invoice, or null if creation failed
     */
    public Invoice createInvoiceFromUnbilledTimeEntries(int caseId, String invoiceNumber, LocalDate dueDate) {
        Connection con = null;
        try {
            // Get case information
            Case legalCase = caseDao.findCaseById(caseId);
            if (legalCase == null) {
                return null;
            }
            
            // Get unbilled time entries
            List<TimeEntry> unbilledEntries = timeEntryDao.findUnbilledTimeEntriesByCase(caseId);
            if (unbilledEntries.isEmpty()) {
                return null;
            }
            
            // Create invoice
            Invoice invoice = new Invoice();
            invoice.setInvoiceNumber(invoiceNumber);
            invoice.setClientId(legalCase.getClientId());
            invoice.setCaseId(caseId);
            invoice.setIssueDate(LocalDate.now());
            invoice.setDueDate(dueDate);
            invoice.setStatus(Invoice.STATUS_ISSUED);
            
            // Calculate total amount
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (TimeEntry entry : unbilledEntries) {
                if (entry.getHourlyRate() != null) {
                    BigDecimal entryAmount = entry.getHourlyRate().multiply(new BigDecimal(entry.getHours()));
                    totalAmount = totalAmount.add(entryAmount);
                }
            }
            invoice.setAmount(totalAmount);
            invoice.setAmountPaid(BigDecimal.ZERO);
            
            // Create connection
            con = DriverManager.getConnection(db_url, db_username, db_passwd);
            con.setAutoCommit(false);  // Start transaction
            
            // Save invoice
            String sql = "INSERT INTO invoices (invoice_number, client_id, case_id, issue_date, " +
                        "due_date, amount, amount_paid, status, notes) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, invoice.getInvoiceNumber());
            pst.setInt(2, invoice.getClientId());
            pst.setInt(3, invoice.getCaseId());
            pst.setDate(4, Date.valueOf(invoice.getIssueDate()));
            pst.setDate(5, Date.valueOf(invoice.getDueDate()));
            pst.setBigDecimal(6, invoice.getAmount());
            pst.setBigDecimal(7, invoice.getAmountPaid());
            pst.setString(8, invoice.getStatus());
            pst.setString(9, invoice.getNotes());
            
            int rowsAffected = pst.executeUpdate();
            
            // Get generated ID
            if (rowsAffected > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    invoice.setId(rs.getInt(1));
                }
                rs.close();
                
                // Mark time entries as billed
                for (TimeEntry entry : unbilledEntries) {
                    timeEntryDao.markTimeEntryAsBilled(entry.getId(), invoice.getId());
                }
            }
            
            // Commit transaction
            con.commit();
            con.close();
            
            // Set time entries
            invoice.setTimeEntries(unbilledEntries);
            
            return invoice;
            
        } catch (Exception ex) {
            try {
                // Rollback transaction on error
                if (con != null && !con.getAutoCommit()) {
                    con.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
    
    /**
     * Deletes an invoice from the database
     * 
     * @param invoiceId The ID of the invoice to delete
     * @return Number of rows affected
     */
    public int deleteInvoice(int invoiceId) {
        Connection con = null;
        try {
            // Check if invoice has payments
            Invoice invoice = getInvoiceWithDetails(invoiceId);
            if (invoice == null) {
                return 0;
            }
            
            if (invoice.getPayments() != null && !invoice.getPayments().isEmpty()) {
                // Cannot delete invoice with payments
                return 0;
            }
            
            // Create connection
            con = DriverManager.getConnection(db_url, db_username, db_passwd);
            con.setAutoCommit(false);  // Start transaction
            
            // Update time entries to unbilled
            String sqlTimeEntries = "UPDATE time_entries SET billed = FALSE, invoice_id = NULL WHERE invoice_id = ?";
            PreparedStatement pstTimeEntries = con.prepareStatement(sqlTimeEntries);
            pstTimeEntries.setInt(1, invoiceId);
            pstTimeEntries.executeUpdate();
            
            // Then delete the invoice
            String sql = "DELETE FROM invoices WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, invoiceId);
            
            int rowsAffected = pst.executeUpdate();
            
            // Commit transaction
            con.commit();
            
            // Close connection
            con.close();
            return rowsAffected;
            
        } catch (Exception ex) {
            try {
                // Rollback transaction on error
                if (con != null && !con.getAutoCommit()) {
                    con.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            
            ex.printStackTrace();
            return 0;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
    
    /**
     * Generates the next invoice number based on the current highest number
     * 
     * @return Next available invoice number
     */
    public String generateNextInvoiceNumber() {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT MAX(CAST(SUBSTRING(invoice_number, 4) AS UNSIGNED)) AS max_num " +
                         "FROM invoices WHERE invoice_number LIKE 'INV%'";
            PreparedStatement pst = con.prepareStatement(sql);
            
            ResultSet rs = pst.executeQuery();
            int maxNum = 0;
            
            if (rs.next()) {
                maxNum = rs.getInt("max_num");
            }
            
            con.close();
            
            // Format next invoice number
            int nextNum = maxNum + 1;
            return String.format("INV%06d", nextNum);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            // Default format if error occurs
            return "INV" + System.currentTimeMillis();
        }
    }
}