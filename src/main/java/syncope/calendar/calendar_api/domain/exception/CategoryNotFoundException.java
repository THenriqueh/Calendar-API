package syncope.calendar.calendar_api.domain.exception;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException(Long id) {
        super("Categoria", id);
    }
}