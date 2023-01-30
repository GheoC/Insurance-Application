package com.pot.error.payload.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
public class Error implements Serializable
{
    private String objectName;
    private String field;
    private String defaultMessage;

    public Error(String objectName, String field, String defaultMessage) {
        this.objectName = objectName;
        this.field = field;
        this.defaultMessage = defaultMessage;
    }

    public Error(String objectName, String defaultMessage) {
        this.objectName = objectName;
        this.defaultMessage = defaultMessage;
    }
}
