package com.handifarm.api.reply.repository;

import com.handifarm.api.reply.entity.BoardReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

    // 각 게시글 별 댓글만 리턴
    Page<BoardReply> findByBoardNo(Long boardNo, Pageable pageable);

    // 댓글 개수 리턴
    @Query("SELECT COUNT(*) FROM BoardReply")
    int countBoardReply();

    // 댓글 수정
    @Modifying
    @Query("UPDATE BoardReply SET reply = :reply WHERE boardNo = :boardNo")
    void modifyBoardReply(Long boardNo, String reply);

    // 댓글 삭제
    @Query("DELETE FROM BoardReply WHERE replyNo = :replyNo")
    void deleteBoardReply(Long replyNo);

}
