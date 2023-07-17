package com.handifarm.api.reply.service;

import com.handifarm.api.board.dto.response.BoardDetailResponseDTO;
import com.handifarm.api.board.dto.response.BoardListResponseDTO;
import com.handifarm.api.board.entity.Board;
import com.handifarm.api.reply.dto.request.BoardReplyModifyRequestDTO;
import com.handifarm.api.reply.dto.request.BoardReplyWriteRequestDTO;
import com.handifarm.api.reply.dto.response.BoardReplyDetailResponseDTO;
import com.handifarm.api.reply.dto.response.BoardReplyListResponseDTO;
import com.handifarm.api.reply.entity.BoardReply;
import com.handifarm.api.reply.repository.BoardReplyRepository;
import com.handifarm.api.util.page.PageDTO;
import com.handifarm.api.util.page.PageResponseDTO;
import com.handifarm.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.cert.CertPathBuilder;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardReplyService {

    private final BoardReplyRepository boardReplyRepository;


    public BoardReplyListResponseDTO retrieve() {
        List<BoardReply> entityList = boardReplyRepository.findAll();

        List<BoardReplyDetailResponseDTO> dtoList = entityList.stream()
                .map(BoardReplyDetailResponseDTO::new)
                .collect(Collectors.toList());

        return BoardReplyListResponseDTO.builder()
                .boardReplies(dtoList)
                .build();
    }

    public BoardReplyListResponseDTO getPage(PageDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), Sort.by("createDate").descending());
        Page<BoardReply> boardReplies = boardReplyRepository.findAll(pageable);

        // 게시글들을 BoardDetailResponseDTO로 변환하여 리스트에 담습니다.
        List<BoardReplyDetailResponseDTO> boardReplyDTOList = boardReplies.stream()
                .map(BoardReplyDetailResponseDTO::new)
                .collect(Collectors.toList());

        return BoardReplyListResponseDTO.builder()
                .replyCount((int) boardReplies.getTotalElements()) // 총 게시글 수
                .totalPages(boardReplies.getTotalPages()) // 총 페이지 수
                .pageInfo(new PageResponseDTO(boardReplies))
                .boardReplies(boardReplyDTOList)
                .build();
    }


    public void registBoardReply(BoardReplyWriteRequestDTO requestDTO, TokenUserInfo userInfo) {

    }


    public void updateBoardReply(BoardReplyModifyRequestDTO requestDTO) {

    }


    public void deleteBoardReply(long replyNo, TokenUserInfo userInfo) {

    }

}
