package syncope.calendar.calendar_api.application.port.in;

import syncope.calendar.calendar_api.application.dto.TransactionRequest;
import syncope.calendar.calendar_api.application.dto.TransactionResponse;

public interface CreateRecurringTransactionUseCase {
    // pode reutilizar TransactionRequest ou criar uma própria
    TransactionResponse createSeries(Long userId, TransactionRequest request);
}
