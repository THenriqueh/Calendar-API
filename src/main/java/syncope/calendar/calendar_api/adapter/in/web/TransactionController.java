package syncope.calendar.calendar_api.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import syncope.calendar.calendar_api.application.dto.TransactionFilterCommand;
import syncope.calendar.calendar_api.application.dto.TransactionFilterRequest;
import syncope.calendar.calendar_api.application.dto.TransactionRequest;
import syncope.calendar.calendar_api.application.dto.TransactionResponse;
import syncope.calendar.calendar_api.application.port.in.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final CreateRecurringTransactionUseCase createRecurringTransactionUseCase;
    private final ListTransactionsUseCase listTransactionsUseCase;
    private final TogglePaidUseCase togglePaidUseCase;
    private final TogglePlannedUseCase togglePlannedUseCase;

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> list(
            TransactionFilterRequest filter,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        // enquanto você não tiver auth, pode passar um userId fixo (ex.: 1L)
        if (userId == null) {
            userId = 1L;
        }

        TransactionFilterCommand command = TransactionFilterCommand.fromRequest(filter);
        var result = listTransactionsUseCase.list(userId, command);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        if (userId == null) userId = 1L;

        var response = createTransactionUseCase.create(userId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recurring")
    public ResponseEntity<TransactionResponse> createRecurring(
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        if (userId == null) userId = 1L;

        var response = createRecurringTransactionUseCase.createSeries(userId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle-paid")
    public ResponseEntity<TransactionResponse> togglePaid(
            @PathVariable Long id,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        if (userId == null) userId = 1L;

        var response = togglePaidUseCase.toggle(userId, id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle-planned")
    public ResponseEntity<TransactionResponse> togglePlanned(
            @PathVariable Long id,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        if (userId == null) userId = 1L;

        var response = togglePlannedUseCase.toggle(userId, id);
        return ResponseEntity.ok(response);
    }
}