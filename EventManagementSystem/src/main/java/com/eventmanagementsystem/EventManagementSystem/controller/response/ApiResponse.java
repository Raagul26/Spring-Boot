package com.eventmanagementsystem.EventManagementSystem.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

        private String status;

        private String errorCode;

        private String message;

        private String accessToken;

        private Object data;

}
