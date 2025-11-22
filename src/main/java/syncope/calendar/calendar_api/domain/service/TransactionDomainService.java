package syncope.calendar.calendar_api.domain.service;


import lombok.RequiredArgsConstructor;
import syncope.calendar.calendar_api.domain.model.FinanceTransaction;
import syncope.calendar.calendar_api.domain.model.RecurringSeries;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TransactionDomainService {

    public RecurringSeries createSeries(
            String seriesId,
            Long userId,
            LocalDate startDate,
            Integer totalMonths,
            Integer preferredDayOfMonth
    ) {
        return RecurringSeries.builder()
                .id(seriesId)
                .userId(userId)
                .startDate(startDate)
                .totalMonths(totalMonths)
                .dayOfMonth(preferredDayOfMonth)
                .build();
    }

    public List<FinanceTransaction> generateRecurringTransactions(
            RecurringSeries series,
            String description,
            BigDecimal value,
            Long categoryId,
            boolean ignoreDuplicateWarning
    ) {
        List<FinanceTransaction> list = new ArrayList<>();

        for (int i = 0; i < series.getTotalMonths(); i++) {
            LocalDate date = clampDate(series.getStartDate().plusMonths(i), series.getDayOfMonth());

            FinanceTransaction tx = FinanceTransaction.builder()
                    .userId(series.getUserId())
                    .date(date)
                    .description(description)
                    .value(value)
                    .categoryId(categoryId)
                    .paid(false)
                    .planned(false)
                    .recurring(true)
                    .recurringInfo((i + 1) + "/" + series.getTotalMonths())
                    .seriesGroupId(series.getId())
                    .ignoreDuplicateWarning(ignoreDuplicateWarning)
                    .build();

            list.add(tx);
        }

        return list;
    }

    private LocalDate clampDate(LocalDate base, Integer dayOfMonth) {
        int lastDay = base.lengthOfMonth();
        int safeDay = Math.min(dayOfMonth, lastDay);
        return base.withDayOfMonth(safeDay);
    }
}
