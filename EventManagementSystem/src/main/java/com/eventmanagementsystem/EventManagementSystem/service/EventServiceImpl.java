package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.model.Event;
import com.eventmanagementsystem.EventManagementSystem.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventServiceImpl implements EventService{

    @Autowired
    public EventRepository eventRepository;

    @Override
    public Event createEvent(Event event)
    {
        Event newEvent = new Event();
        if(eventRepository.findByTitle(event.getTitle()).size()>0)
        {
            throw new RuntimeException("Title already exists");
        }
        else {
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            int eventId = eventRepository.findAll().size() + 1;
            newEvent.setEventId("EVT"+eventId);
            newEvent.setTitle(event.getTitle());
            newEvent.setVenue(event.getVenue());
            newEvent.setDate(event.getDate());
            newEvent.setAmount(event.getAmount());
            newEvent.setDescription(event.getDescription());
            newEvent.setCreatedBy("admin");
            newEvent.setCreatedOn(dateFormat.format(date));
            newEvent.setStatus("active");
            return eventRepository.insert(newEvent);
        }
    }

    @Override
    public Event getEventByEventId(String eventId) {
        if(eventRepository.findByEventId(eventId)!=null)
        {
            return eventRepository.findByEventId(eventId);
        }
        else {
            throw new RuntimeException("Event Id not found");
        }
    }

    @Override
    public List<Event> getAllEvents()
    {
        if(eventRepository.findAll().isEmpty())
        {
            throw new RuntimeException("No Events Found");
        }
        else
        {
            return eventRepository.findAll();
        }
    }

    @Override
    public List<Event> getActiveEvents()
    {
        try
        {
            return eventRepository.findActiveEvents();
        }
        catch(Exception e)
        {
            throw new RuntimeException("No Active Events Found");
        }
    }

    @Override
    public void deleteEvent(String eventId) {
        if(eventRepository.findByEventId(eventId)!=null)
        {
            Event updatedEvent = eventRepository.findByEventId(eventId);
            updatedEvent.setStatus("deleted");
            eventRepository.save(updatedEvent);
        }
        else
        {
            throw new RuntimeException("Event Id not found");
        }
    }

    @Override
    public void updateEvent(String eventId, Event event) {
        Event tempEvent = eventRepository.findByEventId(eventId);

        if(tempEvent==null)
        {
            throw new RuntimeException("Event Id not found");
        }
        else {
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
