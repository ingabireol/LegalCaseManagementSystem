package dao;

import model.Payment;
import model.Invoice;
import model.Client;

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
 * Data Access Object for Payment operations.
 */
public class PaymentDao {
    private String db_url = "jdbc:mysql://localhost:3306/legalcasemgmtdb";
    private String db_username = "root";
    private String db_passwd = "Ornella12345!";
    
    // Other DAOs for related entities
    private ClientDao clientDao;
    private InvoiceDao invoiceDao;
    
    /**
     * Constructor
     */
    public PaymentDao() {
        this.clientDao = new ClientDao();
        this.invoiceDao = new InvoiceDao();
    }
    
    /**
     * Creates a new payment in the database
     * 
     * @param payment The payment to create
     * @return Number of rows affected
     */
    public int createPayment(Payment payment) {
        Connection con = null;
        try {
            // Create connection
            con = DriverManager.getConnection(db_url, db_username, db_passwd);
            con.setAutoCommit(false);  // Start transaction
            
            // Prepare statement
            String sql = "INSERT INTO payments (payment_id, invoice_id, client_id, payment_date, " +
                        "amount, payment_method, reference, notes) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, payment.getPaymentId());
            pst.setInt(2, payment.getInvoiceId());
            pst.setInt(3, payment.getClientId());
            pst.setDate(4, Date.valueOf(payment.getPaymentDate()));
            pst.setBigDecimal(5, payment.getAmount());
            pst.setString(6, payment.getPaymentMethod());
            pst.setString(7, payment.getReference());
            pst.setString(8, payment.getNotes());
            
            // Execute statement
            int rowsAffected = pst.executeUpdate();
            
            // Get generated ID
            if (rowsAffected > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    payment.setId(rs.getInt(1));
                }
                rs.close();
                
                // Update invoice paid amount
                invoiceDao.updateInvoicePaidAmount(payment.getInvoiceId());
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
     * Updates an existing payment in the database
     * 
     * @param payment The payment to update
     * @return Number of rows affected
     */
    public int updatePayment(Payment payment) {
        Connection con = null;
        try {
            // Create connection
            con = DriverManager.getConnection(db_url, db_username, db_passwd);
            con.setAutoCommit(false);  // Start transaction
            
            // Prepare statement
            String sql = "UPDATE payments SET payment_id = ?, invoice_id = ?, client_id = ?, " +
                        "payment_date = ?, amount = ?, payment_method = ?, reference = ?, " +
                        "notes = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, payment.getPaymentId());
            pst.setInt(2, payment.getInvoiceId());
            pst.setInt(3, payment.getClientId());
            pst.setDate(4, Date.valueOf(payment.getPaymentDate()));
            pst.setBigDecimal(5, payment.getAmount());
            pst.setString(6, payment.getPaymentMethod());
            pst.setString(7, payment.getReference());
            pst.setString(8, payment.getNotes());
            pst.setInt(9, payment.getId());
            
            // Execute statement
            int rowsAffected = pst.executeUpdate();
            
            if (rowsAffected > 0) {
                // Update invoice paid amount
                invoiceDao.updateInvoicePaidAmount(payment.getInvoiceId());
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
     * Finds a payment by ID
     * 
     * @param id The payment ID to search for
     * @return The payment if found, null otherwise
     */
    public Payment findPaymentById(int id) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM payments WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            
            ResultSet rs = pst.executeQuery();
            Payment payment = null;
            
            if (rs.next()) {
                payment = extractPaymentFromResultSet(rs);
            }
            
            con.close();
            return payment;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds a payment by payment ID
     * 
     * @param paymentId The payment ID to search for
     * @return The payment if found, null otherwise
     */
    public Payment findPaymentByPaymentId(String paymentId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM payments WHERE payment_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, paymentId);
            
            ResultSet rs = pst.executeQuery();
            Payment payment = null;
            
            if (rs.next()) {
                payment = extractPaymentFromResultSet(rs);
            }
            
            con.close();
            return payment;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds payments by invoice ID
     * 
     * @param invoiceId The invoice ID to search for
     * @return List of payments for the invoice
     */
    public List<Payment> findPaymentsByInvoice(int invoiceId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM payments WHERE invoice_id = ? ORDER BY payment_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, invoiceId);
            
            ResultSet rs = pst.executeQuery();
            List<Payment> paymentList = new ArrayList<>();
            
            while (rs.next()) {
                Payment payment = extractPaymentFromResultSet(rs);
                paymentList.add(payment);
            }
            
            con.close();
            return paymentList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds payments by client ID
     * 
     * @param clientId The client ID to search for
     * @return List of payments for the client
     */
    public List<Payment> findPaymentsByClient(int clientId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM payments WHERE client_id = ? ORDER BY payment_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, clientId);
            
            ResultSet rs = pst.executeQuery();
            List<Payment> paymentList = new ArrayList<>();
            
            while (rs.next()) {
                Payment payment = extractPaymentFromResultSet(rs);
                paymentList.add(payment);
            }
            
            con.close();
            return paymentList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds payments by payment method
     * 
     * @param paymentMethod The payment method to search for
     * @return List of payments with the payment method
     */
    public List<Payment> findPaymentsByMethod(String paymentMethod) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM payments WHERE payment_method = ? ORDER BY payment_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, paymentMethod);
            
            ResultSet rs = pst.executeQuery();
            List<Payment> paymentList = new ArrayList<>();
            
            while (rs.next()) {
                Payment payment = extractPaymentFromResultSet(rs);
                paymentList.add(payment);
            }
            
            con.close();
            return paymentList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Finds payments by date range
     * 
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of payments in the date range
     */
    public List<Payment> findPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM payments WHERE payment_date BETWEEN ? AND ? ORDER BY payment_date";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDate(1, Date.valueOf(startDate));
            pst.setDate(2, Date.valueOf(endDate));
            
            ResultSet rs = pst.executeQuery();
            List<Payment> paymentList = new ArrayList<>();
            
            while (rs.next()) {
                Payment payment = extractPaymentFromResultSet(rs);
                paymentList.add(payment);
            }
            
            con.close();
            return paymentList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Gets all payments
     * 
     * @return List of all payments
     */
    public List<Payment> findAllPayments() {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT * FROM payments ORDER BY payment_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            
            ResultSet rs = pst.executeQuery();
            List<Payment> paymentList = new ArrayList<>();
            
            while (rs.next()) {
                Payment payment = extractPaymentFromResultSet(rs);
                paymentList.add(payment);
            }
            
            con.close();
            return paymentList;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Extract payment data from a ResultSet row
     * 
     * @param rs ResultSet positioned at the payment row
     * @return Payment object populated with data
     * @throws Exception If an error occurs
     */
    private Payment extractPaymentFromResultSet(ResultSet rs) throws Exception {
        Payment payment = new Payment();
        
        payment.setId(rs.getInt("id"));
        payment.setPaymentId(rs.getString("payment_id"));
        payment.setInvoiceId(rs.getInt("invoice_id"));
        payment.setClientId(rs.getInt("client_id"));
        
        // Handle date
        Date paymentDate = rs.getDate("payment_date");
        if (paymentDate != null) {
            payment.setPaymentDate(paymentDate.toLocalDate());
        }
        
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setReference(rs.getString("reference"));
        payment.setNotes(rs.getString("notes"));
        
        return payment;
    }
    
    /**
     * Gets a payment with invoice and client information
     * 
     * @param paymentId The ID of the payment
     * @return The payment with invoice and client loaded
     */
    public Payment getPaymentWithDetails(int paymentId) {
        try {
            // First get the payment
            Payment payment = findPaymentById(paymentId);
            if (payment == null) {
                return null;
            }
            
            // Load invoice
            Invoice invoice = invoiceDao.findInvoiceById(payment.getInvoiceId());
            payment.setInvoice(invoice);
            
            // Load client
            Client client = clientDao.findClientById(payment.getClientId());
            payment.setClient(client);
            
            return payment;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Gets the total payments received for a client
     * 
     * @param clientId The ID of the client
     * @return Total amount paid by the client
     */
    public BigDecimal getTotalPaymentsByClient(int clientId) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT SUM(amount) AS total_amount FROM payments WHERE client_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, clientId);
            
            ResultSet rs = pst.executeQuery();
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            if (rs.next()) {
                BigDecimal amount = rs.getBigDecimal("total_amount");
                if (amount != null) {
                    totalAmount = amount;
                }
            }
            
            con.close();
            return totalAmount;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Gets the total payments received for a date range
     * 
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return Total amount paid in the date range
     */
    public BigDecimal getTotalPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT SUM(amount) AS total_amount FROM payments WHERE payment_date BETWEEN ? AND ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDate(1, Date.valueOf(startDate));
            pst.setDate(2, Date.valueOf(endDate));
            
            ResultSet rs = pst.executeQuery();
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            if (rs.next()) {
                BigDecimal amount = rs.getBigDecimal("total_amount");
                if (amount != null) {
                    totalAmount = amount;
                }
            }
            
            con.close();
            return totalAmount;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Deletes a payment from the database
     * 
     * @param paymentId The ID of the payment to delete
     * @return Number of rows affected
     */
    public int deletePayment(int paymentId) {
        Connection con = null;
        try {
            // First get the payment to get the invoice ID
            Payment payment = findPaymentById(paymentId);
            if (payment == null) {
                return 0;
            }
            
            int invoiceId = payment.getInvoiceId();
            
            // Create connection
            con = DriverManager.getConnection(db_url, db_username, db_passwd);
            con.setAutoCommit(false);  // Start transaction
            
            // Delete the payment
            String sql = "DELETE FROM payments WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, paymentId);
            
            int rowsAffected = pst.executeUpdate();
            
            if (rowsAffected > 0) {
                // Update invoice paid amount
                invoiceDao.updateInvoicePaidAmount(invoiceId);
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
     * Generates the next payment ID based on the current highest number
     * 
     * @return Next available payment ID
     */
    public String generateNextPaymentId() {
        try {
            Connection con = DriverManager.getConnection(db_url, db_username, db_passwd);
            String sql = "SELECT MAX(CAST(SUBSTRING(payment_id, 4) AS UNSIGNED)) AS max_num " +
                         "FROM payments WHERE payment_id LIKE 'PMT%'";
            PreparedStatement pst = con.prepareStatement(sql);
            
            ResultSet rs = pst.executeQuery();
            int maxNum = 0;
            
            if (rs.next()) {
                maxNum = rs.getInt("max_num");
            }
            
            con.close();
            
            // Format next payment ID
            int nextNum = maxNum + 1;
            return String.format("PMT%06d", nextNum);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            // Default format if error occurs
            return "PMT" + System.currentTimeMillis();
        }
    }
}