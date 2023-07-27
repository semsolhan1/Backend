package com.handifarm.api.board.repository;

import com.handifarm.api.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearchRepository {

    // 게시글 개수 리턴
    @Query("SELECT COUNT(*) FROM Board")
    int countBoard();

    // 게시글 수정
    @Modifying
    @Query("UPDATE Board b SET b.title = :title, b.content = :content WHERE b.boardNo = :boardNo")
    void modifyBoard(Long boardNo, String title, String content);

    // 게시글 삭제
    @Query("DELETE FROM Board b WHERE b.boardNo = :boardNo")
    void deleteBoard(Long boardNo);

}
