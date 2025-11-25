package syncope.calendar.calendar_api.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import syncope.calendar.calendar_api.adapter.out.persistence.entity.FinanceTransactionEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SpringDataFinanceTransactionRepository
        extends JpaRepository<FinanceTransactionEntity, Long> {

    Optional<FinanceTransactionEntity> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    List<FinanceTransactionEntity> findByUserIdOrderByDateDesc(Long userId);

    List<FinanceTransactionEntity> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);

    List<FinanceTransactionEntity> findByUserIdAndSeriesGroupId(Long userId, String seriesGroupId);

    // você pode criar queries extras pra status, etc.
}