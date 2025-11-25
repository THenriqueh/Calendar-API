package syncope.calendar.calendar_api.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import syncope.calendar.calendar_api.adapter.out.persistence.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findAllByUserId(Long userId);

    Optional<CategoryEntity> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    boolean existsByNameIgnoreCaseAndUserId(String name, Long userId);
}
