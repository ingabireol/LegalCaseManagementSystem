/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import dao.LoginDao;
import java.time.LocalDate;
import java.util.Date;
import model.User;

/**
 *
 * @author orny
 */
public class CreateUser {
    public static void main(String[] args) {
        LoginDao dao = new LoginDao();
        User user = new User();
        user.setEmail("ornella@gmail.com");
        user.setFullName("Umutoni Ornella");
        user.setRegistrationDate(LocalDate.now());
        user.setUsername("ornella");
        user.setRole("Admin");        
        user = dao.createUser(user, "admin123");
        System.out.println(user.toString());
    }
    
}
