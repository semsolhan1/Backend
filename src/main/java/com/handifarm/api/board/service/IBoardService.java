package com.handifarm.api.board.service;

import com.handifarm.api.board.dto.request.BoardModifyRequestDTO;
import com.handifarm.api.board.dto.request.BoardWriteRequestDTO;
import com.handifarm.api.board.dto.response.BoardListResponseDTO;
import com.handifarm.jwt.TokenUserInfo;

public interface IBoardService {

    BoardListResponseDTO retrieve();

    void registBoard(BoardWriteRequestDTO requestDTO, TokenUserInfo userInfo);

    void updateBoard(BoardModifyRequestDTO requestDTO);

    void deleteBoard(long boardNo, TokenUserInfo userInfo);
}
