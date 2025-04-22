package view.invoices;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import controller.InvoiceController;
import view.util.UIConstants;
import view.components.TableFilterPanel;

/**
 * Panel for invoices and payment management in the Legal Case Management System.
 */
public class InvoicesPanel extends JPanel {
    private InvoiceController invoiceController;
    private TableFilterPanel filterPanel;
    
    /**
     * Constructor
     */
    public InvoicesPanel() {
        this.invoiceController = new InvoiceController();
        
        initializeUI();
    }
    
    /**
     * Initialize the user interface components
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create placeholder content
        JPanel placeholderPanel = new JPanel(new BorderLayout());
        placeholderPanel.setBackground(Color.WHITE);
        placeholderPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel placeholderLabel = new JLabel("Invoices & Payments Panel - Under Construction", SwingConstants.CENTER);
        placeholderLabel.setFont(UIConstants.SUBTITLE_FONT);
        placeholderLabel.setForeground(UIConstants.PRIMARY_COLOR);
        
        JLabel detailsLabel = new JLabel(
            "<html><center>This panel will contain functionality for managing invoices and payments.<br><br>" +
            "Features will include:<br>" +
            "- Viewing all invoices<br>" +
            "- Filtering by client, status, or date<br>" +
            "- Creating invoices from billable hours<br>" +
            "- Recording payments<br>" +
            "- Generating payment reports<br>" +
            "- Tracking overdue invoices</center></html>",
            SwingConstants.CENTER
        );
        detailsLabel.setFont(UIConstants.NORMAL_FONT);
        
        placeholderPanel.add(placeholderLabel, BorderLayout.NORTH);
        placeholderPanel.add(detailsLabel, BorderLayout.CENTER);
        
        add(placeholderPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create the header panel with title and filters
     * 
     * @return The header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
        
        JLabel titleLabel = new JLabel("Invoices & Payments");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titlePanel.add(titleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Filter panel
        filterPanel = new TableFilterPanel(
            new String[]{"All", "Invoice #", "Client", "Status"},
            searchText -> {
                // Placeholder for search action
                JOptionPane.showMessageDialog(
                    this,
                    "Search for invoices with text: " + searchText,
                    "Search",
                    JOptionPane.INFORMATION_MESSAGE
                );
            },
            () -> {
                // Placeholder for clear action
                JOptionPane.showMessageDialog(
                    this,
                    "Filters cleared",
                    "Clear",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        );
        headerPanel.add(filterPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
}