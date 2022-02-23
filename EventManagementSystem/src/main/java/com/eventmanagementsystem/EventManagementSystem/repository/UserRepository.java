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

    @Query(value = "{emailId:?0}")
    User findByEmailId(String emailId);

    @Query(value = "{userId:?0}", fields = "{password:0}")
    User findByUsersId(String userId);

    @Query(value = "{userId:?0}")
    User findByUserId(String userId);

    @Query("{emailId:?0, password:?1}")
    List<User> findByEmailIdAndPassword(String emailId, String password);

}
