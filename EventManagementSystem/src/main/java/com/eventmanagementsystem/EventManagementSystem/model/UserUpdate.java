package com.eventmanagementsystem.EventManagementSystem.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdate {

    @Size(min = 3, max = 15, message = "first name should contain min 3 letters")
    private String firstName;

    @Size(min = 1, max = 15, message = "last name should contain min 1 letters")
    private String lastName;

    @Size(min = 10, max = 10, message = "must contain 10 numbers")
    private String contactNo;

}
