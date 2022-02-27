package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.exception.EventAlreadyBookedException;
import com.eventmanagementsystem.EventManagementSystem.exception.EventIdNotFoundException;
import com.eventmanagementsystem.EventManagementSystem.exception.InvalidCredException;
import com.eventmanagementsystem.EventManagementSystem.exception.UserNotFoundException;
import com.eventmanagementsystem.EventManagementSystem.model.Booking;
import com.eventmanagementsystem.EventManagementSystem.model.Event;
import com.eventmanagementsystem.EventManagementSystem.model.User;
import com.eventmanagementsystem.EventManagementSystem.repository.BookingRepository;
import com.eventmanagementsystem.EventManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    public BookingRepository bookingRepository;

    @Autowired
    public EventService eventService;

    @Autowired
    public UserRepository userRepository;

    @Override
    public void bookEvent(Booking booking) {

        if (userRepository.findByUserId(booking.getUserId()) != null) {
            if (bookingRepository.findByEventIdAndUserId(booking.getEventId(), booking.getUserId()) == null && eventService.getEventByEventId(booking.getEventId()) != null) {
                Booking newBooking = new Booking();
                newBooking.setEventId(booking.getEventId());
                newBooking.setUserId(booking.getUserId());
                newBooking.setAttendedStatus("none");
                bookingRepository.insert(newBooking);
            } else {
                throw new EventAlreadyBookedException(booking.getUserId()+" already booked "+booking.getEventId());
            }
        } else {
            throw new UserNotFoundException("User not found with userId "+booking.getUserId());
        }
    }

    @Override
    public List<User> getBookersByEventId(String eventId) {
        if (eventService.getEventByEventId(eventId) != null) {
            List<User> bookedUsers = new ArrayList<>();
            for (Booking b : bookingRepository.findBookersByEventId(eventId)) {
                bookedUsers.add(userRepository.findByUsersId(b.getUserId()));
            }
            return bookedUsers;
        } else {
            throw new EventIdNotFoundException(eventId+" - Event id not found");
        }
    }

    @Override
    public List<Event> getEventsByUserId(String userId) {
        List<Event> bookedEvents = new ArrayList<>();
        if (userRepository.findByUserId(userId) != null) {
            for (Booking b : bookingRepository.findEventsByUserId(userId)) {
                bookedEvents.add(eventService.getEventByEventId(b.getEventId()));
            }
            return bookedEvents;
        } else {
            throw new UserNotFoundException("User not found with userId "+userId);
        }
    }

    @Override
    public void cancelEventBooking(Booking booking) {
        if (bookingRepository.findByEventIdAndUserId(booking.getEventId(), booking.getUserId()) != null) {
            Booking booking1 = bookingRepository.findByEventIdAndUserId(booking.getEventId(), booking.getUserId());
            booking1.setAttendedStatus("cancelled");
            bookingRepository.save(booking1);
        } else {
            throw new InvalidCredException("Invalid User Id or Event Id");
        }
    }
}
