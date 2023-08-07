package com.aea.authservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidatonErrorResponse {
    private String message;
    private Map<String, String> invalidFields;
}
