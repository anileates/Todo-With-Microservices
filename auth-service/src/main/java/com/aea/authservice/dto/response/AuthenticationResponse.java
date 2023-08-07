package com.aea.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private Long userId;
    private String message;
    @JsonProperty("access_token")
    private String accessToken;
}
