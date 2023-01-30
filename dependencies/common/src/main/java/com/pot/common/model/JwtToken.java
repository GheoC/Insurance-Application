package com.pot.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JwtToken
{
    @JsonProperty(value = "access_token")
    private String accessToken;
}
