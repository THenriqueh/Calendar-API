package syncope.calendar.calendar_api.adapter.out.persistence.mapper;

import org.mapstruct.Mapper;
import syncope.calendar.calendar_api.adapter.out.persistence.entity.FinanceTransactionEntity;
import syncope.calendar.calendar_api.domain.model.FinanceTransaction;

@Mapper(componentModel = "spring")
public interface FinanceTransactionPersistenceMapper {

    FinanceTransactionEntity toEntity(FinanceTransaction domain);

    FinanceTransaction toDomain(FinanceTransactionEntity entity);
}