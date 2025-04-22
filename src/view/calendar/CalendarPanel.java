package view.calendar;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import controller.EventController;
import view.util.UIConstants;
import view.components.TableFilterPanel;

/**
 * Panel for calendar and events management in the Legal Case Management System.
 */
public class CalendarPanel extends JPanel {
    private EventController eventController;
    private LocalDate currentDate;
    private JPanel calendarGrid;
    private JLabel monthYearLabel;
    
    /**
     * Constructor
     */
    public CalendarPanel() {
        this.eventController = new EventController();
        this.currentDate = LocalDate.now();
        
        initializeUI();
    }
    
    /**
     * Initialize the user interface components
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create calendar panel
        JPanel calendarPanel = createCalendarPanel();
        add(calendarPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create the header panel with title and controls
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
        
        JLabel titleLabel = new JLabel("Calendar & Events");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titlePanel.add(titleLabel);
        
        // Month navigation panel
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        navigationPanel.setBackground(Color.WHITE);
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        
        JButton prevMonthButton = new JButton("◀");
        prevMonthButton.setFont(UIConstants.NORMAL_FONT);
        prevMonthButton.setFocusPainted(false);
        prevMonthButton.addActionListener(e -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendarView();
        });
        
        monthYearLabel = new JLabel();
        monthYearLabel.setFont(UIConstants.SUBTITLE_FONT);
        monthYearLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthYearLabel.setPreferredSize(new Dimension(200, 30));
        
        JButton nextMonthButton = new JButton("▶");
        nextMonthButton.setFont(UIConstants.NORMAL_FONT);
        nextMonthButton.setFocusPainted(false);
        nextMonthButton.addActionListener(e -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendarView();
        });
        
        JButton todayButton = new JButton("Today");
        todayButton.setFont(UIConstants.NORMAL_FONT);
        todayButton.setFocusPainted(false);
        todayButton.addActionListener(e -> {
            currentDate = LocalDate.now();
            updateCalendarView();
        });
        
        navigationPanel.add(prevMonthButton);
        navigationPanel.add(Box.createHorizontalStrut(10));
        navigationPanel.add(monthYearLabel);
        navigationPanel.add(Box.createHorizontalStrut(10));
        navigationPanel.add(nextMonthButton);
        navigationPanel.add(Box.createHorizontalStrut(20));
        navigationPanel.add(todayButton);
        
        headerPanel.add(titlePanel, BorderLayout.NORTH);
        headerPanel.add(navigationPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Create the calendar panel with month view
     * 
     * @return The calendar panel
     */
    private JPanel createCalendarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Create day of week header
        JPanel weekDaysPanel = new JPanel(new GridLayout(1, 7));
        weekDaysPanel.setBackground(Color.WHITE);
        
        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (String day : weekDays) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(UIConstants.NORMAL_FONT.deriveFont(Font.BOLD));
            dayLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            weekDaysPanel.add(dayLabel);
        }
        
        // Create calendar grid
        calendarGrid = new JPanel(new GridLayout(6, 7));
        calendarGrid.setBackground(Color.WHITE);
        
        panel.add(weekDaysPanel, BorderLayout.NORTH);
        panel.add(calendarGrid, BorderLayout.CENTER);
        
        // Initialize calendar view
        updateCalendarView();
        
        return panel;
    }
    
    /**
     * Update the calendar grid based on the current month
     */
    private void updateCalendarView() {
        // Update month/year label
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthYearLabel.setText(currentDate.format(formatter));
        
        // Clear existing calendar cells
        calendarGrid.removeAll();
        
        // Get month information
        YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonth());
        int daysInMonth = yearMonth.lengthOfMonth();
        int firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue() % 7; // Adjust for Sunday start
        
        // Add empty cells for days before the first day of the month
        for (int i = 0; i < firstDayOfWeek; i++) {
            JPanel emptyCell = createEmptyCalendarCell();
            calendarGrid.add(emptyCell);
        }
        
        // Add cells for each day of the month
        LocalDate today = LocalDate.now();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), day);
            JPanel dayCell = createCalendarDayCell(date, date.equals(today));
            calendarGrid.add(dayCell);
        }
        
        // Add empty cells for remaining grid spaces
        int remainingCells = 42 - daysInMonth - firstDayOfWeek; // 6 rows * 7 days = 42
        for (int i = 0; i < remainingCells; i++) {
            JPanel emptyCell = createEmptyCalendarCell();
            calendarGrid.add(emptyCell);
        }
        
        // Refresh the view
        calendarGrid.revalidate();
        calendarGrid.repaint();
    }
    
    /**
     * Create an empty calendar cell
     * 
     * @return Empty cell panel
     */
    private JPanel createEmptyCalendarCell() {
        JPanel cell = new JPanel();
        cell.setBackground(Color.WHITE);
        cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return cell;
    }
    
    /**
     * Create a calendar cell for a specific day
     * 
     * @param date The date for the cell
     * @param isToday Whether this date is today
     * @return Calendar day cell panel
     */
    private JPanel createCalendarDayCell(LocalDate date, boolean isToday) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setBackground(Color.WHITE);
        
        // Add border, highlight today's date
        if (isToday) {
            cell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.SECONDARY_COLOR, 2),
                BorderFactory.createEmptyBorder(2, 4, 2, 4)
            ));
        } else {
            cell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
            ));
        }
        
        // Day number label
        JLabel dayLabel = new JLabel(Integer.toString(date.getDayOfMonth()));
        dayLabel.setFont(UIConstants.NORMAL_FONT);
        if (isToday) {
            dayLabel.setForeground(UIConstants.SECONDARY_COLOR);
            dayLabel.setFont(UIConstants.NORMAL_FONT.deriveFont(Font.BOLD));
        }
        
        cell.add(dayLabel, BorderLayout.NORTH);
        
        // Placeholder for events that would be shown in this cell
        if (date.getDayOfMonth() % 4 == 0) { // Just for demonstration
            JLabel eventLabel = new JLabel("Sample Event");
            eventLabel.setFont(UIConstants.SMALL_FONT);
            eventLabel.setForeground(UIConstants.SECONDARY_COLOR);
            cell.add(eventLabel, BorderLayout.CENTER);
        }
        
        // Make cell interactive
        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showDayEvents(date);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isToday) {
                    cell.setBackground(UIConstants.ACCENT_COLOR);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                cell.setBackground(Color.WHITE);
            }
        });
        
        return cell;
    }
    
    /**
     * Show events for a specific day
     * 
     * @param date The date to show events for
     */
    private void showDayEvents(LocalDate date) {
        // Placeholder for showing events
        JOptionPane.showMessageDialog(
            this,
            "Events for " + date.toString() + "\n" +
            "This feature will be fully implemented in the future.",
            "Day Events",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}