package com.eventmanagementsystem.EventManagementSystem.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document("admin")
public class Admin {

    private String id;

    private String userName;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "must be a valid email id")
    @NotEmpty
    private String emailId;

    @NotEmpty
    @Size(min = 5)
    private String password;

}
