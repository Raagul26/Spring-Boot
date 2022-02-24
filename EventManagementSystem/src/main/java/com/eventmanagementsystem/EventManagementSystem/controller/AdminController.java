package com.eventmanagementsystem.EventManagementSystem.controller;

import com.eventmanagementsystem.EventManagementSystem.controller.response.ApiResponse;
import com.eventmanagementsystem.EventManagementSystem.model.Admin;
import com.eventmanagementsystem.EventManagementSystem.model.User;
import com.eventmanagementsystem.EventManagementSystem.repository.AdminRepository;
import com.eventmanagementsystem.EventManagementSystem.service.AdminService;
import com.eventmanagementsystem.EventManagementSystem.service.BookingService;
import com.eventmanagementsystem.EventManagementSystem.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    public AdminService adminService;

    @Autowired
    public BookingService bookingService;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/login")
    public ResponseEntity<ApiResponse> adminLogin(@Valid @RequestBody Admin adminLogin)
    {
        ApiResponse response = new ApiResponse();
        try
        {
            adminService.getAdminByEmailIdAndPassword(adminLogin);
            response.setStatus("Success");
            response.setMessage("Admin Logged in successfully");
            response.setAccessToken(jwtUtility.generateToken(adminLogin));
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/bookedUsers/{eventId}")
    public ResponseEntity<ApiResponse> bookingsForEvent(@PathVariable String eventId, @RequestHeader(value = "authorization", defaultValue = "") String auth)
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
            response.setStatus("Success");
            response.setMessage("Fetched Event");
            List<User> data = bookingService.getBookersByEventId(eventId);
            response.setData(data);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

}
