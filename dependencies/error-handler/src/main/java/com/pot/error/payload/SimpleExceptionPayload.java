package com.pot.error.payload;

import com.pot.common.enums.ErrorCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class SimpleExceptionPayload extends ExceptionPayload
{
    public SimpleExceptionPayload(ErrorCategory errorCategory, String userMessage) {
        super(LocalDateTime.now(), errorCategory, userMessage);
    }
}
