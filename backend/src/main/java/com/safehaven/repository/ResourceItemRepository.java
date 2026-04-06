package com.safehaven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.safehaven.entity.ResourceItem;

public interface ResourceItemRepository extends JpaRepository<ResourceItem, Long> {
    List<ResourceItem> findAllByOrderByTypeAscTitleAsc();
}
