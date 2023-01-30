package com.pot.error.payload;


import com.pot.common.enums.ErrorCategory;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(as = ComplexExceptionPayload.class)
public abstract class ExceptionPayload
{
    private LocalDateTime timestamp;
    private ErrorCategory errorCategory;
    private String userMessage;

}
