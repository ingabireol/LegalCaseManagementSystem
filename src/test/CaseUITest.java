package test;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Case;
import model.Client;
import model.Attorney;
import model.Document;
import model.Event;
import model.TimeEntry;
import view.cases.CasesPanel;
import view.cases.CaseEditorDialog;
import view.cases.CaseDetailsDialog;
import controller.CaseController;

/**
 * Test class for Cases UI components.
 * This class demonstrates how to use the Cases UI components.
 */
public class CaseUITest {
    
    /**
     * Main method to start the test application
     */
    public static void main(String[] args) {
        try {
            // Set look and feel to system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    
    /**
     * Create and show the GUI
     */
    private static void createAndShowGUI() {
        // Create and set up the window
        JFrame frame = new JFrame("Cases UI Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        
        // Create a tabbed pane for testing different components
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Add test panels
        tabbedPane.addTab("Cases Panel", createCasesPanelTest());
        tabbedPane.addTab("Case Editor", createCaseEditorTest());
        tabbedPane.addTab("Case Details", createCaseDetailsTest());
        
        frame.getContentPane().add(tabbedPane);
        
        // Display the window
        frame.setVisible(true);
    }
    
    /**
     * Create a test panel for CasesPanel
     */
    private static JPanel createCasesPanelTest() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Create the cases panel
        CasesPanel casesPanel = new CasesPanel();
        
        // Add it to the test panel
        panel.add(casesPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create a test panel for CaseEditorDialog
     */
    private static JPanel createCaseEditorTest() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add description
        JLabel descLabel = new JLabel("<html>Click the button below to open the Case Editor Dialog.<br>" +
                                      "You can test creating a new case.</html>");
        panel.add(descLabel, BorderLayout.NORTH);
        
        // Add button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton newCaseButton = new JButton("Open Case Editor (New Case)");
        newCaseButton.addActionListener(e -> {
            // Create a new case editor dialog
            CaseEditorDialog dialog = new CaseEditorDialog(
                SwingUtilities.getWindowAncestor(panel), null);
            dialog.setVisible(true);
            
            // Show result
            if (dialog.isCaseSaved()) {
                JOptionPane.showMessageDialog(
                    panel,
                    "Case saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        
        JButton editCaseButton = new JButton("Open Case Editor (Edit Sample Case)");
        editCaseButton.addActionListener(e -> {
            // Create a sample case for editing
            Case sampleCase = createSampleCase();
            
            // Open editor with sample case
            CaseEditorDialog dialog = new CaseEditorDialog(
                SwingUtilities.getWindowAncestor(panel), sampleCase);
            dialog.setVisible(true);
            
            // Show result
            if (dialog.isCaseSaved()) {
                JOptionPane.showMessageDialog(
                    panel,
                    "Case updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        
        buttonPanel.add(newCaseButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(editCaseButton);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create a test panel for CaseDetailsDialog
     */
    private static JPanel createCaseDetailsTest() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add description
        JLabel descLabel = new JLabel("<html>Click the button below to open the Case Details Dialog.<br>" +
                                      "You can view details of a sample case.</html>");
        panel.add(descLabel, BorderLayout.NORTH);
        
        // Add button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton viewCaseButton = new JButton("Open Case Details (Sample Case)");
        viewCaseButton.addActionListener(e -> {
            // Create a sample case with all details
            Case sampleCase = createSampleCaseWithDetails();
            
            // Open details dialog
            CaseDetailsDialog dialog = new CaseDetailsDialog(
                SwingUtilities.getWindowAncestor(panel), sampleCase);
            dialog.setVisible(true);
        });
        
        buttonPanel.add(viewCaseButton);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create a sample case for testing
     */
    private static Case createSampleCase() {
        Case sampleCase = new Case();
        sampleCase.setId(1);
        sampleCase.setCaseNumber("CASE-2023-1234");
        sampleCase.setTitle("Smith v. Johnson");
        sampleCase.setCaseType("Civil");
        sampleCase.setStatus("Open");
        sampleCase.setDescription("This is a sample case for testing the Case UI components.");
        sampleCase.setFileDate(LocalDate.now().minusMonths(2));
        sampleCase.setCourt("Superior Court of California");
        sampleCase.setJudge("Judge Robert Wilson");
        sampleCase.setOpposingParty("John Johnson");
        sampleCase.setOpposingCounsel("Jane Williams, Esq.");
        
        // Create a sample client
        Client client = new Client();
        client.setId(1);
        client.setClientId("CLI-2023-5678");
        client.setName("Michael Smith");
        client.setEmail("michael.smith@example.com");
        client.setPhone("555-123-4567");
        client.setClientType("Individual");
        sampleCase.setClientId(client.getId());
        sampleCase.setClient(client);
        
        // Create sample attorneys
        ArrayList<Attorney> attorneys = new ArrayList<>();
        
        Attorney attorney1 = new Attorney();
        attorney1.setId(1);
        attorney1.setAttorneyId("ATT-2023-001");
        attorney1.setFirstName("Lisa");
        attorney1.setLastName("Campbell");
        attorney1.setEmail("lisa.campbell@lawfirm.com");
        attorney1.setSpecialization("Civil Litigation");
        attorneys.add(attorney1);
        
        Attorney attorney2 = new Attorney();
        attorney2.setId(2);
        attorney2.setAttorneyId("ATT-2023-002");
        attorney2.setFirstName("David");
        attorney2.setLastName("Martinez");
        attorney2.setEmail("david.martinez@lawfirm.com");
        attorney2.setSpecialization("Corporate Law");
        attorneys.add(attorney2);
        
        sampleCase.setAttorneys(attorneys);
        
        return sampleCase;
    }
    
    /**
     * Create a sample case with all details for testing
     */
    private static Case createSampleCaseWithDetails() {
        Case sampleCase = createSampleCase();
        
        // Add documents
        ArrayList<Document> documents = new ArrayList<>();
        
        Document doc1 = new Document();
        doc1.setId(1);
        doc1.setDocumentId("DOC-2023-001");
        doc1.setTitle("Complaint");
        doc1.setDocumentType("Pleading");
        doc1.setDateAdded(LocalDate.now().minusMonths(2));
        doc1.setStatus("Active");
        documents.add(doc1);
        
        Document doc2 = new Document();
        doc2.setId(2);
        doc2.setDocumentId("DOC-2023-002");
        doc2.setTitle("Answer to Complaint");
        doc2.setDocumentType("Pleading");
        doc2.setDateAdded(LocalDate.now().minusMonths(1));
        doc2.setStatus("Active");
        documents.add(doc2);
        
        Document doc3 = new Document();
        doc3.setId(3);
        doc3.setDocumentId("DOC-2023-003");
        doc3.setTitle("Initial Disclosure");
        doc3.setDocumentType("Discovery");
        doc3.setDateAdded(LocalDate.now().minusWeeks(2));
        doc3.setStatus("Active");
        documents.add(doc3);
        
        sampleCase.setDocuments(documents);
        
        // Add events
        ArrayList<Event> events = new ArrayList<>();
        
        Event event1 = new Event();
        event1.setId(1);
        event1.setEventId("EVT-2023-001");
        event1.setTitle("Initial Case Management Conference");
        event1.setEventType("Court Appearance");
        event1.setEventDate(LocalDate.now().plusWeeks(2));
        event1.setStatus("Scheduled");
        event1.setLocation("Courtroom 3B");
        events.add(event1);
        
        Event event2 = new Event();
        event2.setId(2);
        event2.setEventId("EVT-2023-002");
        event2.setTitle("Client Meeting");
        event2.setEventType("Meeting");
        event2.setEventDate(LocalDate.now().minusWeeks(1));
        event2.setStatus("Completed");
        event2.setLocation("Law Office Conference Room");
        events.add(event2);
        
        Event event3 = new Event();
        event3.setId(3);
        event3.setEventId("EVT-2023-003");
        event3.setTitle("Deposition of Plaintiff");
        event3.setEventType("Deposition");
        event3.setEventDate(LocalDate.now().plusMonths(1));
        event3.setStatus("Scheduled");
        event3.setLocation("Court Reporter's Office");
        events.add(event3);
        
        sampleCase.setEvents(events);
        
        // Add time entries
        ArrayList<TimeEntry> timeEntries = new ArrayList<>();
        
        TimeEntry entry1 = new TimeEntry();
        entry1.setId(1);
        entry1.setEntryId("TE-2023-001");
        entry1.setCaseId(sampleCase.getId());
        entry1.setAttorneyId(1);
        entry1.setAttorney(sampleCase.getAttorneys().get(0));
        entry1.setEntryDate(LocalDate.now().minusWeeks(3));
        entry1.setHours(2.5);
        entry1.setDescription("Initial client consultation");
        entry1.setActivityCode("COM");
        entry1.setBilled(true);
        timeEntries.add(entry1);
        
        TimeEntry entry2 = new TimeEntry();
        entry2.setId(2);
        entry2.setEntryId("TE-2023-002");
        entry2.setCaseId(sampleCase.getId());
        entry2.setAttorneyId(1);
        entry2.setAttorney(sampleCase.getAttorneys().get(0));
        entry2.setEntryDate(LocalDate.now().minusWeeks(2));
        entry2.setHours(4.0);
        entry2.setDescription("Research and draft complaint");
        entry2.setActivityCode("DRA");
        entry2.setBilled(true);
        timeEntries.add(entry2);
        
        TimeEntry entry3 = new TimeEntry();
        entry3.setId(3);
        entry3.setEntryId("TE-2023-003");
        entry3.setCaseId(sampleCase.getId());
        entry3.setAttorneyId(2);
        entry3.setAttorney(sampleCase.getAttorneys().get(1));
        entry3.setEntryDate(LocalDate.now().minusWeeks(1));
        entry3.setHours(1.5);
        entry3.setDescription("Review answer to complaint");
        entry3.setActivityCode("REV");
        entry3.setBilled(false);
        timeEntries.add(entry3);
        
        sampleCase.setTimeEntries(timeEntries);
        
        return sampleCase;
    }
}