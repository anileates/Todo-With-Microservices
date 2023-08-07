package com.aea.authservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty(message = "The username is required.")
    private String username;

    @NotEmpty(message = "The password is required.")
    private String password;
}
