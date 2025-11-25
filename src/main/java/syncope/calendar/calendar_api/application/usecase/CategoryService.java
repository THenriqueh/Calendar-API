package syncope.calendar.calendar_api.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import syncope.calendar.calendar_api.application.dto.categories.CategoryRequest;
import syncope.calendar.calendar_api.application.dto.categories.CategoryResponse;
import syncope.calendar.calendar_api.application.mapper.CategoryMapper;
import syncope.calendar.calendar_api.application.port.in.CategoryUseCase;
import syncope.calendar.calendar_api.application.port.out.CategoryRepositoryPort;
import syncope.calendar.calendar_api.domain.exception.NotFoundException;
import syncope.calendar.calendar_api.domain.model.Category;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryUseCase {

    private final CategoryRepositoryPort categoryRepository;
    private final CategoryMapper mapper;

    @Override
    public CategoryResponse create(Long userId, CategoryRequest request) {
        if (categoryRepository.existsByNameAndUserId(request.name(), userId)) {
            throw new IllegalArgumentException("Já existe categoria com esse nome");
        }

        Category category = Category.builder()
                .userId(userId)
                .name(request.name().trim())
                .build();

        Category saved = categoryRepository.save(category);
        return mapper.toResponse(saved);
    }

    @Override
    public List<CategoryResponse> list(Long userId) {
        return categoryRepository
                .findAllByUserId(userId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse update(Long userId, Long categoryId, CategoryRequest request) {

        Category category = categoryRepository
                .findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new NotFoundException("", "Categoria não encontrada"));

        // Verifica duplicação ao renomear
        if (categoryRepository.existsByNameAndUserId(request.name(), userId)) {
            throw new IllegalArgumentException("Já existe categoria com esse nome");
        }

        category.setName(request.name().trim());

        Category saved = categoryRepository.save(category);

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long categoryId) {
        // garante 404 se não existir
        categoryRepository
                .findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new NotFoundException("","Categoria não encontrada"));

        categoryRepository.deleteByIdAndUserId(categoryId, userId);
    }
}
