package com.handifarm.api.board.repository;

import com.handifarm.api.board.entity.Board;
import com.handifarm.api.board.entity.Category;

import java.util.List;

public interface BoardRepositoryCustom {
  List<Board> findSearch(Board.Category category, String content);
}
