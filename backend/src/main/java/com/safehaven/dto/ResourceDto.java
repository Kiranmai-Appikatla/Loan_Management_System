package com.safehaven.dto;

import com.safehaven.entity.ResourceItem;

public record ResourceDto(Long id, String title, String description, String type, String contactLink) {

    public static ResourceDto from(ResourceItem resourceItem) {
        return new ResourceDto(
                resourceItem.getId(),
                resourceItem.getTitle(),
                resourceItem.getDescription(),
                resourceItem.getType(),
                resourceItem.getContactLink()
        );
    }
}
