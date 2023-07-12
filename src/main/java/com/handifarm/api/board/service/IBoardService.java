package com.handifarm.api.board.service;


import com.handifarm.api.board.dto.request.BoardModifyRequestDTO;
import com.handifarm.api.board.dto.request.BoardWriteRequestDTO;

public interface IBoardService {

    // 게시글 등록 처리
    void registBoard(final BoardWriteRequestDTO dto);

    // 게시글 수정 처리
    void updateBoard(BoardModifyRequestDTO dto);

    // 게시글 삭제 처리
    void deleteBoard(Long boardNo);



}
