package syncope.calendar.calendar_api.application.dto.transaction;

public record TransactionFilterRequest(
        String status,           // all, pending, paid, planned
        String startDate,        // yyyy-MM-dd
        String endDate,          // yyyy-MM-dd
        String categories,       // "1,2,3"
        String searchTerm                 // search term
) {}