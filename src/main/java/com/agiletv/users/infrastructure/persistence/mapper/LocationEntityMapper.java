package com.agiletv.users.infrastructure.persistence.mapper;

import com.agiletv.users.domain.model.ILocation;
import com.agiletv.users.domain.model.Location;
import com.agiletv.users.infrastructure.persistence.entity.LocationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationEntityMapper {
    Location toDomain(LocationEntity location);

    Location toDomain(ILocation location);

    LocationEntity toEntity(Location location);

    LocationEntity toEntity(ILocation location);
}