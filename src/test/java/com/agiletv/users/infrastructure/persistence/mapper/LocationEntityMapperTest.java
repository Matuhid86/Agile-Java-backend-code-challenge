package com.agiletv.users.infrastructure.persistence.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.agiletv.users.application.config.TestConfig;
import com.agiletv.users.domain.model.Location;
import com.agiletv.users.infrastructure.persistence.entity.LocationEntity;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
class LocationEntityMapperTest {

    @Autowired
    private LocationEntityMapper mapper;

    @Test
    void domainToEntityTest() {
        Location domain = new EasyRandom().nextObject(Location.class);

        LocationEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getCity(), entity.getCity());
        assertEquals(domain.getCountry(), entity.getCountry());
        assertEquals(domain.getState(), entity.getState());
    }

    @Test
    void entityToDomainTest() {
        LocationEntity entity = new EasyRandom().nextObject(LocationEntity.class);

        Location domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getCity(), domain.getCity());
        assertEquals(entity.getCountry(), domain.getCountry());
        assertEquals(entity.getState(), domain.getState());
    }
}
