package syncope.calendar.calendar_api.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionFilterCommand;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionRequest;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionResponse;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionUpdateRequest;
import syncope.calendar.calendar_api.application.mapper.FinanceTransactionMapper;
import syncope.calendar.calendar_api.application.port.in.TransactionUseCase;
import syncope.calendar.calendar_api.application.port.out.FinanceTransactionRepositoryPort;
import syncope.calendar.calendar_api.application.port.out.IdGeneratorPort;
import syncope.calendar.calendar_api.domain.model.FinanceTransaction;
import syncope.calendar.calendar_api.domain.model.RecurringSeries;
import syncope.calendar.calendar_api.domain.service.TransactionDomainService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService implements  TransactionUseCase {

    private final FinanceTransactionRepositoryPort transactionRepository;
    private final TransactionDomainService transactionDomainService;
    private final FinanceTransactionMapper mapper;
    private final IdGeneratorPort idGenerator;

    @Override
    public TransactionResponse create(Long userId, TransactionRequest request) {
        LocalDate date = LocalDate.parse(request.date());

        boolean planned = Boolean.TRUE.equals(request.planned());
        boolean recurring = Boolean.TRUE.equals(request.recurring());
        boolean ignoreDup = Boolean.TRUE.equals(request.ignoreDuplicateWarning());

        FinanceTransaction tx = FinanceTransaction.builder()
                .userId(userId)
                .date(date)
                .description(request.description())
                .value(request.value())
                .categoryId(request.categoryId())
                .paid(false)
                .planned(planned)
                .recurring(recurring)
                .recurringInfo(null)
                .seriesGroupId(null)
                .ignoreDuplicateWarning(ignoreDup)
                .build();

        FinanceTransaction saved = transactionRepository.save(tx);
        return mapper.toResponse(saved);
    }

    @Override
    public TransactionResponse createRecurring(Long userId, TransactionRequest request) {

        LocalDate startDate = LocalDate.parse(request.date());

        Integer totalMonths = Optional.ofNullable(request.recurringMonths()).orElseThrow(() -> new IllegalArgumentException("recurringMonths é obrigatório para recorrência"));

        Integer dayOfMonth = Optional.ofNullable(request.dayOfMonth()).orElse(startDate.getDayOfMonth());

        boolean ignoreDup = Boolean.TRUE.equals(request.ignoreDuplicateWarning());

        // cria "série" no domínio
        String seriesId = idGenerator.nextSeriesId();
        RecurringSeries series = transactionDomainService.createSeries(seriesId, userId, startDate, totalMonths, dayOfMonth);

        // gera as transações recorrentes no domínio
        List<FinanceTransaction> generated = transactionDomainService.generateRecurringTransactions(series, request.description(), request.value(), request.categoryId(), ignoreDup);

        // persiste todas
        List<FinanceTransaction> savedList = transactionRepository.saveAll(generated);

        // devolve a primeira transação da série como resposta (mesmo comportamento de antes)
        FinanceTransaction first = savedList.get(0);
        return mapper.toResponse(first);
    }


    @Override
    public List<TransactionResponse> list(Long userId, TransactionFilterCommand filter) {
        List<FinanceTransaction> list = transactionRepository.findByFilter(userId, filter);
        return list.stream().map(mapper::toResponse).collect(Collectors.toList());
    }


    @Override
    public TransactionResponse togglePaid(Long userId, Long id) {
        FinanceTransaction tx = getByUserAndIdOrThrow(userId, id);
        tx.setPaid(!tx.isPaid());
        // se pagou, normalmente deixa de ser planejado
        if (tx.isPaid()) {
            tx.setPlanned(false);
        }
        FinanceTransaction saved = transactionRepository.save(tx);
        return mapper.toResponse(saved);
    }

    @Override
    public TransactionResponse togglePlanned(Long userId, Long id) {
        FinanceTransaction tx = getByUserAndIdOrThrow(userId, id);
        tx.setPlanned(!tx.isPlanned());
        // se marcou como planejado, não mexe no paid; se isso for regra, você pode ajustar
        FinanceTransaction saved = transactionRepository.save(tx);
        return mapper.toResponse(saved);
    }

    @Override
    public TransactionResponse update(Long userId, Long id, TransactionUpdateRequest request) {
        FinanceTransaction tx = getByUserAndIdOrThrow(userId, id);

        // só atualiza o que veio
        if (request.date() != null) {
            tx.setDate(LocalDate.parse(request.date()));
        }
        if (request.description() != null) {
            tx.setDescription(request.description());
        }
        if (request.value() != null) {
            tx.setValue(request.value());
        }
        if (request.categoryId() != null) {
            tx.setCategoryId(request.categoryId());
        }
        if (request.planned() != null) {
            tx.setPlanned(request.planned());
        }
        if (request.ignoreDuplicateWarning() != null) {
            tx.setIgnoreDuplicateWarning(request.ignoreDuplicateWarning());
        }

        FinanceTransaction saved = transactionRepository.save(tx);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Long userId, Long id) {
        FinanceTransaction tx = getByUserAndIdOrThrow(userId, id);
        transactionRepository.delete(tx);
    }

    private FinanceTransaction getByUserAndIdOrThrow(Long userId, Long id) {
        return transactionRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new RuntimeException("Transação não encontrada para este usuário."));
    }

}