package com.eventmanagementsystem.EventManagementSystem.repository;

import com.eventmanagementsystem.EventManagementSystem.model.Admin;
import com.eventmanagementsystem.EventManagementSystem.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AdminRepository extends MongoRepository<Admin,String> {

    @Query("{emailId:?0, password:?1}")
    List<Admin> findByEmailIdAndPassword(String emailId, String password);

    Admin  findByEmailId(String emailId);
}
