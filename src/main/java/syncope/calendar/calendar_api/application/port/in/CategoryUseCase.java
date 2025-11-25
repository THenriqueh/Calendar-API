package syncope.calendar.calendar_api.application.port.in;

import syncope.calendar.calendar_api.application.dto.categories.CategoryRequest;
import syncope.calendar.calendar_api.application.dto.categories.CategoryResponse;

import java.util.List;

public interface CategoryUseCase {

    CategoryResponse create(Long userId, CategoryRequest request);

    List<CategoryResponse> list(Long userId);

    CategoryResponse update(Long userId, Long categoryId, CategoryRequest request);

    void delete(Long userId, Long categoryId);
}
