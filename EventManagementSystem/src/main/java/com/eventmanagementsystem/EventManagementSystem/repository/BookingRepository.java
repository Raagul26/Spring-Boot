package com.eventmanagementsystem.EventManagementSystem.repository;

import com.eventmanagementsystem.EventManagementSystem.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {

    Booking findByEventIdAndUserId(String eventId, String userId);

    @Query("{eventId:?0,attendedStatus:'none'}")
    List<Booking> findBookersByEventId(String eventId);

    @Query("{userId:?0,attendedStatus:'none'}")
    List<Booking> findEventsByUserId(String userId);
}
