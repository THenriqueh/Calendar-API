package syncope.calendar.calendar_api.application.dto.transaction;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateRequest(

        @NotBlank
        String name

) {}