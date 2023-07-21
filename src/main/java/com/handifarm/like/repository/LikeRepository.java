package com.handifarm.like.repository;

import com.handifarm.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {
    boolean existsByPostIdAndCommentId(Long postId, Long commentId);

    void deleteByPostIdAndCommentId(Long postId, Long commentId);

}
