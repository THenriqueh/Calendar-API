package syncope.calendar.calendar_api.adapter.in.web;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String code,
        String message,
        String path,
        List<FieldErrorResponse> fieldErrors
) {}

record FieldErrorResponse(
        String field,
        String message
) {}