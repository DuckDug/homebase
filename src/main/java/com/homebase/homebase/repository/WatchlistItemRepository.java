package com.homebase.homebase.repository;

import com.homebase.homebase.model.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistItemRepository extends JpaRepository<WatchlistItem, Long> {

    List<WatchlistItem> findAllByWatchlistId(Long watchlistId);

    List<WatchlistItem> findAllByItemTypeAndWatchlistId(String itemType,  Long watchlistId);

    boolean existsByWatchlistIdAndReferenceId(Long watchlistId, String referenceId);
}
