/*package syncope.calendar.calendar_api.adapter.in.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionFilterCommand;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionRequest;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionResponse;
import syncope.calendar.calendar_api.application.port.in.*;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureMockMvc(addFilters = false) // ignora filtros de segurança pra simplificar
class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    CreateTransactionUseCase createTransactionUseCase;


    CreateRecurringTransactionUseCase createRecurringTransactionUseCase;


    ListTransactionsUseCase listTransactionsUseCase;


    TogglePaidUseCase togglePaidUseCase;


    TogglePlannedUseCase togglePlannedUseCase;

    // --- Helpers -------------------------------------------------------------

    private TransactionRequest buildRequest(boolean recurring) {
        return new TransactionRequest(
                "2025-11-25",                // date
                "Aluguel",                   // description
                new BigDecimal("1500.00"),   // value
                1L,                          // categoryId
                false,                       // planned
                recurring,                   // recurring
                recurring ? 6 : null,        // recurringMonths
                25,                          // dayOfMonth
                false                        // ignoreDuplicateWarning
        );
    }

    private TransactionResponse buildResponse(Long id, boolean recurring, String recurringInfo) {
        return new TransactionResponse(
                id,
                "2025-11-25",
                "Aluguel",
                new BigDecimal("1500.00"),
                1L,
                false,
                false,
                recurring,
                recurringInfo,
                recurring ? "series-xyz" : null
        );
    }

    // --- Tests ---------------------------------------------------------------

    @Test
    @DisplayName("GET /api/transactions deve listar transações filtradas")
    void deveListarTransacoes() throws Exception {
        TransactionResponse tx1 = buildResponse(1L, false, null);
        TransactionResponse tx2 = buildResponse(2L, true, "1/6");

        Mockito.when(listTransactionsUseCase.list(
                        eq(1L), any(TransactionFilterCommand.class)))
                .thenReturn(List.of(tx1, tx2));

        mockMvc.perform(get("/api/transactions")
                        .param("startDate", "2025-11-01")
                        .param("endDate", "2025-11-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Aluguel"))
                .andExpect(jsonPath("$[1].recurring").value(true))
                .andExpect(jsonPath("$[1].recurringInfo").value("1/6"));
    }

    @Test
    @DisplayName("POST /api/transactions deve criar transação simples")
    void deveCriarTransacao() throws Exception {
        TransactionRequest request = buildRequest(false);
        TransactionResponse response = buildResponse(10L, false, null);

        Mockito.when(createTransactionUseCase.create(eq(1L), any(TransactionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/transactions")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.description").value("Aluguel"))
                .andExpect(jsonPath("$.recurring").value(false));
    }

    @Test
    @DisplayName("POST /api/transactions/recurring deve criar série recorrente e retornar primeira parcela")
    void deveCriarTransacaoRecorrente() throws Exception {
        TransactionRequest request = buildRequest(true);
        TransactionResponse response = buildResponse(20L, true, "1/6");

        Mockito.when(createRecurringTransactionUseCase.createSeries(eq(1L), any(TransactionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/transactions/recurring")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(20L))
                .andExpect(jsonPath("$.recurring").value(true))
                .andExpect(jsonPath("$.recurringInfo").value("1/6"))
                .andExpect(jsonPath("$.seriesGroupId").value("series-xyz"));
    }

    @Test
    @DisplayName("PATCH /api/transactions/{id}/toggle-paid deve alternar status de pago")
    void deveAlternarPago() throws Exception {
        Long txId = 30L;
        TransactionResponse response = new TransactionResponse(
                txId,
                "2025-11-25",
                "Aluguel",
                new BigDecimal("1500.00"),
                1L,
                true,   // agora pago
                false,
                false,
                null,
                null
        );

        Mockito.when(togglePaidUseCase.toggle(1L, txId))
                .thenReturn(response);

        mockMvc.perform(patch("/api/transactions/{id}/toggle-paid", txId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(txId))
                .andExpect(jsonPath("$.paid").value(true));
    }

    @Test
    @DisplayName("PATCH /api/transactions/{id}/toggle-planned deve alternar status de planejado")
    void deveAlternarPlanejado() throws Exception {
        Long txId = 40L;
        TransactionResponse response = new TransactionResponse(
                txId,
                "2025-11-25",
                "Aluguel",
                new BigDecimal("1500.00"),
                1L,
                false,
                true,   // agora planejado
                false,
                null,
                null
        );

        Mockito.when(togglePlannedUseCase.toggle(1L, txId))
                .thenReturn(response);

        mockMvc.perform(patch("/api/transactions/{id}/toggle-planned", txId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(txId))
                .andExpect(jsonPath("$.planned").value(true));
    }
}

 */