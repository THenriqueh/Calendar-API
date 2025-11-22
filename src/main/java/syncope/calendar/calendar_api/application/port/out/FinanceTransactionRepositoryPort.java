package syncope.calendar.calendar_api.application.port.out;

import syncope.calendar.calendar_api.application.dto.TransactionFilterCommand;
import syncope.calendar.calendar_api.domain.model.FinanceTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FinanceTransactionRepositoryPort {

    FinanceTransaction save(FinanceTransaction tx);

    List<FinanceTransaction> saveAll(List<FinanceTransaction> list);

    Optional<FinanceTransaction> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    List<FinanceTransaction> findByFilter(Long userId, TransactionFilterCommand command);

    List<FinanceTransaction> findByFilter(
            Long userId,
            String status,
            LocalDate startDate,
            LocalDate endDate,
            List<Long> categoryIds,
            String search
    );

    List<FinanceTransaction> findBySeriesGroupIdAndUserId(String seriesGroupId, Long userId);
}