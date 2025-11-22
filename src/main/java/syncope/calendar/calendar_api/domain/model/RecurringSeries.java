package syncope.calendar.calendar_api.domain.model;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@Builder
public class RecurringSeries {

    private String id;           // ex: UUID
    private Long userId;
    private LocalDate startDate;
    private Integer totalMonths;
    private Integer dayOfMonth;
}