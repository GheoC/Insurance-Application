package com.pot.common.mapper.base;

import org.mapstruct.MapperConfig;

@MapperConfig(componentModel = "spring")
public interface BaseMapper<Entity, DTO>
    extends ToDtoMapper<Entity, DTO>, ToEntityMapper<Entity, DTO> {}
