package syncope.calendar.calendar_api.application.dto.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record TransactionFilterCommand(
        String status,
        LocalDate startDate,
        LocalDate endDate,
        List<Long> categoryIds,
        String search
) {

    public static TransactionFilterCommand fromRequest(TransactionFilterRequest request) {
        // status normalizado
        String status = (request.status() == null || request.status().isBlank())
                ? "all"
                : request.status().trim().toLowerCase();

        // datas
        LocalDate start = null;
        LocalDate end = null;
        if (request.startDate() != null && !request.startDate().isBlank()) {
            start = LocalDate.parse(request.startDate());
        }
        if (request.endDate() != null && !request.endDate().isBlank()) {
            end = LocalDate.parse(request.endDate());
        }

        // categorias -> List<Long>
        List<Long> categoryIds = null;
        if (request.categories() != null && !request.categories().isBlank()) {
            categoryIds = Stream.of(request.categories().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }

        String search = request.searchTerm() == null ? null : request.searchTerm().trim();

        return new TransactionFilterCommand(status, start, end, categoryIds, search);
    }
}
