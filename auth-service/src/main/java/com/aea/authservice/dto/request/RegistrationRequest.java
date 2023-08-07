package com.aea.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {
    @NotEmpty(message = "The username is required.")
    @Size(min = 3, max = 16, message = "The length of username must be between 2 and 100.")
    private String username;

    @Email
    @NotEmpty(message = "Email is required.")
    private String email;

    @NotEmpty(message = "The password is required.")
    @Size(min = 6, max = 16, message = "The length of password must be between 6 and 100.")
    private String password;
    private String passwordConfirm;
}
