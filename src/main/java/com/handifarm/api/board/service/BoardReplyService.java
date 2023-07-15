package com.handifarm.api.board.service;

import com.handifarm.api.board.dto.request.BoardReplyModifyRequestDTO;
import com.handifarm.api.board.dto.request.BoardReplyWriteRequestDTO;
import com.handifarm.api.board.entity.BoardReply;
import com.handifarm.api.board.repository.BoardReplyRepository;
import com.handifarm.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardReplyService {


    private final BoardReplyRepository boardReplyRepository;

    public void registBoardReply(BoardReplyWriteRequestDTO requestDTO, TokenUserInfo userInfo) {
        String userNick = userInfo.getUserNick();
        Long boardNo = requestDTO.getBoardNo();
        String boardReplyContent = requestDTO.getBoardReplyContent();

        BoardReply boardReply = BoardReply.builder()
                .userNick(userNick)
                .boardNo((boardNo))
                .boardReplyContent(boardReplyContent)
                .build();

        boardReplyRepository.save(boardReply);
        log.info("댓글 등록 완료");
    }

    public void updateBoardReply(BoardReplyModifyRequestDTO requestDTO) {
        String boardReplyContent = requestDTO.getBoardReplyContent();

        BoardReply boardReply = boardReplyRepository.findById(requestDTO.getBoardReplyNo())
                .orElseThrow(() -> new RuntimeException("댓글이 없습니다."));

        boardReply.setBoardReplyContent(boardReplyContent);
    }

    public void deleteBoardReply(long boardReplyNo) {
        BoardReply boardReply = boardReplyRepository.findById(boardReplyNo)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        boardReplyRepository.delete(boardReply);
    }

}
