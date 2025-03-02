package com.agiletv.user.context.mapper;

import java.util.List;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingTarget;

@MapperConfig
public interface GenericMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntities(List<D> dtos);

    List<D> toDtos(List<E> entities);

    void updateEntityFromDto(D dto, @MappingTarget E entity);
}