package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.model.Admin;
import com.eventmanagementsystem.EventManagementSystem.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Boolean adminLogin(Admin adminLoginDetails) {
        Admin admin = adminRepository.findByEmailId(adminLoginDetails.getEmailId());
        return admin != null && bCryptPasswordEncoder.matches(adminLoginDetails.getPassword(), admin.getPassword());
    }

}
