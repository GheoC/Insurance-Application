package com.pot.common.mapper.base;

import org.mapstruct.MapperConfig;

@MapperConfig(componentModel = "spring")
public interface ToDtoMapper<Entity, DTO> {

    DTO toDto(Entity source);
}
