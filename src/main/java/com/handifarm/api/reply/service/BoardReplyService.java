package com.handifarm.api.reply.service;

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
import org.webjars.NotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardReplyService {

    private final BoardReplyRepository boardReplyRepository;

    public BoardReplyListResponseDTO getPage(long boardNo, PageDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), Sort.by("createDate").descending());
        Page<BoardReply> boardReplies = boardReplyRepository.findByBoardNo(boardNo, pageable);

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


    public void registBoardReply(long boardNo, BoardReplyWriteRequestDTO requestDTO, TokenUserInfo userInfo) {

        BoardReply boardReply = requestDTO.toEntity(userInfo, boardNo);

        boardReplyRepository.save(boardReply);

        log.info("댓글 등록 성공");
        log.info(String.valueOf(boardReply));
    }


    public void updateBoardReply(BoardReplyModifyRequestDTO requestDTO) {
        long replyNo = requestDTO.getReplyNo();
        String reply = requestDTO.getReply();

        BoardReply boardReply = boardReplyRepository.findById(replyNo)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        // BoardReply 엔티티의 필드를 업데이트합니다.
        boardReply.setReply(reply);

        boardReplyRepository.save(boardReply);

        // 댓글 수정이 성공적으로 완료되었다면 로그를 출력합니다.
        log.info("댓글 수정 성공 - 댓글 번호: {}, 수정된 댓글 내용: {}", replyNo, reply);
    }


    public void deleteBoardReply(long replyNo, TokenUserInfo userInfo) {
        // 게시글 조회
        BoardReply boardReply = boardReplyRepository.findById(replyNo).orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));

        // 게시글의 작성자와 로그인한 사용자의 userNick이 같은지 확인
        if (boardReply.getUserNick().equals(userInfo.getUserNick())) {
            boardReplyRepository.delete(boardReply);
        } else {
            throw new RuntimeException("권한이 없습니다. 댓글 작성자만 삭제할 수 있습니다.");
        }
    }

}
