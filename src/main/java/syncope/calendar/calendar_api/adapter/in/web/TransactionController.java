package syncope.calendar.calendar_api.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionFilterCommand;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionFilterRequest;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionRequest;
import syncope.calendar.calendar_api.application.dto.transaction.TransactionResponse;
import syncope.calendar.calendar_api.application.port.in.TransactionUseCase;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionUseCase transactionUseCase;

    // por enquanto sem auth, userId fixo
    private Long getCurrentUserId() {
        return 1L;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> list(
            TransactionFilterRequest filter
    ) {
        Long userId = getCurrentUserId();
        TransactionFilterCommand command = TransactionFilterCommand.fromRequest(filter);
        var result = transactionUseCase.list(userId, command);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @Valid @RequestBody TransactionRequest request
    ) {
        Long userId = getCurrentUserId();
        var response = transactionUseCase.create(userId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recurring")
    public ResponseEntity<TransactionResponse> createRecurring(
            @Valid @RequestBody TransactionRequest request
    ) {
        Long userId = getCurrentUserId();
        var response = transactionUseCase.createRecurring(userId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle-paid")
    public ResponseEntity<TransactionResponse> togglePaid(
            @PathVariable Long id
    ) {
        Long userId = getCurrentUserId();
        var response = transactionUseCase.togglePaid(userId, id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle-planned")
    public ResponseEntity<TransactionResponse> togglePlanned(
            @PathVariable Long id
    ) {
        Long userId = getCurrentUserId();
        var response = transactionUseCase.togglePlanned(userId, id);
        return ResponseEntity.ok(response);
    }
}
