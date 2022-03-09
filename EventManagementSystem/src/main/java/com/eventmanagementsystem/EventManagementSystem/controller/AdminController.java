package com.eventmanagementsystem.EventManagementSystem.controller;

import com.eventmanagementsystem.EventManagementSystem.controller.response.ApiResponse;
import com.eventmanagementsystem.EventManagementSystem.enums.Status;
import com.eventmanagementsystem.EventManagementSystem.model.Admin;
import com.eventmanagementsystem.EventManagementSystem.repository.AdminRepository;
import com.eventmanagementsystem.EventManagementSystem.repository.BookingRepository;
import com.eventmanagementsystem.EventManagementSystem.service.AdminService;
import com.eventmanagementsystem.EventManagementSystem.service.BookingService;
import com.eventmanagementsystem.EventManagementSystem.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // admin login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> adminLogin(@Valid @RequestBody Admin adminLoginDetails) {
        ApiResponse responseBody = new ApiResponse();
        if (adminService.adminLogin(adminLoginDetails)) {
            responseBody.setStatus(Status.SUCCESS.name());
            responseBody.setMessage("Admin Logged in successfully");
            String jwtToken = jwtUtility.generateToken(adminLoginDetails);
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.set("JWTToken", jwtToken);
            return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.OK);
        } else {
            responseBody.setStatus(Status.FAILED.name());
            responseBody.setMessage("Invalid email id or password");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
    }

    // get booked users
    @GetMapping("/bookedUsers/{title}")
    public ResponseEntity<ApiResponse> bookingsForEvent(@PathVariable String title, @RequestHeader(value = "authorization") String auth) {
        ApiResponse responseBody = new ApiResponse();
        if (jwtUtility.validateAdminToken(auth)) {
            responseBody.setStatus(Status.SUCCESS.name());
            responseBody.setMessage("Fetched Booked Users");
            responseBody.setData(bookingService.getBookersByEventTitle(title));
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.setStatus(Status.FAILED.name());
            responseBody.setMessage("Access denied");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
    }

    // total bookings
    @GetMapping("/booked")
    public ResponseEntity<ApiResponse> bookingsForEvent(@RequestHeader(value = "authorization") String auth) {
        ApiResponse responseBody = new ApiResponse();
        if (jwtUtility.validateAdminToken(auth)) {
            responseBody.setStatus(Status.SUCCESS.name());
            responseBody.setMessage("Fetched Booked Users");
            responseBody.setData(bookingRepository.findAll().size());
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.setStatus(Status.FAILED.name());
            responseBody.setMessage("Access denied");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
    }
}
