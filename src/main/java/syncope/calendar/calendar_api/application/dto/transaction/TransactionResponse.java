package syncope.calendar.calendar_api.application.dto.transaction;

import java.math.BigDecimal;

public record TransactionResponse (
        Long id,
        String date,
        String description,
        BigDecimal value,
        Long categoryId,
        boolean paid,
        boolean planned,
        boolean recurring,
        String recurringInfo,
        String seriesGroupId
) {}

