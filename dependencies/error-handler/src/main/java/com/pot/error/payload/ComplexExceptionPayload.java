package com.pot.error.payload;

import com.pot.common.enums.ErrorCategory;
import com.pot.error.payload.model.Error;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ComplexExceptionPayload extends ExceptionPayload
{
    private List<Error> errors;

    public ComplexExceptionPayload(ErrorCategory errorCategory, String userMessage, List<Error> errors)
    {
        super(LocalDateTime.now(), errorCategory, userMessage);
        this.errors = errors;
    }
}
