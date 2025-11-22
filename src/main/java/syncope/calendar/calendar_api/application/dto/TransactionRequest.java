package syncope.calendar.calendar_api.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransactionRequest (@NotNull
                                    String date,                     // "yyyy-MM-dd"

                                  @NotBlank
                                    String description,

                                  @NotNull
                                    @DecimalMin("0.01")
                                  BigDecimal value,

                                  @NotNull
                                    Long categoryId,

                                  Boolean planned,

                                  Boolean recurring,

                                  @Min(1)
                                    @Max(120)
                                    Integer recurringMonths,

                                  @Min(1)
                                    @Max(31)
                                    Integer dayOfMonth,

                                  Boolean ignoreDuplicateWarning
) {}
