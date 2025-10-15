package co.edu.unbosque.microservice_system_management.exception;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {}
