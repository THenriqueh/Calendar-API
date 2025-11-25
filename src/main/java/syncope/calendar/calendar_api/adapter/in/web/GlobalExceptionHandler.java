package syncope.calendar.calendar_api.adapter.in.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import syncope.calendar.calendar_api.domain.exception.BusinessRuleException;
import syncope.calendar.calendar_api.domain.exception.NotFoundException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ===== Exceptions de domínio =====

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            NotFoundException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                ex.getCode(),
                ex.getMessage(),
                request,
                null
        );
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(
            BusinessRuleException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Regra de negócio violada",
                ex.getCode(),
                ex.getMessage(),
                request,
                null
        );
    }

    // ===== Validações (Bean Validation) =====

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<FieldErrorResponse> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .toList();

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "VALIDATION_ERROR",
                "Um ou mais campos estão inválidos",
                request,
                fieldErrors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        List<FieldErrorResponse> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(v -> new FieldErrorResponse(
                        v.getPropertyPath().toString(),
                        v.getMessage()
                ))
                .toList();

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "VALIDATION_ERROR",
                "Um ou mais parâmetros de requisição estão inválidos",
                request,
                fieldErrors
        );
    }

    // ===== Genérico (último fallback) =====

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        // Nesse ponto você pode logar ex.printStackTrace() ou logger.error(ex)
        ex.printStackTrace();

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                "INTERNAL_ERROR",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                request,
                null
        );
    }

    // ===== Helpers =====

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String error,
            String code,
            String message,
            HttpServletRequest request,
            List<FieldErrorResponse> fieldErrors
    ) {
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                status.value(),
                error,
                code,
                message,
                request.getRequestURI(),
                fieldErrors
        );
        return ResponseEntity.status(status).body(body);
    }

    private FieldErrorResponse mapFieldError(FieldError fieldError) {
        String field = fieldError.getField();
        String message = fieldError.getDefaultMessage();
        return new FieldErrorResponse(field, message);
    }
}