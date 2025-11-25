package syncope.calendar.calendar_api.application.port.in;

import syncope.calendar.calendar_api.application.dto.transaction.TransactionFilterCommand;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionRequest;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionResponse;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionUpdateRequest;

import java.util.List;

// application/port/in/TransactionUseCase.java
public interface TransactionUseCase {

    TransactionResponse create(Long userId, TransactionRequest request);

    TransactionResponse createRecurring(Long userId, TransactionRequest request);

    List<TransactionResponse> list(Long userId, TransactionFilterCommand filter);

    TransactionResponse togglePaid(Long userId, Long id);

    TransactionResponse togglePlanned(Long userId, Long id);

    TransactionResponse update(Long userId, Long id, TransactionUpdateRequest request);

    void delete(Long userId, Long id);
}

