package com.safehaven.dto;

import java.util.List;
import java.util.Map;

public record AdminOverviewDto(
        long totalUsers,
        long totalResources,
        long totalSupportRequests,
        Map<String, Long> roleCounts,
        Map<String, Long> supportStatusCounts,
        List<SupportRequestDto> recentSupportRequests
) {
}
