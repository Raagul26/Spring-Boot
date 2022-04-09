package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.model.Event;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface EventService {

    Map<String, String> createEvent(Event event);

    Event getEventByEventId(String id);

    String getEventIdByTitle(String title);

    List<Event> getAllEvents();

    List<Event> getActiveEvents();

    List<Event> getDeletedEvents();

    void deleteEvent(String id);

    void updateEvent(String eventId, Event event);

    List<String> getEventTitles();
}
