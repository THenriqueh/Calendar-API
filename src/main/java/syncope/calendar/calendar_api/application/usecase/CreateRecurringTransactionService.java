package syncope.calendar.calendar_api.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import syncope.calendar.calendar_api.application.dto.TransactionRequest;
import syncope.calendar.calendar_api.application.dto.TransactionResponse;
import syncope.calendar.calendar_api.application.mapper.FinanceTransactionMapper;
import syncope.calendar.calendar_api.application.port.in.CreateRecurringTransactionUseCase;
import syncope.calendar.calendar_api.application.port.out.FinanceTransactionRepositoryPort;
import syncope.calendar.calendar_api.application.port.out.IdGeneratorPort;
import syncope.calendar.calendar_api.domain.model.FinanceTransaction;
import syncope.calendar.calendar_api.domain.model.RecurringSeries;
import syncope.calendar.calendar_api.domain.service.TransactionDomainService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateRecurringTransactionService implements CreateRecurringTransactionUseCase {

    private final FinanceTransactionRepositoryPort transactionRepository;
    private final FinanceTransactionMapper mapper;
    private final IdGeneratorPort idGenerator;

    private final TransactionDomainService domainService = new TransactionDomainService();

    @Override
    public TransactionResponse createSeries(Long userId, TransactionRequest request) {
        LocalDate startDate = LocalDate.parse(request.date());
        int totalMonths = request.recurringMonths();
        int dayOfMonth = request.dayOfMonth();

        String seriesId = idGenerator.nextSeriesId();

        RecurringSeries series = domainService.createSeries(
                seriesId,
                userId,
                startDate,
                totalMonths,
                dayOfMonth
        );

        List<FinanceTransaction> list = domainService.generateRecurringTransactions(
                series,
                request.description(),
                request.value(),
                request.categoryId(),
                Boolean.TRUE.equals(request.ignoreDuplicateWarning())
        );

        List<FinanceTransaction> saved = transactionRepository.saveAll(list);

        // pode retornar só a primeira, ou uma lista; aqui retorno primeira só de exemplo
        return mapper.toResponse(saved.get(0));
    }
}
