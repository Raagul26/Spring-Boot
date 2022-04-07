package com.eventmanagementsystem.EventManagementSystem.repository;

import com.eventmanagementsystem.EventManagementSystem.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    @Query("{date:{$gte:?1}}")
    List<Event> findByStatusAndDate(String status,String date);

    Event findByEventIdAndStatus(String eventId, String status);

    Event findByTitleAndStatus(String title, String status);

}
