package com.handifarm.api.board.repository;

import com.handifarm.api.board.entity.Board;

import java.util.List;

public interface BoardSearchRepository {

    List<Board> searchBoardList(String category, String condition, String searchWord);


}
