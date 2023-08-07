package com.aea.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

@Component
public class RouteValidator {

        public static final List<String> openApiEndpoints = List.of(
                        "/auth",
                        "/eureka");

        public Predicate<ServerHttpRequest> isAuthRoute = (request) -> {
                String path = request.getURI().getPath();
                boolean isAuthRoute = openApiEndpoints.stream().anyMatch(path::startsWith);
                return isAuthRoute;
        };

        public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints
                        .stream()
                        .noneMatch(uri -> request.getURI().getPath().contains(uri));
}