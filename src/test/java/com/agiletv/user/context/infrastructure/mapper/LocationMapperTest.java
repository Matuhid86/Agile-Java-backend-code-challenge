package com.agiletv.user.context.infrastructure.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.agiletv.user.app.config.TestConfig;
import com.agiletv.user.context.infrastructure.domain.LocationEntity;
import com.agiletv.user.context.model.dto.LocationDto;
import java.util.ArrayList;
import java.util.List;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
class LocationMapperTest {

    @Autowired
    private LocationMapper mapper;

    @Test
    void dtoToEntityTest() {
        LocationDto dto = new EasyRandom().nextObject(LocationDto.class);

        LocationEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getCity(), entity.getCity());
        assertEquals(dto.getCountry(), entity.getCountry());
        assertEquals(dto.getState(), entity.getState());
    }

    @Test
    void entityToDtoTest() {
        LocationEntity entity = new EasyRandom().nextObject(LocationEntity.class);

        LocationDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getCity(), dto.getCity());
        assertEquals(entity.getCountry(), dto.getCountry());
        assertEquals(entity.getState(), dto.getState());
    }

    @Test
    void dtosToEntitiesTest() {
        List<LocationDto> dtos = new ArrayList<>();
        LocationDto dto = new EasyRandom().nextObject(LocationDto.class);

        dtos.add(dto);

        List<LocationEntity> entities = mapper.toEntities(dtos);

        assertNotNull(entities);
        assertThat(entities).hasSize(1);
        assertEquals(dto.getCity(), entities.get(0).getCity());
        assertEquals(dto.getCountry(), entities.get(0).getCountry());
        assertEquals(dto.getState(), entities.get(0).getState());
    }

    @Test
    void entitiesToDtosTest() {
        List<LocationEntity> entities = new ArrayList<>();
        LocationEntity entity = new EasyRandom().nextObject(LocationEntity.class);

        entities.add(entity);

        List<LocationDto> dtos = mapper.toDtos(entities);

        assertNotNull(dtos);
        assertThat(dtos).hasSize(1);
        assertEquals(entity.getCity(), dtos.get(0).getCity());
        assertEquals(entity.getCountry(), dtos.get(0).getCountry());
        assertEquals(entity.getState(), dtos.get(0).getState());
    }

    @Test
    void updateEntityFromDtoTest() {
        LocationDto dto = new EasyRandom().nextObject(LocationDto.class);
        LocationEntity entity = new EasyRandom().nextObject(LocationEntity.class);

        mapper.updateEntityFromDto(dto, entity);

        assertEquals(dto.getCity(), entity.getCity());
        assertEquals(dto.getCountry(), entity.getCountry());
        assertEquals(dto.getState(), entity.getState());
    }
}
