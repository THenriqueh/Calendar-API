package syncope.calendar.calendar_api.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import syncope.calendar.calendar_api.application.dto.TransactionResponse;
import syncope.calendar.calendar_api.application.mapper.FinanceTransactionMapper;
import syncope.calendar.calendar_api.application.port.in.TogglePaidUseCase;
import syncope.calendar.calendar_api.application.port.out.FinanceTransactionRepositoryPort;
import syncope.calendar.calendar_api.domain.exception.BusinessException;
import syncope.calendar.calendar_api.domain.model.FinanceTransaction;

@Service
@RequiredArgsConstructor
public class TogglePaidService implements TogglePaidUseCase {

    private final FinanceTransactionRepositoryPort transactionRepository;
    private final FinanceTransactionMapper mapper;

    @Override
    public TransactionResponse toggle(Long userId, Long transactionId) {
        FinanceTransaction tx = transactionRepository
                .findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new BusinessException("Lançamento não encontrado"));

        tx.togglePaid();

        FinanceTransaction saved = transactionRepository.save(tx);

        return mapper.toResponse(saved);
    }
}