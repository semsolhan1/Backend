package com.handifarm.api.market.repository;

import com.handifarm.api.market.entity.TransHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransHistoryRepository extends JpaRepository<TransHistory, String> {
}
