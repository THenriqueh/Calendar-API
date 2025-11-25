package syncope.calendar.calendar_api.domain.exception;


public class NotFoundException extends DomainException {

    public NotFoundException(String resourceName, Object id) {
        super(
                resourceName.toUpperCase() + "_NOT_FOUND",
                resourceName + " não encontrado(a) para id=" + id
        );
    }
}