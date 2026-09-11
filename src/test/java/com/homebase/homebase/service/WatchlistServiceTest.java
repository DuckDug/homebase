package com.homebase.homebase.service;

import com.homebase.homebase.exception.ResourceNotFoundException;
import com.homebase.homebase.model.Watchlist;
import com.homebase.homebase.repository.WatchlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class WatchlistServiceTest {

    @Mock
    private WatchlistRepository watchlistRepository;

    @InjectMocks
    private WatchlistService watchlistService;

    @Test
    void deleteWatchlist_whenFound_deletesSuccessfully() {
        Watchlist watchlist = new Watchlist();
        watchlist.setId(10L);
        watchlist.setUserId(1L);

        when(watchlistRepository.findByIdAndUserId(10L, 1L))
                .thenReturn(Optional.of(watchlist));

        watchlistService.deleteWatchlist(1L, 10L);

        verify(watchlistRepository).delete(watchlist);

    }

    @Test
    void deleteWatchlist_whenNotFoundOrNotOwned_throwsResourceNotFound() {
        Watchlist watchlist = new Watchlist();
        watchlist.setId(10L);
        watchlist.setUserId(1L);

        when(watchlistRepository.findByIdAndUserId(10L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> watchlistService.deleteWatchlist(1L, 10L)
        );

        verify(watchlistRepository, never()).delete(any());
    }
}
