package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.model.User;
import com.eventmanagementsystem.EventManagementSystem.model.UserLogin;
import com.eventmanagementsystem.EventManagementSystem.model.UserUpdate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {

    void createUser(User user);

    List<User> getAllUsers();

    User getUserByEmailId(String emailId);

    Map<String,String> userLogin(UserLogin userLoginDetails);

    void updateUser(String userId, UserUpdate user);

    void sendMail(String sender,String subject, String link);

    boolean forgotPassword(String emailId);

    boolean resetPassword(Map<String,String> resetPasswordCredentials);

}
