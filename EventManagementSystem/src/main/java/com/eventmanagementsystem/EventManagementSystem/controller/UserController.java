package com.eventmanagementsystem.EventManagementSystem.controller;

import com.eventmanagementsystem.EventManagementSystem.controller.response.ApiResponse;
import com.eventmanagementsystem.EventManagementSystem.enums.Status;
import com.eventmanagementsystem.EventManagementSystem.model.Booking;
import com.eventmanagementsystem.EventManagementSystem.model.User;
import com.eventmanagementsystem.EventManagementSystem.model.UserLogin;
import com.eventmanagementsystem.EventManagementSystem.model.UserUpdate;
import com.eventmanagementsystem.EventManagementSystem.repository.UserRepository;
import com.eventmanagementsystem.EventManagementSystem.service.BookingService;
import com.eventmanagementsystem.EventManagementSystem.service.UserService;
import com.eventmanagementsystem.EventManagementSystem.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

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

    // user signup
    @PostMapping(value = "/signup")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody User user) {
        userService.createUser(user);
        ApiResponse responseBody = new ApiResponse();
        responseBody.setStatus(Status.SUCCESS.name());
        responseBody.setMessage("User Created Successfully");
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    // user login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> userLogin(@Valid @RequestBody UserLogin userLoginDetails) {
        ApiResponse responseBody = new ApiResponse();
        responseBody.setData(userService.userLogin(userLoginDetails));
        responseBody.setStatus(Status.SUCCESS.name());
        responseBody.setMessage("User Logged in successfully");
        String jwtToken = jwtUtility.generateToken(userLoginDetails);
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("JWTToken", jwtToken);
        return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.OK);
    }

    // get all users
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getUsers(@RequestHeader(value = "authorization") String auth) {
        ApiResponse responseBody = new ApiResponse();
        if (Objects.equals(jwtUtility.validateToken(auth), "admin")) {
            responseBody.setData(userService.getAllUsers());
            responseBody.setStatus(Status.SUCCESS.name());
            responseBody.setMessage("Fetched Successfully");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.setStatus(Status.FAILED.name());
            responseBody.setMessage("Access denied");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
    }

    // update user
    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable String userId, @Valid @RequestBody UserUpdate user, @RequestHeader(value = "authorization") String auth) {
        ApiResponse responseBody = new ApiResponse();
        if (Objects.equals(jwtUtility.validateToken(auth), "user")) {
            userService.updateUser(userId, user);
            responseBody.setStatus(Status.SUCCESS.name());
            responseBody.setMessage("User Updated Successfully");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.setStatus(Status.FAILED.name());
            responseBody.setMessage("Access denied");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
    }

    // book event
    @PostMapping("/bookEvent")
    public ResponseEntity<ApiResponse> bookEvent(@RequestBody Booking booking, @RequestHeader(value = "authorization") String auth) {
        ApiResponse responseBody = new ApiResponse();
        if (Objects.equals(jwtUtility.validateToken(auth), "user")) {
            bookingService.bookEvent(booking);
            responseBody.setStatus(Status.SUCCESS.name());
            responseBody.setMessage("Event Booked Successfully");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.setStatus(Status.FAILED.name());
            responseBody.setMessage("Access Denied");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
    }

    // get booked events
    @GetMapping("/bookedEvents/{userId}")
    public ResponseEntity<ApiResponse> bookedEvents(@PathVariable String userId, @RequestHeader(value = "authorization") String auth) {
        ApiResponse responseBody = new ApiResponse();
        if (Objects.equals(jwtUtility.validateToken(auth), "user")) {
            responseBody.setData(bookingService.getEventsByUserId(userId));
            responseBody.setStatus(Status.SUCCESS.name());
            responseBody.setMessage("Fetched booked events");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.setStatus(Status.FAILED.name());
            responseBody.setMessage("Access denied");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
    }

    // cancel event
    @PostMapping("/cancelEvent")
    public ResponseEntity<ApiResponse> cancelEvent(@RequestBody Booking booking, @RequestHeader(value = "authorization") String auth) {
        ApiResponse responseBody = new ApiResponse();
        if (Objects.equals(jwtUtility.validateToken(auth), "user")) {
            bookingService.cancelEventBooking(booking);
            responseBody.setStatus(Status.SUCCESS.name());
            responseBody.setMessage("Cancelled Event");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.setStatus(Status.FAILED.name());
            responseBody.setMessage("Access denied");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
    }

    // get User
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable String userId, @RequestHeader(value = "authorization") String auth)
    {
        ApiResponse responseBody = new ApiResponse();
        if (Objects.equals(jwtUtility.validateToken(auth), "user")) {
            responseBody.setData(userRepository.findByUsersId(userId));
            responseBody.setStatus(Status.SUCCESS.name());
            responseBody.setMessage("Fetched User Successfully");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.setStatus(Status.FAILED.name());
            responseBody.setMessage("Access denied");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
    }

}
