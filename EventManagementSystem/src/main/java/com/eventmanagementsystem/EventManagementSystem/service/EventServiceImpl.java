package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.exception.EventIdNotFoundException;
import com.eventmanagementsystem.EventManagementSystem.exception.EventTitleNotFoundException;
import com.eventmanagementsystem.EventManagementSystem.exception.TitleAlreadyExistsException;
import com.eventmanagementsystem.EventManagementSystem.model.Event;
import com.eventmanagementsystem.EventManagementSystem.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    public EventRepository eventRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Map<String, String> createEvent(Event event) {
        if (eventRepository.findByTitleAndStatus(event.getTitle(),"active") != null ) {
            throw new TitleAlreadyExistsException("Title already exists");
        } else {
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            int eventId = eventRepository.findAll().size() + 1;
            Event newEvent = new Event();
            newEvent.setEventId("EVT" + eventId);
            newEvent.setTitle(event.getTitle());
            newEvent.setVenue(event.getVenue());
            newEvent.setDate(event.getDate());
            newEvent.setAmount(event.getAmount());
            newEvent.setDescription(event.getDescription());
            newEvent.setCreatedBy("admin");
            newEvent.setCreatedOn(dateFormat.format(date));
            newEvent.setStatus("active");
            Event createdEvent = eventRepository.insert(newEvent);
            return Map.of("EventId", "createdEvent.getEventId()", "Title", "createdEvent.getTitle()");
        }
    }

    @Override
    public Event getEventByEventId(String eventId) {
        if (eventRepository.findByEventIdAndStatus(eventId,"active") != null) {
            return eventRepository.findByEventIdAndStatus(eventId,"active");
        } else {
            throw new EventIdNotFoundException(eventId + " - Event Id not found");
        }
    }

    @Override
    public String getEventIdByTitle(String title) {
        if (eventRepository.findByTitleAndStatus(title,"active") != null) {
            return eventRepository.findByTitleAndStatus(title,"active").getEventId();
        } else {
            throw new EventTitleNotFoundException(title+ " not found");
        }
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getActiveEvents() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return eventRepository.findByStatusAndDate("active",dateFormat.format(date));
    }

    @Override
    public List<Event> getDeletedEvents() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return eventRepository.findByStatusAndDate("deleted",dateFormat.format(date));
    }

    @Override
    public void deleteEvent(String eventId) {
        if (eventRepository.findByEventIdAndStatus(eventId,"active") != null) {
            Event deleteEvent = eventRepository.findByEventIdAndStatus(eventId,"active");
            deleteEvent.setStatus("deleted");
            eventRepository.save(deleteEvent);
        } else {
            throw new EventIdNotFoundException(eventId + " - Event Id not found");
        }
    }

    @Override
    public void updateEvent(String eventId, Event event) {
        Event tempEvent = eventRepository.findByEventIdAndStatus(eventId,"active");

        if (tempEvent == null) {
            throw new EventIdNotFoundException(eventId + " - Event Id not found");
        } else {
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            tempEvent.setTitle(event.getTitle());
            tempEvent.setDate(event.getDate());
            tempEvent.setVenue(event.getVenue());
            tempEvent.setAmount(event.getAmount());
            tempEvent.setDescription(event.getDescription());
            tempEvent.setLastUpdatedOn(dateFormat.format(date));
            eventRepository.save(tempEvent);
        }
    }

    @Override
    public List<String> getEventTitles() {
        Query query = new Query(Criteria.where("status").is("active"));
        return new ArrayList<>(mongoTemplate.findDistinct(query,"title", Event.class, String.class));
    }
}
