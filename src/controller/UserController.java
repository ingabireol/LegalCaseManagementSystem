package controller;

import dao.LoginDao;
import model.User;

import java.util.List;

/**
 * Controller for user-related operations.
 */
public class UserController {
    private LoginDao loginDao;
    
    /**
     * Constructor
     */
    public UserController() {
        this.loginDao = new LoginDao();
    }
    
    /**
     * Authenticate a user
     * 
     * @param username The username
     * @param password The password
     * @return The authenticated user, or null if authentication failed
     */
    public User authenticateUser(String username, String password) {
        return loginDao.authenticateUser(username, password);
    }
    
    /**
     * Get a user by ID
     * 
     * @param userId The user ID
     * @return The user
     */
    public User getUserById(int userId) {
        return loginDao.findUserById(userId);
    }
    
    /**
     * Get a user by username
     * 
     * @param username The username
     * @return The user
     */
    public User getUserByUsername(String username) {
        return loginDao.findUserByUsername(username);
    }
    
    /**
     * Get a user by email
     * 
     * @param email The email
     * @return The user
     */
    public User getUserByEmail(String email) {
        return loginDao.findUserByEmail(email);
    }
    
    /**
     * Get all active users
     * 
     * @return List of active users
     */
    public List<User> getAllActiveUsers() {
        return loginDao.findAllActiveUsers();
    }
    
    /**
     * Get users by role
     * 
     * @param role The role
     * @return List of users with the role
     */
    public List<User> getUsersByRole(String role) {
        return loginDao.findUsersByRole(role);
    }
    
    /**
     * Create a new user
     * 
     * @param user The user to create
     * @param password The password
     * @return The created user, or null if creation failed
     */
    public User createUser(User user, String password) {
        return loginDao.createUser(user, password);
    }
    
    /**
     * Update a user
     * 
     * @param user The user to update
     * @return true if successful
     */
    public boolean updateUser(User user) {
        return loginDao.updateUser(user);
    }
    
    /**
     * Change a user's password
     * 
     * @param userId The user ID
     * @param currentPassword The current password
     * @param newPassword The new password
     * @return true if successful
     */
    public boolean changePassword(int userId, String currentPassword, String newPassword) {
        return loginDao.changePassword(userId, currentPassword, newPassword);
    }
    
    /**
     * Reset a user's password
     * 
     * @param email The user's email
     * @return The new password, or null if reset failed
     */
    public String resetPassword(String email) {
        return loginDao.resetPassword(email);
    }
    
    /**
     * Deactivate a user
     * 
     * @param userId The user ID
     * @return true if successful
     */
    public boolean deactivateUser(int userId) {
        return loginDao.deactivateUser(userId);
    }
    
    /**
     * Reactivate a user
     * 
     * @param userId The user ID
     * @return true if successful
     */
    public boolean reactivateUser(int userId) {
        return loginDao.reactivateUser(userId);
    }
    
    /**
     * Check if a username exists
     * 
     * @param username The username to check
     * @return true if the username exists
     */
    public boolean isUsernameExists(String username) {
        return loginDao.isUsernameExists(username);
    }
    
    /**
     * Get available user roles
     * 
     * @return Array of user roles
     */
    public String[] getUserRoles() {
        return new String[] {
            User.ROLE_ADMIN,
            User.ROLE_ATTORNEY,
            User.ROLE_STAFF,
            User.ROLE_FINANCE,
            User.ROLE_READONLY
        };
    }
}