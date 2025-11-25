package syncope.calendar.calendar_api.domain.exception;


public class BusinessRuleException extends DomainException {

    public BusinessRuleException(String code, String message) {
        super(code, message);
    }

    public static BusinessRuleException of(String message) {
        return new BusinessRuleException("BUSINESS_RULE_VIOLATION", message);
    }
}