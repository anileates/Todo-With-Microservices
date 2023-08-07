package com.aea.authservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PasswordResetRequest {
    @NotEmpty(message = "The token is required.")
    private String token;

    @NotEmpty(message = "The password is required.")
    @Size(min = 6, max = 16, message = "The length of password must be between 6 and 16.")
    private String password;

    @NotEmpty(message = "The password is required.")
    @Size(min = 6, max = 16, message = "The length of password must be between 6 and 16.")
    private String passwordConfirm;
}
