package com.handifarm.api.snsBoard.repository;

import com.handifarm.api.snsBoard.entity.SnsBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnsBoardRepository extends JpaRepository<SnsBoard, Long> {

    List<SnsBoard> findAllByUserNick(String userNick);

}
