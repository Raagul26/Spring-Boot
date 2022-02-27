package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.model.User;
import com.eventmanagementsystem.EventManagementSystem.model.UserLogin;
import com.eventmanagementsystem.EventManagementSystem.model.UserUpdate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    void createUser(User user);

    List<User> getAllUsers();

    User getUserByEmailId(String emailId);

    void userLogin(UserLogin userLoginDetails);

    void updateUser(String userId, UserUpdate user);

}
