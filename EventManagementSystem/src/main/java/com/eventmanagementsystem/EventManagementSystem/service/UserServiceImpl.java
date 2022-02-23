package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.model.User;
import com.eventmanagementsystem.EventManagementSystem.model.UserLogin;
import com.eventmanagementsystem.EventManagementSystem.model.UserUpdate;
import com.eventmanagementsystem.EventManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserServiceImpl implements  UserService{

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void createUser(User user)
    {
        User newUser = new User();
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        if(userRepository.findByEmailId(user.getEmailId())==null)
        {
            int userId = userRepository.findAll().size() + 1;
            newUser.setUserId("USR"+userId);
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            newUser.setEmailId(user.getEmailId());
            newUser.setPassword(encodedPassword);
            newUser.setContactNo(user.getContactNo());
            newUser.setCreatedOn(dateFormat.format(date));
            newUser.setStatus("active");

            userRepository.insert(newUser);
        }
        else
        {
            throw new RuntimeException("Email id already exists");
        }
    }

    @Override
    public User getUserByEmailId(String emailId)
    {
        if(userRepository.findByEmailId(emailId)!=null)
        {
            return userRepository.findByEmailId(emailId);
        }
        else
        {
            throw new RuntimeException("Email id not found");
        }
    }

    @Override
    public void getUserByEmailIdAndPassword(UserLogin userLogin) {
        if(!bCryptPasswordEncoder.matches(userLogin.getPassword(), getUserByEmailId(userLogin.getEmailId()).getPassword()))
        {
            throw new RuntimeException("Invalid email id or password");
        }
    }

    @Override
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(String userId, UserUpdate user) {
        User tempUser = userRepository.findByUserId(userId);

        if(tempUser!=null) {
            if (!tempUser.getEmailId().equals(user.getEmailId()) && userRepository.findByEmailId(user.getEmailId())!=null) {
                throw new RuntimeException("Email Id already exists");
            } else {
                if(user.getEmailId()!=null)
                {
                    tempUser.setEmailId(user.getEmailId());
                }
            }
            if(user.getFirstName()!=null)
            {
                tempUser.setFirstName(user.getFirstName());
            }
            if(user.getLastName()!=null)
            {
                tempUser.setLastName(user.getLastName());
            }
            if(user.getContactNo()!=null){
                tempUser.setContactNo(user.getContactNo());
            }

            userRepository.save(tempUser);
        }
        else
        {
            throw new RuntimeException("No user found with id "+userId);
        }
    }
}
