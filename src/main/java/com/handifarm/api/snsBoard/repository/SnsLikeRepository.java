package com.handifarm.api.snsBoard.repository;

import com.handifarm.api.snsBoard.entity.SnsLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsLikeRepository extends JpaRepository<SnsLike, Long> {

    boolean existsBySnsBoardSnsNoAndUserUserNick(long snsNo, String userNick);

    SnsLike findBySnsBoardSnsNoAndUserUserNick (long snsNo, String userNick);

}
