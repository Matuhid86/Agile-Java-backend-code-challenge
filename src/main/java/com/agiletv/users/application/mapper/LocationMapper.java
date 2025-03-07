package com.agiletv.users.application.mapper;

import com.agiletv.users.domain.model.Location;
import com.agiletv.users.infrastructure.persistence.entity.LocationEntity;
import com.agiletv.users.application.dto.LocationDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LocationMapper  {
    LocationDto toDto(Location location);

    Location toDomain(LocationDto location);

    void updateDomainFromDto(LocationDto dto, @MappingTarget Location domain);
}