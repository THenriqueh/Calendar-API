package syncope.calendar.calendar_api.application.dto.transaction;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record TransactionUpdateRequest(

        // todos opcionais para PATCH

        String date,                     // "yyyy-MM-dd"

        String description,

        @DecimalMin("0.01")
        BigDecimal value,

        Long categoryId,

        Boolean planned,

        Boolean ignoreDuplicateWarning

        // se um dia você quiser permitir editar recorrência,
        // dá pra adicionar aqui: recurring, recurringMonths, dayOfMonth etc.
) {}