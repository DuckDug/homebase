package com.homebase.homebase.dto;

import com.homebase.homebase.model.WatchlistStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WatchlistUpdateRequest {

    private String name;
    private WatchlistStatus status;
}
