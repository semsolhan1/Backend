package com.handifarm.api.market.repository;

import com.handifarm.api.market.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, String> {
}
