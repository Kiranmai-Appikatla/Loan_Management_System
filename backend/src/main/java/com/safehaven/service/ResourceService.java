package com.safehaven.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.safehaven.dto.ResourceDto;
import com.safehaven.dto.ResourceRequest;
import com.safehaven.entity.ResourceItem;
import com.safehaven.repository.ResourceItemRepository;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ResourceService {

    private final ResourceItemRepository resourceItemRepository;

    public ResourceService(ResourceItemRepository resourceItemRepository) {
        this.resourceItemRepository = resourceItemRepository;
    }

    public List<ResourceDto> getAll() {
        return resourceItemRepository.findAllByOrderByTypeAscTitleAsc().stream().map(ResourceDto::from).toList();
    }

    @Transactional
    public ResourceDto create(ResourceRequest request) {
        ResourceItem saved = resourceItemRepository.save(
                new ResourceItem(request.title(), request.description(), request.type(), request.contactLink())
        );
        return ResourceDto.from(saved);
    }

    @Transactional
    public ResourceDto update(Long id, ResourceRequest request) {
        ResourceItem resourceItem = resourceItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Resource not found"));
        resourceItem.setTitle(request.title());
        resourceItem.setDescription(request.description());
        resourceItem.setType(request.type());
        resourceItem.setContactLink(request.contactLink());
        return ResourceDto.from(resourceItemRepository.save(resourceItem));
    }

    @Transactional
    public void delete(Long id) {
        if (!resourceItemRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Resource not found");
        }
        resourceItemRepository.deleteById(id);
    }
}
