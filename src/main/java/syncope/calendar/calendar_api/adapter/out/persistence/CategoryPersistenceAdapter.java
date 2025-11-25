package syncope.calendar.calendar_api.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import syncope.calendar.calendar_api.adapter.out.persistence.repository.CategoryJpaRepository;
import syncope.calendar.calendar_api.application.mapper.CategoryMapper;
import syncope.calendar.calendar_api.application.port.out.CategoryRepositoryPort;
import syncope.calendar.calendar_api.domain.model.Category;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository jpa;
    private final CategoryMapper mapper;

    @Override
    public Category save(Category category) {
        var entity = mapper.toEntity(category);
        var saved = jpa.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Category> findByIdAndUserId(Long id, Long userId) {
        return jpa.findByIdAndUserId(id, userId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Category> findAllByUserId(Long userId) {
        return jpa.findAllByUserId(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByNameAndUserId(String name, Long userId) {
        return jpa.existsByNameIgnoreCaseAndUserId(name, userId);
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {
        jpa.deleteByIdAndUserId(id, userId);
    }
}
