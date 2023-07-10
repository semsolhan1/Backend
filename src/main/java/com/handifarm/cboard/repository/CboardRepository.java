package com.handifarm.cboard.repository;

import com.handifarm.cboard.entity.Cboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


public interface CboardRepository extends JpaRepository<Cboard,String>{


    Cboard findFirstByBoardTimeLessThanOrderByBoardTimeDesc(LocalDateTime boardTime);


}
