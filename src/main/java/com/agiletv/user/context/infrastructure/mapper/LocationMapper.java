package com.agiletv.user.context.infrastructure.mapper;

import com.agiletv.user.context.infrastructure.domain.LocationEntity;
import com.agiletv.user.context.model.dto.LocationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = GenericMapper.class)
public interface LocationMapper extends GenericMapper<LocationDto, LocationEntity>  { }