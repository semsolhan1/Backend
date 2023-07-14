package com.handifarm.api.board.repository;

import com.handifarm.api.board.entity.Board;
import com.handifarm.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {


    // 게시글 개수 리턴
    @Query("SELECT COUNT(*) FROM Board")
    int countBoard();

    // 게시글 수정
    @Modifying
    @Query("UPDATE Board b SET b.title = :title, b.content = :content WHERE b.boardNo = :boardNo")
    void modifyBoard(Long boardNo, String title, String content);

    // 게시글 삭제
    @Modifying
    @Query("DELETE FROM Board b WHERE b.boardNo = :boardNo")
    void deleteBoard(Long boardNo);

    // 유저별 게시글 조회
    List<Board> findAllByUser(User user);
}


