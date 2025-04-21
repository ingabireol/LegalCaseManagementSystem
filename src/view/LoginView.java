package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import controller.LoginController;
import model.User;

/**
 * Login screen for the Legal Case Management System.
 */
public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    private JLabel statusLabel;
    private LoginController loginController;
    
    // Custom colors for the application
    private static final Color PRIMARY_COLOR = new Color(42, 58, 86);     // Dark blue
    private static final Color SECONDARY_COLOR = new Color(2, 119, 189);  // Bright blue
    private static final Color ACCENT_COLOR = new Color(245, 245, 245);   // Light gray
    private static final Color TEXT_COLOR = new Color(51, 51, 51);        // Dark gray
    private static final Color ERROR_COLOR = new Color(176, 42, 55);      // Red
    
    /**
     * Constructor
     */
    public LoginView() {
        loginController = new LoginController();
        initializeUI();
    }
    
    /**
     * Initialize the user interface components
     */
    private void initializeUI() {
        setTitle("Legal Case Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 600);
        setLocationRelativeTo(null); // Center on screen
        setResizable(true);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    0, getHeight(), SECONDARY_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        // Logo/title panel
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));
        
        // System logo image (you can replace with your own logo)
        ImageIcon logoIcon = createLogo(120, 120);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // System name
        JLabel titleLabel = new JLabel("Legal Case Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Professional Legal Practice Solution");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        logoPanel.add(logoLabel);
        logoPanel.add(titleLabel);
        logoPanel.add(subtitleLabel);
        
        // Login form panel
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        
        // Create a white, rounded panel for the form
        JPanel loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        
        loginPanel.setOpaque(false);
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, SECONDARY_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, SECONDARY_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(SECONDARY_COLOR);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setFocusPainted(false);
        
        // Exit button
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(PRIMARY_COLOR);
        exitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exitButton.setFocusPainted(false);
        
        // Status label for error messages
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(ERROR_COLOR);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add components to the login panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel loginHeaderLabel = new JLabel("User Login");
        loginHeaderLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loginHeaderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(loginHeaderLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        loginPanel.add(statusLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginPanel.add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        loginPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        loginPanel.add(passwordField, gbc);
        
        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(exitButton);
        buttonPanel.add(loginButton);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        loginPanel.add(buttonPanel, gbc);
        
        formPanel.add(loginPanel);
        
        // Add components to main panel
        mainPanel.add(logoPanel);
        mainPanel.add(formPanel);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        loginButton.addActionListener(e -> performLogin());
        exitButton.addActionListener(e -> System.exit(0));
        
        // Add key listener for Enter key
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });
    }
    
    /**
     * Perform login authentication
     */
    private void performLogin() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password");
            return;
        }
        
        try {
            User user = loginController.authenticateUser(username, password);
            
            if (user != null) {
                // Clear the password array for security
                Arrays.fill(passwordChars, '0');
                
                // Open main application
                openMainApplication(user);
                
                // Close login window
                dispose();
            } else {
                statusLabel.setText("Invalid username or password");
                passwordField.setText("");
            }
        } catch (Exception ex) {
            statusLabel.setText("Login error: " + ex.getMessage());
        }
    }
    
    /**
     * Open the main application window after successful login
     * 
     * @param user The authenticated user
     */
    private void openMainApplication(User user) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView(user);
            mainView.setVisible(true);
        });
    }
    
    /**
     * Create a simple law scales logo image
     * 
     * @param width Width of the logo
     * @param height Height of the logo
     * @return An ImageIcon with the logo
     */
    private ImageIcon createLogo(int width, int height) {
        // Create a buffered image for the logo
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
            width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw a scales of justice symbol
        g2d.setColor(Color.WHITE);
        
        // Base of the scales
        g2d.fillRect(width/2 - 2, height/3, 4, 2*height/3 - 10);
        
        // Top horizontal line
        g2d.fillRect(width/4, height/3, width/2, 4);
        
        // Left scale
        g2d.drawOval(width/4 - 20, height/3 + 10, 40, 40);
        
        // Right scale
        g2d.drawOval(3*width/4 - 20, height/3 + 10, 40, 40);
        
        // Gavel
        g2d.fillRoundRect(width/2 - 25, height/6, 50, 20, 5, 5);
        g2d.fillRect(width/2 + 15, height/6 - 10, 5, 40);
        
        g2d.dispose();
        
        return new ImageIcon(image);
    }
    
    /**
     * Main method to start the application
     */
    public static void main(String[] args) {
        try {
            // Set look and feel to system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}

/**
 * Main application window for the Legal Case Management System.
 */
class MainView extends JFrame {
    private User currentUser;
    
    // Custom colors for the application
    private static final Color PRIMARY_COLOR = new Color(42, 58, 86);     // Dark blue
    private static final Color SECONDARY_COLOR = new Color(2, 119, 189);  // Bright blue
    private static final Color ACCENT_COLOR = new Color(245, 245, 245);   // Light gray
    private static final Color TEXT_COLOR = new Color(51, 51, 51);        // Dark gray
    
    // Content panels
    private JPanel dashboardPanel;
    private JPanel casesPanel;
    private JPanel clientsPanel;
    private JPanel attorneysPanel;
    private JPanel documentsPanel;
    private JPanel calendarPanel;
    private JPanel invoicesPanel;
    private JPanel adminPanel;
    
    // Navigation buttons
    private JButton dashboardButton;
    private JButton casesButton;
    private JButton clientsButton;
    private JButton attorneysButton;
    private JButton documentsButton;
    private JButton calendarButton;
    private JButton invoicesButton;
    private JButton adminButton;
    private JButton logoutButton;
    
    // Card layout for switching between panels
    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    /**
     * Constructor
     * 
     * @param user The authenticated user
     */
    public MainView(User user) {
        this.currentUser = user;
        initializeUI();
    }
    
    /**
     * Initialize the user interface components
     */
    private void initializeUI() {
        setTitle("Legal Case Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null); // Center on screen
        
        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create sidebar navigation panel
        JPanel sidebarPanel = createSidebarPanel();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        
        // Create content panel with card layout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Create all content panels
        createContentPanels();
        
        // Add content panels to card layout
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(casesPanel, "cases");
        contentPanel.add(clientsPanel, "clients");
        contentPanel.add(attorneysPanel, "attorneys");
        contentPanel.add(documentsPanel, "documents");
        contentPanel.add(calendarPanel, "calendar");
        contentPanel.add(invoicesPanel, "invoices");
        contentPanel.add(adminPanel, "admin");
        
        // Show dashboard panel by default
        cardLayout.show(contentPanel, "dashboard");
        highlightSelectedButton(dashboardButton);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add status bar at the bottom
        JPanel statusBar = createStatusBar();
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    /**
     * Create the header panel with user info and system title
     * 
     * @return The header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, SECONDARY_COLOR));
        
        // System title
        JLabel titleLabel = new JLabel("Legal Case Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // User info panel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        userPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        
        JLabel userIconLabel = new JLabel("\uD83D\uDC64"); // Unicode user icon
        userIconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        userIconLabel.setForeground(Color.WHITE);
        
        JLabel userNameLabel = new JLabel(currentUser.getFullName());
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userNameLabel.setForeground(Color.WHITE);
        
        JLabel userRoleLabel = new JLabel("(" + currentUser.getRole() + ")");
        userRoleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        userRoleLabel.setForeground(Color.WHITE);
        
        userPanel.add(userIconLabel);
        userPanel.add(userNameLabel);
        userPanel.add(userRoleLabel);
        
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Create the sidebar panel with navigation buttons
     * 
     * @return The sidebar panel
     */
    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(new Color(52, 68, 96)); // Slightly lighter than PRIMARY_COLOR
        sidebarPanel.setPreferredSize(new Dimension(220, getHeight()));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SECONDARY_COLOR));
        
        // Create navigation buttons
        dashboardButton = createNavButton("Dashboard", "🏠");
        casesButton = createNavButton("Cases", "📁");
        clientsButton = createNavButton("Clients", "👥");
        attorneysButton = createNavButton("Attorneys", "⚖️");
        documentsButton = createNavButton("Documents", "📄");
        calendarButton = createNavButton("Calendar", "📅");
        invoicesButton = createNavButton("Invoices", "💰");
        adminButton = createNavButton("Administration", "⚙️");
        logoutButton = createNavButton("Logout", "🚪");
        
        // Restrict access to admin panel
        adminButton.setVisible(currentUser.isAdmin());
        
        // Add buttons to sidebar
        sidebarPanel.add(Box.createVerticalStrut(20));
        sidebarPanel.add(dashboardButton);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(casesButton);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(clientsButton);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(attorneysButton);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(documentsButton);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(calendarButton);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(invoicesButton);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(adminButton);
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(logoutButton);
        sidebarPanel.add(Box.createVerticalStrut(20));
        
        // Add action listeners
        dashboardButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "dashboard");
            highlightSelectedButton(dashboardButton);
        });
        
        casesButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "cases");
            highlightSelectedButton(casesButton);
        });
        
        clientsButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "clients");
            highlightSelectedButton(clientsButton);
        });
        
        attorneysButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "attorneys");
            highlightSelectedButton(attorneysButton);
        });
        
        documentsButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "documents");
            highlightSelectedButton(documentsButton);
        });
        
        calendarButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "calendar");
            highlightSelectedButton(calendarButton);
        });
        
        invoicesButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "invoices");
            highlightSelectedButton(invoicesButton);
        });
        
        adminButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "admin");
            highlightSelectedButton(adminButton);
        });
        
        logoutButton.addActionListener(e -> logout());
        
        return sidebarPanel;
    }
    
    /**
     * Create a styled navigation button for the sidebar
     * 
     * @param text Button text
     * @param icon Unicode icon
     * @return Styled JButton
     */
    private JButton createNavButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 68, 96)); // Same as sidebar
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setMaximumSize(new Dimension(220, 50));
        
        // Change appearance on hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.getBackground().equals(SECONDARY_COLOR)) {
                    button.setBackground(new Color(62, 78, 106)); // Lighter on hover
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.getBackground().equals(SECONDARY_COLOR)) {
                    button.setBackground(new Color(52, 68, 96)); // Back to normal
                }
            }
        });
        
        return button;
    }
    
    /**
     * Highlight the selected navigation button
     * 
     * @param selectedButton The button to highlight
     */
    private void highlightSelectedButton(JButton selectedButton) {
        // Reset all buttons
        JButton[] buttons = {
            dashboardButton, casesButton, clientsButton, attorneysButton,
            documentsButton, calendarButton, invoicesButton, adminButton
        };
        
        for (JButton button : buttons) {
            if (button != selectedButton) {
                button.setBackground(new Color(52, 68, 96));
                button.setFont(new Font("Arial", Font.PLAIN, 16));
            }
        }
        
        // Highlight selected button
        selectedButton.setBackground(SECONDARY_COLOR);
        selectedButton.setFont(new Font("Arial", Font.BOLD, 16));
    }
    
    /**
     * Create placeholder content panels
     */
    private void createContentPanels() {
        // Dashboard panel
        dashboardPanel = createDashboardPanel();
        
        // Cases panel (placeholder)
        casesPanel = new JPanel(new BorderLayout());
        casesPanel.setBackground(Color.WHITE);
        JLabel casesLabel = new JLabel("Cases Management", SwingConstants.CENTER);
        casesLabel.setFont(new Font("Arial", Font.BOLD, 24));
        casesPanel.add(casesLabel, BorderLayout.CENTER);
        
        // Clients panel (placeholder)
        clientsPanel = new JPanel(new BorderLayout());
        clientsPanel.setBackground(Color.WHITE);
        JLabel clientsLabel = new JLabel("Clients Management", SwingConstants.CENTER);
        clientsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        clientsPanel.add(clientsLabel, BorderLayout.CENTER);
        
        // Attorneys panel (placeholder)
        attorneysPanel = new JPanel(new BorderLayout());
        attorneysPanel.setBackground(Color.WHITE);
        JLabel attorneysLabel = new JLabel("Attorneys Management", SwingConstants.CENTER);
        attorneysLabel.setFont(new Font("Arial", Font.BOLD, 24));
        attorneysPanel.add(attorneysLabel, BorderLayout.CENTER);
        
        // Documents panel (placeholder)
        documentsPanel = new JPanel(new BorderLayout());
        documentsPanel.setBackground(Color.WHITE);
        JLabel documentsLabel = new JLabel("Documents Management", SwingConstants.CENTER);
        documentsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        documentsPanel.add(documentsLabel, BorderLayout.CENTER);
        
        // Calendar panel (placeholder)
        calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setBackground(Color.WHITE);
        JLabel calendarLabel = new JLabel("Calendar and Events", SwingConstants.CENTER);
        calendarLabel.setFont(new Font("Arial", Font.BOLD, 24));
        calendarPanel.add(calendarLabel, BorderLayout.CENTER);
        
        // Invoices panel (placeholder)
        invoicesPanel = new JPanel(new BorderLayout());
        invoicesPanel.setBackground(Color.WHITE);
        JLabel invoicesLabel = new JLabel("Invoices and Payments", SwingConstants.CENTER);
        invoicesLabel.setFont(new Font("Arial", Font.BOLD, 24));
        invoicesPanel.add(invoicesLabel, BorderLayout.CENTER);
        
        // Admin panel (placeholder)
        adminPanel = new JPanel(new BorderLayout());
        adminPanel.setBackground(Color.WHITE);
        JLabel adminLabel = new JLabel("System Administration", SwingConstants.CENTER);
        adminLabel.setFont(new Font("Arial", Font.BOLD, 24));
        adminPanel.add(adminLabel, BorderLayout.CENTER);
    }
    
    /**
     * Create the dashboard panel with statistics and quick access
     * 
     * @return The dashboard panel
     */
    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(Color.WHITE);
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(PRIMARY_COLOR);
        
        titlePanel.add(welcomeLabel);
        dashboardPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Main content with grid of cards
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Create statistic cards
        JPanel activeCasesCard = createStatCard("Active Cases", "42", "📁", new Color(41, 128, 185));
        JPanel clientsCard = createStatCard("Total Clients", "87", "👥", new Color(46, 204, 113));
        JPanel upcomingEventsCard = createStatCard("Upcoming Events", "12", "📅", new Color(155, 89, 182));
        JPanel overdueInvoicesCard = createStatCard("Overdue Invoices", "5", "⚠️", new Color(231, 76, 60));
        
        // Add cards to grid
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(activeCasesCard, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPanel.add(clientsCard, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(upcomingEventsCard, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(overdueInvoicesCard, gbc);
        
        dashboardPanel.add(contentPanel, BorderLayout.CENTER);
        // Create recent activity panel
        JPanel recentActivityPanel = new JPanel(new BorderLayout());
        recentActivityPanel.setBackground(Color.WHITE);
        recentActivityPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 20, 20, 20),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR),
                "Recent Activity",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                SECONDARY_COLOR
            )
        ));
        
        // Recent activity list
        DefaultListModel<String> activityModel = new DefaultListModel<>();
        activityModel.addElement("Case #12345 - Johnson v. Smith - Updated 10 minutes ago");
        activityModel.addElement("Invoice #INV-2023-042 created for Client XYZ Corp - 25 minutes ago");
        activityModel.addElement("Meeting scheduled with Jane Doe - Tomorrow at 9:00 AM");
        activityModel.addElement("New document uploaded to Case #12345 - 2 hours ago");
        activityModel.addElement("Payment received from ABC Holdings - $5,250.00 - Today at 11:15 AM");
        
        JList<String> activityList = new JList<>(activityModel);
        activityList.setFont(new Font("Arial", Font.PLAIN, 14));
        activityList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        activityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane activityScrollPane = new JScrollPane(activityList);
        activityScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        recentActivityPanel.add(activityScrollPane, BorderLayout.CENTER);
        
        dashboardPanel.add(recentActivityPanel, BorderLayout.SOUTH);
        
        return dashboardPanel;
    }
    
    /**
     * Create a statistic card for the dashboard
     * 
     * @param title Card title
     * @param value Statistic value
     * @param icon Unicode icon
     * @param color Card color
     * @return The statistic card panel
     */
    private JPanel createStatCard(String title, String value, String icon, Color color) {
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw background with slight gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, color,
                    0, getHeight(), color.darker());
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        cardPanel.setPreferredSize(new Dimension(250, 150));
        
        // Card title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Card value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Card icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setForeground(new Color(255, 255, 255, 200));
        iconLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        iconLabel.setVerticalAlignment(SwingConstants.TOP);
        
        // Panel for title and icon
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(iconLabel, BorderLayout.EAST);
        
        cardPanel.add(topPanel, BorderLayout.NORTH);
        cardPanel.add(valueLabel, BorderLayout.CENTER);
        
        return cardPanel;
    }
    
    /**
     * Create the status bar for the bottom of the main window
     * 
     * @return The status bar panel
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(ACCENT_COLOR);
        statusBar.setPreferredSize(new Dimension(getWidth(), 25));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        // Status message
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        // Current date/time
        JLabel dateTimeLabel = new JLabel(java.time.LocalDate.now().toString());
        dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateTimeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        statusBar.add(dateTimeLabel, BorderLayout.EAST);
        
        return statusBar;
    }
    
    /**
     * Perform logout and return to login screen
     */
    private void logout() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            });
        }
    }
}
