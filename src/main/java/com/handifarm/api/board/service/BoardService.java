package com.handifarm.api.board.service;

import com.handifarm.api.board.dto.request.BoardModifyRequestDTO;
import com.handifarm.api.board.dto.request.BoardWriteRequestDTO;
import com.handifarm.api.board.dto.response.BoardDetailResponseDTO;
import com.handifarm.api.board.dto.response.BoardListResponseDTO;
import com.handifarm.api.board.entity.Board;
import com.handifarm.api.board.repository.BoardRepository;
import com.handifarm.api.board.repository.BoardSearchRepositoryImpl;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService implements IBoardService {

    private final BoardRepository boardRepository;
    private final BoardSearchRepositoryImpl boardSearchRepositoryImpl;

    public BoardListResponseDTO retrieve() {

        List<Board> entityList = boardRepository.findAll();

        List<BoardDetailResponseDTO> dtoList = entityList.stream()
                .map(BoardDetailResponseDTO::new)
                .collect(Collectors.toList());

        return BoardListResponseDTO.builder()
                .boards(dtoList)
                .build();
    }



    public List<Board> searchBoards(String category, String condition, String searchWord) {
        return boardSearchRepositoryImpl.searchBoardList(category, condition, searchWord);
    }



    public void registBoard(BoardWriteRequestDTO requestDTO, TokenUserInfo userInfo) {
//        Board.Category category = Board.Category.valueOf(requestDTO.getCategory());
//        String title = requestDTO.getTitle();
//        String content = requestDTO.getContent();
//        String userNick = userInfo.getUserNick();
//
//        Board board = Board.builder()
//                .userNick(userNick)
//                .category(category)
//                .title(title)
//                .content(content)
//                .build();
        Board board = requestDTO.toEntity(userInfo);

        boardRepository.save(board);
        log.info("게시글 등록 완료");
    }

    public BoardDetailResponseDTO showDetailBoard(Long boardNo) {
        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        board.setViews(board.getViews() + 1);
        Board saved = boardRepository.save(board);

        BoardDetailResponseDTO boardDetailResponseDTO = new BoardDetailResponseDTO(saved);

        return boardDetailResponseDTO;
    }

    @Override
    public void updateBoard(BoardModifyRequestDTO requestDTO) {
        long boardNo = requestDTO.getBoardNo();
        String title = requestDTO.getTitle();
        String content = requestDTO.getContent();


        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        long existingBoardNo = board.getBoardNo();
        String existingTitle = board.getTitle();
        String existingCategory = String.valueOf(board.getCategory());
        String existingContent = board.getContent();

        board.setTitle(title);
        board.setContent(content);
    }


    @Override
    public void deleteBoard(long boardNo, TokenUserInfo userInfo) {
        // 게시글 조회
        Board board = boardRepository.findById(boardNo).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        // 게시글의 작성자와 로그인한 사용자의 userNick이 같은지 확인
        if (board.getUserNick().equals(userInfo.getUserNick())) {
            boardRepository.delete(board);
        } else {
            throw new RuntimeException("권한이 없습니다. 게시글 작성자만 삭제할 수 있습니다.");
        }
    }




    public BoardListResponseDTO getPage(PageDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), Sort.by("createDate").descending());
        Page<Board> boards = boardRepository.findAll(pageable);

        // NOTICE 카테고리의 게시물을 먼저 따로 빼고, 나머지 게시물은 createDate 순으로 정렬하여 합칩니다.
        List<Board> noticeBoards = new ArrayList<>();
        List<Board> otherBoards = new ArrayList<>();

        for (Board board : boards.getContent()) {
            if (board.getCategory().equals(Board.Category.NOTICE)) {
                noticeBoards.add(board);
            } else {
                otherBoards.add(board);
            }
        }

        // 나머지 게시물들을 createDate를 기준으로 내림차순 정렬합니다.
        otherBoards.sort(Comparator.comparing(Board::getCreateDate).reversed());

        // NOTICE 게시물을 가장 앞에 추가하여 하나의 리스트로 합칩니다.
        List<Board> combinedBoards = new ArrayList<>();
        combinedBoards.addAll(noticeBoards);
        combinedBoards.addAll(otherBoards);

        // 게시글들을 BoardDetailResponseDTO로 변환하여 리스트에 담습니다.
        List<BoardDetailResponseDTO> detailList = combinedBoards.stream()
                .map(BoardDetailResponseDTO::new)
                .collect(Collectors.toList());

        return BoardListResponseDTO.builder()
                .postCount((int) boards.getTotalElements()) // 총 게시글 수
                .totalPages(boards.getTotalPages()) // 총 페이지 수
                .pageInfo(new PageResponseDTO(boards))
                .boards(detailList)
                .build();
    }

    // 조회수 증가 로직
    public void increaseViews(Long boardNo) {
        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        // 조회수 증가
        board.setViews(board.getViews() + 1);

        // 수정된 게시글 저장
        boardRepository.save(board);
    }




}