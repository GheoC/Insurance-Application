package com.pot.users.helpers;

import com.pot.users.entity.contact.Address;
import com.pot.users.mapper.AddressMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class AddressHelper
{
    private final AddressMapper addressMapper;

    public Address create()
    {
        Address address = new Address();
        address.setCountry("Romania");
        address.setCity("Brasov");
        address.setRoom("23");
        address.setState("Brasov County");
        address.setBuilding("9");
        address.setStreet("Carpatilor");
        return address;
    }
}
