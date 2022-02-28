package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.exception.EventIdNotFoundException;
import com.eventmanagementsystem.EventManagementSystem.exception.TitleAlreadyExistsException;
import com.eventmanagementsystem.EventManagementSystem.model.Event;
import com.eventmanagementsystem.EventManagementSystem.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    public EventRepository eventRepository;

    @Override
    public Map<String, String> createEvent(Event event) {

        if (eventRepository.findByTitle(event.getTitle()).size() > 0) {
            throw new TitleAlreadyExistsException("Title already exists");
        } else {
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
            return Map.of("EventId", createdEvent.getEventId(), "Title", createdEvent.getTitle());
        }
    }

    @Override
    public Event getEventByEventId(String eventId) {
        if (eventRepository.findByEventId(eventId) != null) {
            return eventRepository.findByEventId(eventId);
        } else {
            throw new EventIdNotFoundException(eventId + " - Event Id not found");
        }
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getActiveEvents() {
        return eventRepository.findActiveEvents();
    }

    @Override
    public void deleteEvent(String eventId) {
        if (eventRepository.findByEventId(eventId) != null) {
            Event deleteEvent = eventRepository.findByEventId(eventId);
            deleteEvent.setStatus("deleted");
            eventRepository.save(deleteEvent);
        } else {
            throw new EventIdNotFoundException(eventId + " - Event Id not found");
        }
    }

    @Override
    public void updateEvent(String eventId, Event event) {
        Event tempEvent = eventRepository.findByEventId(eventId);

        if (tempEvent == null) {
            throw new EventIdNotFoundException(eventId + " - Event Id not found");
        } else {
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            tempEvent.setTitle(event.getTitle());
            tempEvent.setDate(event.getDate());
            tempEvent.setVenue(event.getVenue());
            tempEvent.setAmount(event.getAmount());
            tempEvent.setDescription(event.getDescription());
            tempEvent.setLastUpdatedOn(dateFormat.format(date));
            eventRepository.save(tempEvent);
        }
    }
}
