package syncope.calendar.calendar_api.domain.service;

import org.junit.jupiter.api.Test;
import syncope.calendar.calendar_api.domain.model.FinanceTransaction;
import syncope.calendar.calendar_api.domain.model.RecurringSeries;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionDomainServiceTest {

    private final TransactionDomainService service = new TransactionDomainService();

    @Test
    void createSeries_deve_preencher_campos_corretamente() {
        // given
        String seriesId = "series-123";
        Long userId = 1L;
        LocalDate startDate = LocalDate.of(2025, 11, 25);
        Integer totalMonths = 6;
        Integer preferredDay = 25;

        // when
        RecurringSeries series = service.createSeries(
                seriesId,
                userId,
                startDate,
                totalMonths,
                preferredDay
        );

        // then
        assertThat(series.getId()).isEqualTo(seriesId);
        assertThat(series.getUserId()).isEqualTo(userId);
        assertThat(series.getStartDate()).isEqualTo(startDate);
        assertThat(series.getTotalMonths()).isEqualTo(totalMonths);
        assertThat(series.getDayOfMonth()).isEqualTo(preferredDay);
    }

    @Test
    void generateRecurringTransactions_deve_criar_lista_com_tamanhos_e_campos_corretos() {
        // given
        RecurringSeries series = RecurringSeries.builder()
                .id("series-abc")
                .userId(1L)
                .startDate(LocalDate.of(2025, 1, 31))
                .totalMonths(3)
                .dayOfMonth(31)
                .build();

        String description = "Aluguel";
        BigDecimal value = new BigDecimal("1500.00");
        Long categoryId = 1L;
        boolean ignoreDuplicate = false;

        // when
        List<FinanceTransaction> result = service.generateRecurringTransactions(
                series,
                description,
                value,
                categoryId,
                ignoreDuplicate
        );

        // then
        assertThat(result).hasSize(3);

        FinanceTransaction t1 = result.get(0);
        FinanceTransaction t2 = result.get(1);
        FinanceTransaction t3 = result.get(2);

        // mesma série / usuário / categoria
        assertThat(t1.getSeriesGroupId()).isEqualTo("series-abc");
        assertThat(t1.getUserId()).isEqualTo(1L);
        assertThat(t1.getCategoryId()).isEqualTo(1L);

        // datas "clampadas": 31/01, 29/02 -> 29, 31/03 -> 31
        assertThat(t1.getDate()).isEqualTo(LocalDate.of(2025, 1, 31));
        assertThat(t2.getDate()).isEqualTo(LocalDate.of(2025, 2, 28)); // ano não bissexto
        assertThat(t3.getDate()).isEqualTo(LocalDate.of(2025, 3, 31));

        // info de recorrência
        assertThat(t1.getRecurringInfo()).isEqualTo("1/3");
        assertThat(t2.getRecurringInfo()).isEqualTo("2/3");
        assertThat(t3.getRecurringInfo()).isEqualTo("3/3");

        // flags
        assertThat(t1.isRecurring()).isTrue();
        assertThat(t1.isPaid()).isFalse();
        assertThat(t1.isPlanned()).isFalse();
    }
}
