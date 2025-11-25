package syncope.calendar.calendar_api.application.dto.categories;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "Nome da categoria é obrigatório")
        String name
) {}