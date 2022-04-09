package com.eventmanagementsystem.EventManagementSystem.repository;

import com.eventmanagementsystem.EventManagementSystem.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {

    Booking findByEventIdAndUserIdAndAttendedStatus(String eventId, String userId, String attendedStatus);

    Booking findByEventIdAndUserId(String eventId, String userId);

    List<Booking> findByEventIdAndAttendedStatus(String eventId, String attendedStatus);

    List<Booking> findByUserIdAndAttendedStatus(String userId, String attendedStatus);

}
