package com.handifarm.api.board.service;

import com.handifarm.api.board.dto.request.BoardModifyRequestDTO;
import com.handifarm.api.board.dto.request.BoardWriteRequestDTO;
import com.handifarm.api.board.dto.response.BoardListResponseDTO;
import com.handifarm.api.board.entity.Board;
import com.handifarm.jwt.TokenUserInfo;

import java.util.List;

public interface IBoardService {

    BoardListResponseDTO retrieve();

    void registBoard(BoardWriteRequestDTO requestDTO, TokenUserInfo userInfo);

    void updateBoard(BoardModifyRequestDTO requestDTO);

    void deleteBoard(long boardNo, TokenUserInfo userInfo);

}
