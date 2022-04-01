package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.exception.EmailIdAlreadyExistException;
import com.eventmanagementsystem.EventManagementSystem.exception.InvalidCredException;
import com.eventmanagementsystem.EventManagementSystem.exception.UserNotFoundException;
import com.eventmanagementsystem.EventManagementSystem.model.User;
import com.eventmanagementsystem.EventManagementSystem.model.UserLogin;
import com.eventmanagementsystem.EventManagementSystem.model.UserUpdate;
import com.eventmanagementsystem.EventManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void createUser(User user) {
        User newUser = new User();
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        if (userRepository.findByEmailId(user.getEmailId()) == null) {
            int userId = userRepository.findAll().size() + 1;
            newUser.setUserId("USR" + userId);
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            newUser.setEmailId(user.getEmailId());
            newUser.setPassword(encodedPassword);
            newUser.setContactNo(user.getContactNo());
            newUser.setUserType("user");
            newUser.setCreatedOn(dateFormat.format(date));
            newUser.setStatus("active");
            userRepository.insert(newUser);
        } else {
            throw new EmailIdAlreadyExistException("Email Id Already exists");
        }
    }

    @Override
    public User getUserByEmailId(String emailId) {
        return userRepository.findByEmailId(emailId);
    }

    @Override
    public Map<String, String> userLogin(UserLogin userLoginDetails) {
        try {
            String encodedPassword = getUserByEmailId(userLoginDetails.getEmailId()).getPassword();
            if (!bCryptPasswordEncoder.matches(userLoginDetails.getPassword(), encodedPassword)) {
                throw new InvalidCredException("Invalid EmailId or Password");
            }
            User user = getUserByEmailId(userLoginDetails.getEmailId());
            Map<String,String> op = new HashMap<String,String>();
            op.put("userId",user.getUserId());
            return op;
        }
        catch (NullPointerException e)
        {
            throw new InvalidCredException("Invalid EmailId or Password");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(String userId, UserUpdate user) {
        User tempUser = userRepository.findByUserId(userId);

        if (tempUser != null) {
            if (user.getContactNo()!=null && user.getFirstName()!=null && user.getLastName()!=null) {
                if (user.getFirstName() != null) {
                    tempUser.setFirstName(user.getFirstName());
                }
                if (user.getLastName() != null) {
                    tempUser.setLastName(user.getLastName());
                }
                if (user.getContactNo() != null) {
                    tempUser.setContactNo(user.getContactNo());
                }

                userRepository.save(tempUser);
            }
            else {
                throw new InvalidCredException("Invalid data");
            }
        } else {
            throw new UserNotFoundException("User not found with userId "+userId);
        }
    }
}
