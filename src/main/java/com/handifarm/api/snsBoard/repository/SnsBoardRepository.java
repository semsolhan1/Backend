package com.handifarm.api.snsBoard.repository;

import com.handifarm.api.snsBoard.entity.SnsBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsBoardRepository extends JpaRepository<SnsBoard, Long> {
}
