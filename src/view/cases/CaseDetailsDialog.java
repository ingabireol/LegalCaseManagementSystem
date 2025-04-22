package view.cases;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import model.Case;
import controller.CaseController;
import view.util.UIConstants;

/**
 * Dialog for viewing case details.
 */
public class CaseDetailsDialog extends JDialog {
    private Case legalCase;
    private CaseController caseController;
    
    private JButton closeButton;
    private JButton editButton;
    
    /**
     * Constructor
     * 
     * @param parent The parent window
     * @param legalCase The case to display
     */
    public CaseDetailsDialog(Window parent, Case legalCase) {
        super(parent, "Case Details", ModalityType.APPLICATION_MODAL);
        
        this.legalCase = legalCase;
        this.caseController = new CaseController();
        
        initializeUI();
    }
    
    /**
     * Initialize the user interface components
     */
    private void initializeUI() {
        setSize(900, 650);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        // Create title panel
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        // Create content panel - placeholder for now
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel placeholderLabel = new JLabel("Case Details: " + legalCase.getCaseNumber() + " - " + legalCase.getTitle(), 
                                         SwingConstants.CENTER);
        placeholderLabel.setFont(UIConstants.SUBTITLE_FONT);
        contentPanel.add(placeholderLabel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create the title panel
     * 
     * @return The title panel
     */
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UIConstants.PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Title label
        JLabel titleLabel = new JLabel("Case Details");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        return titlePanel;
    }
    
    /**
     * Create the button panel
     * 
     * @return The button panel
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        buttonPanel.setBackground(Color.WHITE);
        
        editButton = new JButton("Edit Case");
        editButton.setFont(UIConstants.NORMAL_FONT);
        editButton.addActionListener(e -> editCase());
        
        closeButton = new JButton("Close");
        closeButton.setFont(UIConstants.NORMAL_FONT);
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(closeButton);
        
        return buttonPanel;
    }
    
    /**
     * Edit this case
     */
    private void editCase() {
        // Placeholder for case editing functionality
        JOptionPane.showMessageDialog(
            this,
            "Edit case: " + legalCase.getCaseNumber() + "\n" +
            "This feature will be implemented in the CaseEditorDialog.",
            "Edit Case",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}