package com.eventmanagementsystem.EventManagementSystem.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document("users")
public class User {

    private String id;

    @Field("user_id")
    private String userId;

    @NotEmpty(message = "Enter first name")
    @Size(min = 3, max = 15, message = "first name should contain min 3 letters")
    @Field("first_name")
    private String firstName;

    @NotEmpty(message = "Enter last name")
    @Size(min = 1, max = 15, message = "last name should contain min 1 letters")
    @Field("last_name")
    private String lastName;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "must be a valid email id")
    @NotEmpty
    @Field("email_id")
    private String emailId;

    @NotEmpty(message = "Enter valid password")
    @Size(min = 5, message = "should contain min 5 characters")
    private String password;

    @NotEmpty(message = "Enter contact no")
    @Size(min = 10, max = 10, message = "must contain 10 numbers")
    @Field("contact_no")
    private String contactNo;

    @Field("created_on")
    private String createdOn;

    private String status;

}
