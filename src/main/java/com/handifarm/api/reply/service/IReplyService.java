package com.handifarm.api.reply.service;

import com.handifarm.api.reply.dto.request.BoardReplyModifyRequestDTO;
import com.handifarm.api.reply.dto.request.BoardReplyWriteRequestDTO;
import com.handifarm.api.reply.dto.response.BoardReplyListResponseDTO;
import com.handifarm.jwt.TokenUserInfo;

public interface IReplyService {

    BoardReplyListResponseDTO retrieve();

    void registReply(BoardReplyWriteRequestDTO requestDTO, TokenUserInfo userInfo);

    void updateReply(BoardReplyModifyRequestDTO requestDTO);

    void deleteReply(long replyNo, TokenUserInfo userInfo);



}
