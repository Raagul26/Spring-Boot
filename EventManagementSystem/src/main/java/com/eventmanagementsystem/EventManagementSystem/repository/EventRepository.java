package com.eventmanagementsystem.EventManagementSystem.repository;

import com.eventmanagementsystem.EventManagementSystem.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    @Query("{status:'active'}")
    List<Event> findActiveEvents();

    @Query(value = "{eventId:?0,status:'active'}")
    Event findByEventId(String eventId);

    List<Event> findByTitle(String title);

}
