package com.agiletv.user.context.mapper;

import com.agiletv.user.context.infrastructure.domain.LocationEntity;
import com.agiletv.user.context.model.LocationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = GenericMapper.class)
public interface LocationMapper extends GenericMapper<LocationDto, LocationEntity>  { }