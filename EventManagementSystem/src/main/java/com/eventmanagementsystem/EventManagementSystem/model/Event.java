package com.eventmanagementsystem.EventManagementSystem.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document("events")
public class Event {

    private String id;

    @Field("event_id")
    private String eventId;

    @NotBlank
    @Size(min = 10, max = 25, message = "must contain min 10 characters")
    private String title;

    @NotBlank
    @Size(min = 3, max = 25, message = "must contain min 3 characters")
    private String venue;

    @NotBlank
    @Pattern(regexp = "^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$", message = "must be a valid date (YYYY-MM-DD)")
    private String date;

    @Min(100)
    private double amount;

    @NotBlank // only for text
    @Size(min = 15, max = 500, message = "must contain min 15 characters")
    private String description;

    @Field("created_on")
    private String createdOn;

    @Field("created_by")
    private String createdBy;

    @Field("last_updated_on")
    private String lastUpdatedOn;

    private String status;


}
