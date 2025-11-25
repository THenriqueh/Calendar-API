package syncope.calendar.calendar_api.application.port.in;

import syncope.calendar.calendar_api.application.dto.transaction.TransactionResponse;

public interface TogglePlannedUseCase {

    TransactionResponse toggle(Long userId, Long transactionId);
}
