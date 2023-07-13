package com.handifarm.api.board.Service;


import com.handifarm.api.board.dto.*;
import com.handifarm.api.board.entity.Board;
import com.handifarm.api.board.entity.Category;
import com.handifarm.api.board.repository.BoardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

  private final BoardRepository boardRepository;


  public BoardListResponseDTO getBoards(BoardDTO dto) {

    //pageable 객체 생성
    Pageable pageable = PageRequest.of(
            dto.getPage() - 1, // 0을 1페이지로 인식
            dto.getSize(),
            Sort.by("createDate").descending()
    );

    //데이터베이스에서 게시물 목록 조회
    Page<Board> boards = boardRepository.findAll(pageable);

    // 게시물 정보만 꺼내기
    List<Board> boardList = boards.getContent();

    List<BoardDetailResponseDTO> detailList
            = boardList.stream()
            .map(board -> new BoardDetailResponseDTO(board))
              .collect(Collectors.toList());

    //DB에서 조회한 정보(ENTITY)를 JSON 형태에 맞는 DTO로 변환
    return BoardListResponseDTO.builder()
            .count(detailList.size())
            .pageInfo(new PageResponseDTO(boards))
            .boards(detailList)
            .build();
  }

  public BoardDetailResponseDTO getDetail(long board_no) {

    Board boardEntity = getBoard(board_no);

    return new BoardDetailResponseDTO(boardEntity);

  }

  private Board getBoard(long board_no) {
    Board boardEntity = boardRepository.findById(board_no)
            .orElseThrow(
                    () -> new RuntimeException(board_no + "번 게시물이 존재하지 않습니다.")
            );
    return boardEntity;
  }
 public BoardDetailResponseDTO insert(final BoardCreateDTO dto)
   throws RuntimeException {
   Board saved = boardRepository.save(dto.toEntity());

 return new BoardDetailResponseDTO(saved);
 }

 // 게시물 저장
  public BoardDetailResponseDTO modify(boardModifyDTO dto) {

    // 수정 전 데이터를 조회
    Board boardEntity = getBoard(dto.getBoardNo());

    //수정 시작
    boardEntity.setTitle(dto.getTitle());
    boardEntity.setContent(dto.getContent());

    //수정 완료
    Board modifiedBoard = boardRepository.save(boardEntity);

    return new BoardDetailResponseDTO(modifiedBoard);
  }


  public void delete(long boardNo) {
     boardRepository.deleteById(boardNo);
  }


}
