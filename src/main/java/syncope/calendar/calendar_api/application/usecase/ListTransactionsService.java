package syncope.calendar.calendar_api.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import syncope.calendar.calendar_api.application.dto.TransactionFilterCommand;
import syncope.calendar.calendar_api.application.dto.TransactionResponse;
import syncope.calendar.calendar_api.application.mapper.FinanceTransactionMapper;
import syncope.calendar.calendar_api.application.port.in.ListTransactionsUseCase;
import syncope.calendar.calendar_api.application.port.out.FinanceTransactionRepositoryPort;
import syncope.calendar.calendar_api.domain.model.FinanceTransaction;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListTransactionsService implements ListTransactionsUseCase {

    private final FinanceTransactionRepositoryPort transactionRepository;
    private final FinanceTransactionMapper mapper;

    @Override
    public List<TransactionResponse> list(Long userId, TransactionFilterCommand command) {

        // se não vier data, usamos um range amplo (ou você pode defaultar pro mês atual)
        TransactionFilterCommand effectiveCommand = normalizeDates(command);

        List<FinanceTransaction> transactions =
                transactionRepository.findByFilter(userId, effectiveCommand);

        return transactions
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    private TransactionFilterCommand normalizeDates(TransactionFilterCommand cmd) {
        LocalDate start = cmd.startDate();
        LocalDate end = cmd.endDate();

        if (start != null && end != null && end.isBefore(start)) {
            // troca se vier invertido
            LocalDate tmp = start;
            start = end;
            end = tmp;
        }

        // se quiser defaultar pro ano todo quando estiver null, você pode mudar aqui
        return new TransactionFilterCommand(
                cmd.status(),
                start,
                end,
                cmd.categoryIds(),
                cmd.search()
        );
    }
}