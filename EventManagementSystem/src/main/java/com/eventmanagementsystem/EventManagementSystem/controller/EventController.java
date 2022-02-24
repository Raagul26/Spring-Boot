package com.eventmanagementsystem.EventManagementSystem.controller;

import com.eventmanagementsystem.EventManagementSystem.controller.response.ApiResponse;
import com.eventmanagementsystem.EventManagementSystem.model.Event;
import com.eventmanagementsystem.EventManagementSystem.repository.AdminRepository;
import com.eventmanagementsystem.EventManagementSystem.service.EventService;
import com.eventmanagementsystem.EventManagementSystem.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    public EventService eventService;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AdminRepository adminRepository;

    // create event
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createEvent(@Valid @RequestBody Event event, @RequestHeader(value = "authorization", defaultValue = "") String auth)
    {
        ApiResponse response = new ApiResponse();
        try{
            if(adminRepository.findByEmailId(jwtUtility.getUsernameFromToken(auth))==null)
            {
                response.setStatus("Access denied");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            jwtUtility.validateToken(auth);
            Event newEvent = eventService.createEvent(event);
            response.setData(Map.of("EventId",newEvent.getEventId(),"Title", newEvent.getTitle()));
            response.setStatus("Success");
            response.setMessage("Event created successfully");
            return new ResponseEntity<>(response,HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage("400");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    // get all events
    @GetMapping("/all")
    public ResponseEntity<Object> getEvents(@RequestHeader(value = "authorization", defaultValue = "") String auth)
    {
        ApiResponse response = new ApiResponse();

        try{
            if(adminRepository.findByEmailId(jwtUtility.getUsernameFromToken(auth))==null)
            {
                response.setStatus("Access denied");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            jwtUtility.validateToken(auth);
            response.setStatus("Success");
            response.setMessage("Fetched successfully");
            response.setData(eventService.getAllEvents());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    // get active events
    @GetMapping("/active")
    public ResponseEntity<Object> getActiveEvents()
    {
        ApiResponse response = new ApiResponse();

        try{
            response.setStatus("Success");
            response.setMessage("Fetched successfully");
            response.setData(eventService.getActiveEvents());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    // get event by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getEventById(@PathVariable String id, @RequestHeader(value = "authorization", defaultValue = "") String auth)
    {
        ApiResponse response = new ApiResponse();
        try{
            jwtUtility.validateToken(auth);
            response.setStatus("Success");
            response.setMessage("Fetched successfully");
            response.setData(eventService.getEventByEventId(id));
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    // delete event
    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable String eventId, @RequestHeader(value = "authorization", defaultValue = "") String auth)
    {
        ApiResponse response = new ApiResponse();
        try{
            if(adminRepository.findByEmailId(jwtUtility.getUsernameFromToken(auth))==null)
            {
                response.setStatus("Access denied");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            jwtUtility.validateToken(auth);
            eventService.deleteEvent(eventId);
            response.setStatus("Success");
            response.setMessage("Deleted successfully");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    // update event
    @PutMapping("/update/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable String eventId, @Valid @RequestBody Event event, @RequestHeader(value = "authorization", defaultValue = "") String auth)
    {
        ApiResponse response =new ApiResponse();
        try {
            if(adminRepository.findByEmailId(jwtUtility.getUsernameFromToken(auth))==null)
            {
                response.setStatus("Access denied");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            jwtUtility.validateToken(auth);
            eventService.updateEvent(eventId, event);
            response.setStatus("Success");
            response.setMessage("Updated Successfully");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e)
        {
            response.setStatus("Error");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

}
