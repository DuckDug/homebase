package com.homebase.homebase.controller;

import com.homebase.homebase.dto.WatchlistCreateRequest;
import com.homebase.homebase.dto.WatchlistResponse;
import com.homebase.homebase.dto.WatchlistUpdateRequest;
import com.homebase.homebase.model.WatchlistStatus;
import com.homebase.homebase.service.UserContextService;
import com.homebase.homebase.service.WatchlistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final UserContextService userContextService;

    public WatchlistController(WatchlistService watchlistService, UserContextService userContextService) {
        this.watchlistService = watchlistService;
        this.userContextService = userContextService;
    }

    @PostMapping
    public ResponseEntity<WatchlistResponse> addWatchlist(@Valid @RequestBody WatchlistCreateRequest request, Authentication authentication) {
        Long userId = userContextService.getUserId(authentication);
        WatchlistResponse response = watchlistService.createWatchlist(userId, request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WatchlistResponse>> getWatchlist(Authentication authentication, @RequestParam Optional<WatchlistStatus> status) {
        Long userId = userContextService.getUserId(authentication);
        return status.map(watchlistStatus -> ResponseEntity.ok().body(watchlistService.getWatchlists(userId, watchlistStatus))).orElseGet(() -> ResponseEntity.ok().body(watchlistService.getWatchlists(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WatchlistResponse> getWatchlistById(Authentication authentication, @PathVariable Long id) {
        Long userId = userContextService.getUserId(authentication);
        return ResponseEntity.ok().body(watchlistService.getWatchlistById(userId, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WatchlistResponse> updateWatchlist(@Valid @RequestBody WatchlistUpdateRequest request, Authentication authentication, @PathVariable Long id) {
        Long userId = userContextService.getUserId(authentication);
        return ResponseEntity.ok().body(watchlistService.updateWatchlist(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WatchlistResponse> deleteWatchlistById(Authentication authentication, @PathVariable Long id) {
        Long userId = userContextService.getUserId(authentication);
        return ResponseEntity.ok().body(watchlistService.deleteWatchlist(userId, id));
    }

}
