package view.admin;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import controller.UserController;
import view.util.UIConstants;

/**
 * Panel for system administration in the Legal Case Management System.
 * Only accessible to users with administrator privileges.
 */
public class AdminPanel extends JPanel {
    private UserController userController;
    
    /**
     * Constructor
     */
    public AdminPanel() {
        this.userController = new UserController();
        
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
        
        // Create tabbed pane for admin sections
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIConstants.NORMAL_FONT);
        
        // Create user management panel
        JPanel usersPanel = createUsersPanel();
        tabbedPane.addTab("User Management", usersPanel);
        
        // Create system settings panel
        JPanel settingsPanel = createSettingsPanel();
        tabbedPane.addTab("System Settings", settingsPanel);
        
        // Create database management panel
        JPanel databasePanel = createDatabasePanel();
        tabbedPane.addTab("Database Management", databasePanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    /**
     * Create the header panel with title
     * 
     * @return The header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
        
        JLabel titleLabel = new JLabel("System Administration");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        headerPanel.add(titleLabel);
        
        return headerPanel;
    }
    
    /**
     * Create the user management panel
     * 
     * @return The users panel
     */
    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Placeholder content
        JLabel placeholderLabel = new JLabel(
            "<html><center>User Management - Under Construction<br><br>" +
            "This panel will provide functionality for:<br>" +
            "- Creating new user accounts<br>" +
            "- Managing user roles and permissions<br>" +
            "- Resetting passwords<br>" +
            "- Activating/deactivating user accounts</center></html>",
            SwingConstants.CENTER
        );
        placeholderLabel.setFont(UIConstants.NORMAL_FONT);
        
        panel.add(placeholderLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create the system settings panel
     * 
     * @return The settings panel
     */
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Placeholder content
        JLabel placeholderLabel = new JLabel(
            "<html><center>System Settings - Under Construction<br><br>" +
            "This panel will provide functionality for:<br>" +
            "- Configuring system-wide settings<br>" +
            "- Setting default values<br>" +
            "- Customizing the application<br>" +
            "- Managing backup and restore options</center></html>",
            SwingConstants.CENTER
        );
        placeholderLabel.setFont(UIConstants.NORMAL_FONT);
        
        panel.add(placeholderLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create the database management panel
     * 
     * @return The database panel
     */
    private JPanel createDatabasePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Placeholder content
        JLabel placeholderLabel = new JLabel(
            "<html><center>Database Management - Under Construction<br><br>" +
            "This panel will provide functionality for:<br>" +
            "- Managing database connections<br>" +
            "- Backing up and restoring data<br>" +
            "- Viewing database statistics<br>" +
            "- Running database maintenance tasks</center></html>",
            SwingConstants.CENTER
        );
        placeholderLabel.setFont(UIConstants.NORMAL_FONT);
        
        panel.add(placeholderLabel, BorderLayout.CENTER);
        
        return panel;
    }
}