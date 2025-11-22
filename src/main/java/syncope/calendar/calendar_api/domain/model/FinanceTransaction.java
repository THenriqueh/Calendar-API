package syncope.calendar.calendar_api.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Builder
public class FinanceTransaction {
    private Long id;                 // id do domínio (pode ser Long)
    private Long userId;             // multi-tenant

    private LocalDate date;
    private String description;
    private BigDecimal value;

    private Long categoryId;

    private boolean paid;
    private boolean planned;

    private boolean recurring;
    private String recurringInfo;    // "1/6"
    private String seriesGroupId;    // id lógico da série

    private boolean ignoreDuplicateWarning;

    public void togglePaid() {
        this.paid = !this.paid;
    }

    public void togglePlanned() {
        this.planned = !this.planned;
    }

    public boolean isDuplicateOf(FinanceTransaction other) {
        return Objects.equals(this.userId, other.userId)
                && Objects.equals(this.date, other.date)
                && this.value != null
                && this.value.compareTo(other.value) == 0;
    }


}
