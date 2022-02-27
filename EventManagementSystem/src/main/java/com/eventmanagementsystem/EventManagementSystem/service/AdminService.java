package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.model.Admin;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    Boolean adminLogin(Admin adminLogin);
}
