package syncope.calendar.calendar_api.application.port.in;


import syncope.calendar.calendar_api.application.dto.TransactionResponse;

public interface TogglePaidUseCase {

    TransactionResponse toggle(Long userId, Long transactionId);
}
