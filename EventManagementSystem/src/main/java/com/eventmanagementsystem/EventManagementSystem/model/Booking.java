package com.eventmanagementsystem.EventManagementSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("bookings")
public class Booking {

    private String id;

    @Field("event_id")
    private String eventId;

    @Field("user_id")
    private String userId;

    @Field("attended_status")
    private String attendedStatus;

}
