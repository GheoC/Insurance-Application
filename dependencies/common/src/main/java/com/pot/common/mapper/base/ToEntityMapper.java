package com.pot.common.mapper.base;

import org.mapstruct.MapperConfig;

@MapperConfig(componentModel = "spring")
public interface ToEntityMapper<Entity, DTO> {

    Entity toEntity(DTO dto);
}
