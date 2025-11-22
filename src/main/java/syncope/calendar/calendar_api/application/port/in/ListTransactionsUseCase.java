package syncope.calendar.calendar_api.application.port.in;

import syncope.calendar.calendar_api.application.dto.TransactionFilterCommand;
import syncope.calendar.calendar_api.application.dto.TransactionResponse;

import java.util.List;

public interface ListTransactionsUseCase {

    List<TransactionResponse> list(Long userId, TransactionFilterCommand command);
}