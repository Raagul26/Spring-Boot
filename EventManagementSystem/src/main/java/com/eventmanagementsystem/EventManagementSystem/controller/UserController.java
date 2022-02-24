package com.eventmanagementsystem.EventManagementSystem.controller;

import com.eventmanagementsystem.EventManagementSystem.controller.response.ApiResponse;
import com.eventmanagementsystem.EventManagementSystem.model.Booking;
import com.eventmanagementsystem.EventManagementSystem.model.User;
import com.eventmanagementsystem.EventManagementSystem.model.UserLogin;
import com.eventmanagementsystem.EventManagementSystem.model.UserUpdate;
import com.eventmanagementsystem.EventManagementSystem.repository.AdminRepository;
import com.eventmanagementsystem.EventManagementSystem.repository.UserRepository;
import com.eventmanagementsystem.EventManagementSystem.service.BookingService;
import com.eventmanagementsystem.EventManagementSystem.service.UserService;
import com.eventmanagementsystem.EventManagementSystem.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AdminRepository adminRepository;

    // user signup
    @PostMapping(value="/signup",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody User user)
    {
        ApiResponse response = new ApiResponse();
        try
        {
            userService.createUser(user);
            response.setStatus("Success");
            response.setMessage("User Created Successfully");
            return new ResponseEntity<>(response,HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    // user login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> getUserByEmailIdAndPassword(@Valid @RequestBody UserLogin userLogin)
    {
        ApiResponse response = new ApiResponse();
        try
        {
            userService.getUserByEmailIdAndPassword(userLogin);
            response.setStatus("Success");
            response.setMessage("User Logged in successfully");
            response.setAccessToken(jwtUtility.generateToken(userLogin));
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getUsers(@RequestHeader(value = "authorization") String auth)
    {
        ApiResponse response = new ApiResponse();
        try
        {
            if(adminRepository.findByEmailId(jwtUtility.getUsernameFromToken(auth))==null)
            {
                response.setStatus("Access denied");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            jwtUtility.validateToken(auth);
            response.setData(userService.getAllUsers());
            response.setStatus("Success");
            response.setMessage("Fetched Successfully");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable String userId, @Valid @RequestBody UserUpdate user, @RequestHeader(value = "authorization") String auth)
    {
        ApiResponse response = new ApiResponse();
        try
        {
            if(userRepository.findByEmailId(jwtUtility.getUsernameFromToken(auth))==null)
            {
                response.setStatus("Access denied");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            jwtUtility.validateToken(auth);
            userService.updateUser(userId, user);
            response.setStatus("Success");
            response.setMessage("User Updated Successfully");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            response.setErrorCode("400");
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/bookEvent")
    public ResponseEntity<ApiResponse> bookEvent(@RequestBody Booking booking, @RequestHeader(value = "authorization") String auth)
    {
        ApiResponse response = new ApiResponse();
        try
        {
            if(userRepository.findByEmailId(jwtUtility.getUsernameFromToken(auth))==null)
            {
                response.setStatus("Access denied");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            jwtUtility.validateToken(auth);
            bookingService.bookEvent(booking);
            response.setStatus("Success");
            response.setMessage("Event Booked Successfully");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/bookedEvents/{userId}")
    public ResponseEntity<ApiResponse> bookedEvents(@PathVariable String userId, @RequestHeader(value = "authorization") String auth)
    {
        ApiResponse response = new ApiResponse();
        try
        {
            if(userRepository.findByEmailId(jwtUtility.getUsernameFromToken(auth))==null)
            {
                response.setStatus("Access denied");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            jwtUtility.validateToken(auth);
            response.setData(bookingService.getEventsByUserId(userId));
            response.setStatus("Success");
            response.setMessage("Fetched booked events");
            return  new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cancelEvent")
    public ResponseEntity<ApiResponse> cancelEvent(@RequestBody Booking booking, @RequestHeader(value = "authorization") String auth)
    {
        ApiResponse response = new ApiResponse();
        try
        {
            if(userRepository.findByEmailId(jwtUtility.getUsernameFromToken(auth))==null)
            {
                response.setStatus("Access denied");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            jwtUtility.validateToken(auth);
            bookingService.cancelEventBooking(booking);
            response.setStatus("Success");
            response.setMessage("Cancelled Event");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            return  new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

}
