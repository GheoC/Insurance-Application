package com.pot.benefits.model;

import com.pot.common.enums.CompanyStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Company
{
    private String companyName;

    private String countryCode;

    private String email;

    private String site;

    private CompanyStatus status;
}
