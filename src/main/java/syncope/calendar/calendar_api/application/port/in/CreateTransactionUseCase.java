package syncope.calendar.calendar_api.application.port.in;

import syncope.calendar.calendar_api.application.dto.TransactionRequest;
import syncope.calendar.calendar_api.application.dto.TransactionResponse;

public interface CreateTransactionUseCase {
    TransactionResponse create(Long userId, TransactionRequest request);
}
