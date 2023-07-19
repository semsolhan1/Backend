package com.handifarm.api.market.repository;

import com.handifarm.api.market.entity.MarketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketItemRepository extends JpaRepository<MarketItem, Long> {
}
