package com.handifarm.api.board.repository;

import com.handifarm.api.board.entity.Board;
import com.handifarm.api.board.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository
        extends JpaRepository <Board, Long> {


  List<Board> findByCategory(Category category);
}
