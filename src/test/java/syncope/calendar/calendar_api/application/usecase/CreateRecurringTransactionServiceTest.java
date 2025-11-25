/*package syncope.calendar.calendar_api.application.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionRequest;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionResponse;
import syncope.calendar.calendar_api.application.mapper.FinanceTransactionMapper;
import syncope.calendar.calendar_api.application.port.out.FinanceTransactionRepositoryPort;
import syncope.calendar.calendar_api.application.port.out.IdGeneratorPort;
import syncope.calendar.calendar_api.domain.model.FinanceTransaction;
import syncope.calendar.calendar_api.domain.service.TransactionDomainService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRecurringTransactionServiceTest {

    @Mock
    FinanceTransactionRepositoryPort repo;

    @Mock
    IdGeneratorPort idGen;

    @Mock
    FinanceTransactionMapper mapper;

    TransactionDomainService domain;

    CreateRecurringTransactionService service;

    @BeforeEach
    void setUp() {
        domain = new TransactionDomainService();
        // ajusta a ordem dos parâmetros se o construtor for diferente
        service = new CreateRecurringTransactionService(repo, domain, idGen, mapper);
    }

    @Test
    void deveCriarSerieERetornarPrimeiraTransacao() {
        TransactionRequest req = new TransactionRequest(
                "2025-11-25",                // date
                "Aluguel",                   // description
                new BigDecimal("1500.00"),   // value
                1L,                          // categoryId
                false,                       // planned
                true,                        // recurring
                6,                           // recurringMonths
                25,                          // dayOfMonth
                false                        // ignoreDuplicateWarning
        );

        // nome do método conforme sua interface
        when(idGen.nextSeriesId()).thenReturn("series-xyz");

        // mapper: entidade -> response
        when(mapper.toResponse(any(FinanceTransaction.class)))
                .thenAnswer(invocation -> {
                    FinanceTransaction tx = invocation.getArgument(0);
                    return new TransactionResponse(
                            tx.getId(),
                            tx.getDate().toString(),
                            tx.getDescription(),
                            tx.getValue(),
                            tx.getCategoryId(),
                            tx.isPaid(),
                            tx.isPlanned(),
                            tx.isRecurring(),
                            tx.getRecurringInfo(),
                            tx.getSeriesGroupId()
                    );
                });

        // repo.saveAll(...) – sem doNothing, usando thenAnswer
        when(repo.saveAll(anyList()))
                .thenAnswer(invocation -> {
                    List<FinanceTransaction> list = invocation.getArgument(0);

                    assertThat(list).hasSize(6);
                    assertThat(list.get(0).getRecurringInfo()).isEqualTo("1/6");
                    assertThat(list.get(5).getRecurringInfo()).isEqualTo("6/6");
                    assertThat(list.get(0).getSeriesGroupId()).isEqualTo("series-xyz");

                    // se a assinatura retornar List<FinanceTransaction>, isso aqui é perfeito
                    return list;
                });

        // execução
        TransactionResponse response = service.createSeries(1L, req);

        // valida resposta
        assertThat(response.recurring()).isTrue();
        assertThat(response.recurringInfo()).isEqualTo("1/6");
        assertThat(response.seriesGroupId()).isEqualTo("series-xyz");
        assertThat(response.description()).isEqualTo("Aluguel");
        assertThat(response.value()).isEqualByComparingTo("1500.00");
    }
}

 */