package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.model.Admin;
import com.eventmanagementsystem.EventManagementSystem.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    public AdminRepository adminRepository;

    @Override
    public void getAdminByEmailIdAndPassword(Admin adminLogin) {

       if(adminRepository.findByEmailIdAndPassword(adminLogin.getEmailId(),adminLogin.getPassword()).size() != 1)
       {
           throw new RuntimeException("Invalid email id or password");
       }
    }

}
