package com.agiletv.user.context.infrastructure.service;

import com.agiletv.user.app.exception.NotFoundException;
import com.agiletv.user.context.mapper.GenericMapper;
import com.agiletv.user.context.repository.GenericRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public abstract class GenericService<D, E, ID> {
    protected final GenericRepository<E, ID> repository;
    protected final GenericMapper<D, E> mapper;

    protected GenericService(GenericRepository<E, ID> repository, GenericMapper<D, E> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    protected List<D> getAll() {
        return mapper.toDtos(repository.findAll());
    }

    protected Page<D> getAll(Pageable pageable) {
        Page<E> page = repository.findAll(pageable);
        return new PageImpl<D>(this.mapper.toDtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }

    protected Optional<D> getById(ID id) {
        return repository.findById(id).map(mapper::toDto);
    }

    protected D save(D dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    protected D update(ID id, D dto) throws NotFoundException {
        Optional<E> entity = repository.findById(id);
        if (entity.isPresent()) {
            mapper.updateEntityFromDto(dto, entity.get());
            return mapper.toDto(repository.save(entity.get()));
        }
        throw new NotFoundException(String.format("Entity not found with id: %s", id));
    }

    protected void delete(ID id) throws NotFoundException {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new NotFoundException(String.format("Entity not found with id: %s", id));
        }
    }
}
