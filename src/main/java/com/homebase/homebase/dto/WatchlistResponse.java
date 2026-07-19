package com.homebase.homebase.dto;

import com.homebase.homebase.model.WatchlistStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class WatchlistResponse {
    private Long id;
    private String name;
    private WatchlistStatus status;
    private LocalDateTime createdAt;
}
