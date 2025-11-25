package syncope.calendar.calendar_api.domain.exception;

public class TransactionNotFoundException extends NotFoundException {

    public TransactionNotFoundException(Long id) {
        super("Transação", id);
    }
}