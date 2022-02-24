package com.eventmanagementsystem.EventManagementSystem.repository;

import com.eventmanagementsystem.EventManagementSystem.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

    @Override
    @Query(value = "{}", fields = "{password:0}")
    List<User> findAll();

    User findByEmailId(String emailId);

    @Query(value = "{userId:?0}", fields = "{password:0}")
    User findByUsersId(String userId);

    User findByUserId(String userId);

}
