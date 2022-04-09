package com.eventmanagementsystem.EventManagementSystem.service;

import com.eventmanagementsystem.EventManagementSystem.exception.*;
import com.eventmanagementsystem.EventManagementSystem.model.Booking;
import com.eventmanagementsystem.EventManagementSystem.model.Event;
import com.eventmanagementsystem.EventManagementSystem.model.User;
import com.eventmanagementsystem.EventManagementSystem.repository.BookingRepository;
import com.eventmanagementsystem.EventManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            }else if(Objects.equals(booking.getAttendedStatus(), "cancelled") && bookingRepository.findByEventIdAndUserIdAndAttendedStatus(booking.getEventId(),booking.getUserId(), "none")!=null)
            {
                Booking updateBooking = bookingRepository.findByEventIdAndUserIdAndAttendedStatus(booking.getEventId(),booking.getUserId(), "none");
                updateBooking.setAttendedStatus("cancelled");
                bookingRepository.save(updateBooking);
            }
            else if(Objects.equals(booking.getAttendedStatus(), "none") && bookingRepository.findByEventIdAndUserIdAndAttendedStatus(booking.getEventId(),booking.getUserId(), "cancelled")!=null)
            {
                Booking updateBooking = bookingRepository.findByEventIdAndUserIdAndAttendedStatus(booking.getEventId(),booking.getUserId(), "cancelled");
                updateBooking.setAttendedStatus("none");
                bookingRepository.save(updateBooking);
            }
            else {
                throw new EventAlreadyBookedException("Event Already Booked!");
            }
        } else {
            throw new UserNotFoundException("User not found with userId "+booking.getUserId());
        }
    }

    @Override
    public List<User> getBookersByEventId(String eventId) {
        if (eventService.getEventByEventId(eventId) != null) {
            List<User> bookedUsers = new ArrayList<>();
            for (Booking b : bookingRepository.findByEventIdAndAttendedStatus(eventId,"none")) {
                bookedUsers.add(userRepository.findByUsersId(b.getUserId()));
            }
            return bookedUsers;
        } else {
            throw new EventIdNotFoundException(eventId+" - Event id not found");
        }
    }

    @Override
    public List<User> getBookersByEventTitle(String title) {
        if (eventService.getEventIdByTitle(title) != null) {
            List<User> bookedUsers = new ArrayList<>();
            for (Booking b : bookingRepository.findByEventIdAndAttendedStatus(eventService.getEventIdByTitle(title),"none")) {
                bookedUsers.add(userRepository.findByUsersId(b.getUserId()));
            }
            return bookedUsers;
        } else {
            throw new EventTitleNotFoundException(title+" not found");
        }
    }

    @Override
    public List<Event> getEventsByUserId(String userId) {
        List<Event> bookedEvents = new ArrayList<>();
        if (userRepository.findByUserId(userId) != null) {
            for (Booking b : bookingRepository.findByUserIdAndAttendedStatus(userId,"none")) {
                bookedEvents.add(eventService.getEventByEventId(b.getEventId()));
            }
            return bookedEvents;
        } else {
            throw new UserNotFoundException("User not found with userId "+userId);
        }
    }

    @Override
    public void cancelEventBooking(Booking booking) {
        if (bookingRepository.findByEventIdAndUserIdAndAttendedStatus(booking.getEventId(), booking.getUserId(), "none") != null) {
            Booking booking1 = bookingRepository.findByEventIdAndUserIdAndAttendedStatus(booking.getEventId(), booking.getUserId(), "none");
            booking1.setAttendedStatus("cancelled");
            bookingRepository.save(booking1);
        } else {
            throw new InvalidCredException("Invalid User Id or Event Id");
        }
    }
}
