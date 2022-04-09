package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.model.Booking;
import com.eventmanagementsystem.EventManagementSystem.model.Event;
import com.eventmanagementsystem.EventManagementSystem.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {

    void bookEvent(Booking booking);

    List<User> getBookersByEventId(String eventId);

    List<User> getBookersByEventTitle(String title);

    List<Event> getEventsByUserId(String userId);

    void cancelEventBooking(Booking booking);
}
