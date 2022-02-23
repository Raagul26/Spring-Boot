package com.eventmanagementsystem.EventManagementSystem.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private  String id;

    private String eventId;

    @NotBlank
    @Size(min = 10, max = 25, message = "must contain min 10 characters")
    private String title;

    @NotBlank
    @Size(min = 3, max = 25, message = "must contain min 3 characters")
    private String venue;

    @NotBlank
    @Pattern(regexp = "^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$" ,message = "must be a valid date (DD-MM-YYYY)")
    private String date;

    @Min(100)
    private double amount;

    @NotBlank // only for text
    @Size(min = 20, max = 50, message = "must contain min 20 characters")
    private String description;

    private String createdOn;

    private String createdBy;

    private String lastUpdatedOn;

    private String status;
}
