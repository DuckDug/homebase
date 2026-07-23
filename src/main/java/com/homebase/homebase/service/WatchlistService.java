package com.homebase.homebase.service;

import com.homebase.homebase.dto.WatchlistCreateRequest;
import com.homebase.homebase.dto.WatchlistResponse;
import com.homebase.homebase.dto.WatchlistUpdateRequest;
import com.homebase.homebase.exception.DuplicateResourceException;
import com.homebase.homebase.exception.ResourceNotFoundException;
import com.homebase.homebase.model.Watchlist;
import com.homebase.homebase.model.WatchlistStatus;
import com.homebase.homebase.repository.WatchlistRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {
    private final WatchlistRepository watchlistRepository;

    public WatchlistService(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    public WatchlistResponse createWatchlist(Long userId, WatchlistCreateRequest watchlistCreateRequest) {
        String name = watchlistCreateRequest.getName();
        if (watchlistRepository.existsByUserIdAndNameIgnoreCase(userId, name)) {
            throw new DuplicateResourceException("Watchlist", "name", name);
        }

        Watchlist watchlist = new Watchlist();
        watchlist.setName(name);
        watchlist.setStatus(WatchlistStatus.ACTIVE);
        watchlist.setUserId(userId);

        Watchlist saved =  watchlistRepository.save(watchlist);

        return mapToWatchlistResponse(saved);

    }

    public List<WatchlistResponse> getWatchlists(Long userId) {
        return watchlistRepository.findByUserId(userId)
                .stream()
                .map(this::mapToWatchlistResponse)
                .toList();
    }

    public List<WatchlistResponse> getWatchlists(Long userId, WatchlistStatus status) {
        return watchlistRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(this::mapToWatchlistResponse)
                .toList();
    }

    public WatchlistResponse getWatchlistById(Long userId, Long id) {
        Watchlist watchlist = watchlistRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Watchlist", id
                ));
        return mapToWatchlistResponse(watchlist);

    }

    public WatchlistResponse updateWatchlist(Long userId, Long id,  WatchlistUpdateRequest watchlistUpdateRequest) {
        String name = watchlistUpdateRequest.getName();
        WatchlistStatus status = watchlistUpdateRequest.getStatus();
        Watchlist watchlist = watchlistRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist", id));

        if (name != null && !name.equals(watchlist.getName())) {
            if (watchlistRepository.existsByUserIdAndNameIgnoreCase(userId, name)) {
                throw new DuplicateResourceException("Watchlist", "name", name);
            } else {
                watchlist.setName(name);
            }

        }

        if (status != null) {
            watchlist.setStatus(status);
        }

        Watchlist saved = watchlistRepository.save(watchlist);
        return mapToWatchlistResponse(saved);
    }

    public WatchlistResponse deleteWatchlist(Long userId, Long id) {
        Watchlist watchlist = watchlistRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist", id));
        watchlistRepository.delete(watchlist);
        return mapToWatchlistResponse(watchlist);
    }

    private WatchlistResponse mapToWatchlistResponse(@NonNull Watchlist watchlist) {
        return new WatchlistResponse(
                watchlist.getId(),
                watchlist.getName(),
                watchlist.getStatus(),
                watchlist.getCreatedAt()
        );
    }
}
