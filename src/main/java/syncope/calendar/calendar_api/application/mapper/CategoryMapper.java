package syncope.calendar.calendar_api.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import syncope.calendar.calendar_api.adapter.out.persistence.entity.CategoryEntity;
import syncope.calendar.calendar_api.application.dto.categories.CategoryResponse;
import syncope.calendar.calendar_api.domain.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // domínio -> entidade
    @Mapping(target = "id", source = "id")
    CategoryEntity toEntity(Category category);

    // entidade -> domínio
    Category toDomain(CategoryEntity entity);

    // domínio -> resposta DTO
    CategoryResponse toResponse(Category category);
}