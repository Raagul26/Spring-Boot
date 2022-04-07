package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.exception.EmailIdAlreadyExistException;
import com.eventmanagementsystem.EventManagementSystem.exception.InvalidCredException;
import com.eventmanagementsystem.EventManagementSystem.exception.UserNotFoundException;
import com.eventmanagementsystem.EventManagementSystem.model.User;
import com.eventmanagementsystem.EventManagementSystem.model.UserLogin;
import com.eventmanagementsystem.EventManagementSystem.model.UserUpdate;
import com.eventmanagementsystem.EventManagementSystem.repository.UserRepository;
import com.eventmanagementsystem.EventManagementSystem.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JwtUtility jwtUtility;

    @Override
    public void createUser(User user) {
        User newUser = new User();
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        if (userRepository.findByEmailId(user.getEmailId()) == null) {
            int userId = userRepository.findAllUser().size() + 1;
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
           sendMail(user.getEmailId(),"Welcome to EMS","Account Created Successfully!");
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

    @Override
    public void sendMail(String sender, String subject, String link) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("raagul26@gmail.com");
            helper.setTo(sender);
            helper.setSubject(subject);
            String htmlText = "<html><body><p>You're receiving this e-mail because you requested a password reset for "
                    +"your EMS account.</p></br><p>Please click the link below to choose a new password.</p><a href='"
                    +link+"'>Reset Password</a></body></html>";
            helper.setText(htmlText,true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean forgotPassword(String emailId) {
        User user = userRepository.findByEmailId(emailId);
        if(user!=null)
        {
            sendMail(emailId,"Reset Password","http://localhost:4200/reset_password/"+jwtUtility.generateToken(emailId));
            return true;
        }
        return false;
    }

    @Override
    public boolean resetPassword(Map<String, String> resetPasswordCredentials) {
        User user = userRepository.findByEmailId(resetPasswordCredentials.get("emailId"));
        if(user!=null)
        {
            String encodedPassword = bCryptPasswordEncoder.encode(resetPasswordCredentials.get("password"));
            user.setPassword(encodedPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
