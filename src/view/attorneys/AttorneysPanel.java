package view.attorneys;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import controller.AttorneyController;
import view.util.UIConstants;
import view.components.TableFilterPanel;

/**
 * Panel for attorney management in the Legal Case Management System.
 */
public class AttorneysPanel extends JPanel {
    private AttorneyController attorneyController;
    private TableFilterPanel filterPanel;
    
    /**
     * Constructor
     */
    public AttorneysPanel() {
        this.attorneyController = new AttorneyController();
        
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
        
        JLabel placeholderLabel = new JLabel("Attorneys Management Panel - Under Construction", SwingConstants.CENTER);
        placeholderLabel.setFont(UIConstants.SUBTITLE_FONT);
        placeholderLabel.setForeground(UIConstants.PRIMARY_COLOR);
        
        JLabel detailsLabel = new JLabel(
            "<html><center>This panel will contain functionality for managing attorneys.<br><br>" +
            "Features will include:<br>" +
            "- Viewing all attorneys<br>" +
            "- Filtering by specialization<br>" +
            "- Creating new attorney profiles<br>" +
            "- Editing attorney details<br>" +
            "- Viewing attorney case assignments<br>" +
            "- Analyzing attorney workload and billable hours</center></html>",
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
        
        JLabel titleLabel = new JLabel("Attorneys Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titlePanel.add(titleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Filter panel
        filterPanel = new TableFilterPanel(
            new String[]{"All", "Name", "Specialization"},
            searchText -> {
                // Placeholder for search action
                JOptionPane.showMessageDialog(
                    this,
                    "Search for attorneys with text: " + searchText,
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