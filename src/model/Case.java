package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a legal case in the system.
 * Contains case details and relationships to clients, attorneys, documents, etc.
 */
public class Case {
    private int id;
    private String caseNumber;
    private String title;
    private String caseType;
    private String status;
    private String description;
    private LocalDate fileDate;
    private LocalDate closingDate;
    private String court;
    private String judge;
    private String opposingParty;
    private String opposingCounsel;
    private int clientId;
    private Client client;
    private List<Attorney> attorneys;
    private List<Document> documents;
    private List<Event> events;
    private List<TimeEntry> timeEntries;
    
    /**
     * Default constructor
     */
    public Case() {
        this.attorneys = new ArrayList<>();
        this.documents = new ArrayList<>();
        this.events = new ArrayList<>();
        this.timeEntries = new ArrayList<>();
        this.fileDate = LocalDate.now();
        this.status = "Open";
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param caseNumber Unique case identifier
     * @param title Case title
     * @param caseType Type of case
     * @param clientId ID of the associated client
     */
    public Case(String caseNumber, String title, String caseType, int clientId) {
        this();
        this.caseNumber = caseNumber;
        this.title = title;
        this.caseType = caseType;
        this.clientId = clientId;
    }
    
    /**
     * Full constructor
     * 
     * @param id Database ID
     * @param caseNumber Unique case identifier
     * @param title Case title
     * @param caseType Type of case
     * @param status Case status
     * @param description Case description
     * @param fileDate Date the case was filed
     * @param closingDate Date the case was closed
     * @param court Court where the case is being heard
     * @param judge Judge presiding over the case
     * @param opposingParty Name of opposing party
     * @param opposingCounsel Name of opposing counsel
     * @param clientId ID of the associated client
     */
    public Case(int id, String caseNumber, String title, String caseType, String status, 
                String description, LocalDate fileDate, LocalDate closingDate, String court, 
                String judge, String opposingParty, String opposingCounsel, int clientId) {
        this();
        this.id = id;
        this.caseNumber = caseNumber;
        this.title = title;
        this.caseType = caseType;
        this.status = status;
        this.description = description;
        this.fileDate = fileDate;
        this.closingDate = closingDate;
        this.court = court;
        this.judge = judge;
        this.opposingParty = opposingParty;
        this.opposingCounsel = opposingCounsel;
        this.clientId = clientId;
    }
    
    // Getters and Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getFileDate() {
        return fileDate;
    }

    public void setFileDate(LocalDate fileDate) {
        this.fileDate = fileDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getOpposingParty() {
        return opposingParty;
    }

    public void setOpposingParty(String opposingParty) {
        this.opposingParty = opposingParty;
    }

    public String getOpposingCounsel() {
        return opposingCounsel;
    }

    public void setOpposingCounsel(String opposingCounsel) {
        this.opposingCounsel = opposingCounsel;
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

    public List<Attorney> getAttorneys() {
        return attorneys;
    }

    public void setAttorneys(List<Attorney> attorneys) {
        this.attorneys = attorneys;
    }
    
    /**
     * Add an attorney to this case
     * 
     * @param attorney The attorney to add
     */
    public void addAttorney(Attorney attorney) {
        this.attorneys.add(attorney);
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
    
    /**
     * Add a document to this case
     * 
     * @param document The document to add
     */
    public void addDocument(Document document) {
        this.documents.add(document);
        document.setCase(this);
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    
    /**
     * Add an event to this case
     * 
     * @param event The event to add
     */
    public void addEvent(Event event) {
        this.events.add(event);
        event.setCase(this);
    }

    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    public void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
    }
    
    /**
     * Add a time entry to this case
     * 
     * @param timeEntry The time entry to add
     */
    public void addTimeEntry(TimeEntry timeEntry) {
        this.timeEntries.add(timeEntry);
        timeEntry.setCase(this);
    }
    
    /**
     * Check if the case is closed
     * 
     * @return true if the case status is "Closed"
     */
    public boolean isClosed() {
        return "Closed".equalsIgnoreCase(status);
    }
    
    /**
     * Calculate the total billable hours for this case
     * 
     * @return Sum of hours from all time entries
     */
    public double getTotalHours() {
        double totalHours = 0.0;
        for (TimeEntry entry : timeEntries) {
            totalHours += entry.getHours();
        }
        return totalHours;
    }
    
    @Override
    public String toString() {
        return "Case [id=" + id + ", caseNumber=" + caseNumber + ", title=" + title + 
               ", status=" + status + ", client=" + (client != null ? client.getName() : "Unknown") + "]";
    }
}