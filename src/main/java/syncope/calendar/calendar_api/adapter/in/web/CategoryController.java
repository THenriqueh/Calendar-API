package syncope.calendar.calendar_api.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import syncope.calendar.calendar_api.application.dto.categories.CategoryRequest;
import syncope.calendar.calendar_api.application.dto.categories.CategoryResponse;
import syncope.calendar.calendar_api.application.port.in.CategoryUseCase;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryUseCase categoryUseCase;

    // por enquanto sem auth, userId fixo = 1L
    private Long getCurrentUserId() {
        return 1L;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @Valid @RequestBody CategoryRequest request
    ) {
        Long userId = getCurrentUserId();
        var response = categoryUseCase.create(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> list() {
        Long userId = getCurrentUserId();
        var list = categoryUseCase.list(userId);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestParam Long categoryId
    ) {
        Long userId = getCurrentUserId();
        categoryUseCase.delete(userId, categoryId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest request
    ) {
        Long userId = getCurrentUserId();
        var response = categoryUseCase.update(userId, categoryId, request);
        return ResponseEntity.ok(response);
    }

}
