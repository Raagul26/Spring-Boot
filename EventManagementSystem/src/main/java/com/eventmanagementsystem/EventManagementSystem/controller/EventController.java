package com.eventmanagementsystem.EventManagementSystem.controller;

import com.eventmanagementsystem.EventManagementSystem.controller.response.ApiResponse;
import com.eventmanagementsystem.EventManagementSystem.enums.Status;
import com.eventmanagementsystem.EventManagementSystem.model.Event;
import com.eventmanagementsystem.EventManagementSystem.repository.BookingRepository;
import com.eventmanagementsystem.EventManagementSystem.repository.EventRepository;
import com.eventmanagementsystem.EventManagementSystem.service.BookingService;
import com.eventmanagementsystem.EventManagementSystem.service.EventService;
import com.eventmanagementsystem.EventManagementSystem.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    // create event
    @PostMapping(value = "/create")
    public ResponseEntity<ApiResponse> createEvent(@Valid @RequestBody Event event, @RequestHeader(value = "authorization") String auth) {
        ApiResponse response = new ApiResponse();
        if (Objects.equals(jwtUtility.validateToken(auth), "admin")) {
            response.setData(eventService.createEvent(event));
            response.setStatus(Status.SUCCESS.name());
            response.setMessage("Event created successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            response.setStatus(Status.FAILED.name());
            response.setMessage("Access denied");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    // get all events
    @GetMapping("/all")
    public ResponseEntity<Object> getEvents(@RequestHeader(value = "authorization") String auth) {
        ApiResponse response = new ApiResponse();
        if (Objects.equals(jwtUtility.validateToken(auth), "admin")) {
            response.setStatus(Status.SUCCESS.name());
            response.setMessage("Fetched successfully");
            response.setData(eventService.getAllEvents());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setStatus(Status.FAILED.name());
            response.setMessage("Access denied");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    // get active events
    @GetMapping("/active")
    public ResponseEntity<Object> getActiveEvents() {
        ApiResponse response = new ApiResponse();
        response.setData(eventService.getActiveEvents());
        response.setStatus(Status.SUCCESS.name());
        response.setMessage("Fetched successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get event titles
    @GetMapping("/titles")
    public ResponseEntity<Object> getEventTitles() {
        ApiResponse response = new ApiResponse();
        response.setData(eventService.getEventTitles());
        response.setStatus(Status.SUCCESS.name());
        response.setMessage("Fetched successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get event by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getEventById(@PathVariable String id) {
        ApiResponse response = new ApiResponse();
        response.setData(eventService.getEventByEventId(id));
        response.setStatus(Status.SUCCESS.name());
        response.setMessage("Fetched successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // delete event
    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable String eventId, @RequestHeader(value = "authorization") String auth) {
        ApiResponse response = new ApiResponse();
        if (Objects.equals(jwtUtility.validateToken(auth), "admin")) {
            eventService.deleteEvent(eventId);
            response.setStatus(Status.SUCCESS.name());
            response.setMessage("Deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setStatus(Status.FAILED.name());
            response.setMessage("Access denied");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    // update event
    @PutMapping("/update/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable String eventId, @Valid @RequestBody Event event, @RequestHeader(value = "authorization") String auth) {
        ApiResponse response = new ApiResponse();
        if (Objects.equals(jwtUtility.validateToken(auth), "admin")) {
            eventService.updateEvent(eventId, event);
            response.setStatus(Status.SUCCESS.name());
            response.setMessage("Updated Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setStatus(Status.FAILED.name());
            response.setMessage("Unauthorized");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    // get deleted events
    @GetMapping("/deleted")
    public ResponseEntity<ApiResponse> deletedEvents(@RequestHeader(value = "authorization") String auth)
    {
        ApiResponse response = new ApiResponse();
        if (Objects.equals(jwtUtility.validateToken(auth), "admin")) {
            response.setData(eventService.getDeletedEvents());
            response.setStatus(Status.SUCCESS.name());
            response.setMessage("Updated Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setStatus(Status.FAILED.name());
            response.setMessage("Unauthorized");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    // get booked users
    @GetMapping("/bookedUsers/{title}")
    public ResponseEntity<ApiResponse> bookingsForEvent(@PathVariable String title, @RequestHeader(value = "authorization") String auth) {
        ApiResponse responseBody = new ApiResponse();
        if (Objects.equals(jwtUtility.validateToken(auth), "admin")) {
            responseBody.setStatus(Status.SUCCESS.name());
            responseBody.setMessage("Fetched Booked Users");
            responseBody.setData(bookingService.getBookersByEventTitle(title));
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.setStatus(Status.FAILED.name());
            responseBody.setMessage("Access denied");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
    }

    // total bookings
    @GetMapping("/booked")
    public ResponseEntity<ApiResponse> bookingsForEvent(@RequestHeader(value = "authorization") String auth) {
        ApiResponse responseBody = new ApiResponse();
        if (Objects.equals(jwtUtility.validateToken(auth), "admin")) {
            responseBody.setStatus(Status.SUCCESS.name());
            responseBody.setMessage("Fetched Booked Users");
            responseBody.setData(bookingRepository.findAll().size());
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.setStatus(Status.FAILED.name());
            responseBody.setMessage("Access denied");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
    }

}
