package syncope.calendar.calendar_api.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import syncope.calendar.calendar_api.application.dto.TransactionResponse;
import syncope.calendar.calendar_api.domain.model.FinanceTransaction;

@Mapper(componentModel = "spring")
public interface FinanceTransactionMapper {

    @Mapping(target = "date", expression = "java(tx.getDate().toString())")
    TransactionResponse toResponse(FinanceTransaction tx);
}
