package com.homebase.homebase.repository;

import com.homebase.homebase.model.Watchlist;
import com.homebase.homebase.model.WatchlistStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    List<Watchlist> findByUserIdAndName(Long userId, String name);

    List<Watchlist> findByUserId(Long userId);

    List<Watchlist> findByUserIdAndStatus(Long userId, WatchlistStatus status);

    boolean existsByUserIdAndName(Long userId, String name);

    Optional<Watchlist> findByIdAndUserId(Long id, Long userId);
}
