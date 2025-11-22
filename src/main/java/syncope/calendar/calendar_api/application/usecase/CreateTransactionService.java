package syncope.calendar.calendar_api.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import syncope.calendar.calendar_api.application.dto.TransactionRequest;
import syncope.calendar.calendar_api.application.dto.TransactionResponse;
import syncope.calendar.calendar_api.application.mapper.FinanceTransactionMapper;
import syncope.calendar.calendar_api.application.port.in.CreateTransactionUseCase;
import syncope.calendar.calendar_api.application.port.out.FinanceTransactionRepositoryPort;
import syncope.calendar.calendar_api.domain.model.FinanceTransaction;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateTransactionService implements CreateTransactionUseCase {

    private final FinanceTransactionRepositoryPort transactionRepository;
    private final FinanceTransactionMapper mapper;

    @Override
    public TransactionResponse create(Long userId, TransactionRequest request) {
        LocalDate date = LocalDate.parse(request.date());

        FinanceTransaction tx = FinanceTransaction.builder()
                .userId(userId)
                .date(date)
                .description(request.description())
                .value(request.value())
                .categoryId(request.categoryId())
                .paid(false)
                .planned(Boolean.TRUE.equals(request.planned()))
                .recurring(false)
                .ignoreDuplicateWarning(Boolean.TRUE.equals(request.ignoreDuplicateWarning()))
                .build();

        FinanceTransaction saved = transactionRepository.save(tx);

        return mapper.toResponse(saved);
    }
}