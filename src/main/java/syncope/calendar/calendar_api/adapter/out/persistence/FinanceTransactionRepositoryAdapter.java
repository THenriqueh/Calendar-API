package syncope.calendar.calendar_api.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import syncope.calendar.calendar_api.adapter.out.persistence.mapper.FinanceTransactionPersistenceMapper;
import syncope.calendar.calendar_api.adapter.out.persistence.repository.SpringDataFinanceTransactionRepository;
import syncope.calendar.calendar_api.application.dto.TransactionFilterCommand;
import syncope.calendar.calendar_api.application.port.out.FinanceTransactionRepositoryPort;
import syncope.calendar.calendar_api.domain.model.FinanceTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FinanceTransactionRepositoryAdapter implements FinanceTransactionRepositoryPort {

    private final SpringDataFinanceTransactionRepository jpaRepository;
    private final FinanceTransactionPersistenceMapper mapper;

    @Override
    public FinanceTransaction save(FinanceTransaction tx) {
        var entity = mapper.toEntity(tx);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<FinanceTransaction> saveAll(List<FinanceTransaction> list) {
        var entities = list.stream().map(mapper::toEntity).toList();
        var saved = jpaRepository.saveAll(entities);
        return saved.stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<FinanceTransaction> findByIdAndUserId(Long id, Long userId) {
        return jpaRepository.findByIdAndUserId(id, userId).map(mapper::toDomain);
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {
        jpaRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public List<FinanceTransaction> findByFilter(Long userId, TransactionFilterCommand command) {
        return List.of();
    }

    @Override
    public List<FinanceTransaction> findByFilter(
            Long userId,
            String status,
            LocalDate startDate,
            LocalDate endDate,
            List<Long> categoryIds,
            String search
    ) {
        // aqui você adapta pra usar métodos do Spring Data ou Specification
        var entities = jpaRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        // filtra status, categoria, search na mão ou com Specification
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<FinanceTransaction> findBySeriesGroupIdAndUserId(String seriesGroupId, Long userId) {
        return jpaRepository.findByUserIdAndSeriesGroupId(userId, seriesGroupId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}