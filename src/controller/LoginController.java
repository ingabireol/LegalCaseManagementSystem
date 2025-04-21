package controller;

public class LoginController {
    private dao.LoginDao loginDao;
    
    /**
     * Constructor
     */
    public LoginController() {
        this.loginDao = new dao.LoginDao();
    }
    
    /**
     * Authenticate a user
     * 
     * @param username The username
     * @param password The password
     * @return The authenticated user, or null if authentication failed
     */
    public model.User authenticateUser(String username, String password) {
        return loginDao.authenticateUser(username, password);
    }
}