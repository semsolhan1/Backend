package com.handifarm.api.board.repository;

import com.handifarm.api.board.entity.BoardReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

    // 댓글 수정
    @Modifying
    @Query("UPDATE BoardReply br SET br.boardReplyContent = :boardReplyContent WHERE br.userNick = :userNick")
    void modifyBoardReply(String boardReplyContent, String userNick);

    // 댓글 삭제
    @Query("DELETE FROM BoardReply br WHERE br.boardReplyNo = :boardReplyNo")
    void deleteBoardReply(Long boardReplyNo);

}
