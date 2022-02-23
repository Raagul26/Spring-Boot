package com.eventmanagementsystem.EventManagementSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("bookings")
public class Booking {

    private String id;

    private String eventId;

    private String userId;

    private String attendedStatus;

}
