package com.handifarm.cboard.repository;

import com.handifarm.cboard.entity.Cboard;
import com.handifarm.cboard.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag,Long> {

    List<HashTag> findByCboard(Cboard cboard);

}
