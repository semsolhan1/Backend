package com.handifarm.api.board.service;

import com.handifarm.api.board.dto.request.BoardModifyRequestDTO;
import com.handifarm.api.board.dto.request.BoardWriteRequestDTO;
import com.handifarm.api.board.dto.response.BoardDetailResponseDTO;
import com.handifarm.api.board.dto.response.BoardListResponseDTO;
import com.handifarm.api.board.entity.Board;
import com.handifarm.api.board.repository.BoardRepository;
import com.handifarm.api.user.entity.User;
import com.handifarm.api.user.repository.UserRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService implements IBoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public BoardListResponseDTO retrieve() {

        List<Board> entityList = boardRepository.findAll();

        List<BoardDetailResponseDTO> dtoList = entityList.stream()
                .map(BoardDetailResponseDTO::new)
                .collect(Collectors.toList());

        return BoardListResponseDTO.builder()
//                .postList(dtoList)
                .build();
    }

    @Override
    public BoardListResponseDTO retrieve(String userNick) {
        User user = getUser(userNick);

        List<Board> entityList = boardRepository.findAllByUser(user);

        List<BoardDetailResponseDTO> dtoList = entityList.stream()
                .map(BoardDetailResponseDTO::new)
                .collect(Collectors.toList());

        return BoardListResponseDTO.builder()
//                .postList(dtoList)
                .build();
    }
    private User getUser(String userNick) {
        User user = userRepository.findById(userNick).orElseThrow(
                () -> new RuntimeException("회원 정보가 없습니다.")
        );
        return user;
    }


    @Override
    public void registBoard(BoardWriteRequestDTO requestDTO, TokenUserInfo userInfo) {
        Board.Category category = Board.Category.valueOf(requestDTO.getCategory());
        String title = requestDTO.getTitle();
        String content = requestDTO.getContent();

        Board board = Board.builder()
                .category(category)
                .title(title)
                .content(content)
                .build();

        boardRepository.save(board);
        log.info("게시글 등록 완료");
    }

    public BoardDetailResponseDTO showDetailBoard(Long boardNo) {
        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        BoardDetailResponseDTO boardDetailResponseDTO = BoardDetailResponseDTO.builder()
                .boardNo(board.getBoardNo())
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory().toString())
                .createDate(board.getCreateDate())
                .updateDate(board.getUpdateDate())
                .build();

        return boardDetailResponseDTO;
    }

    @Override
    public void updateBoard(BoardModifyRequestDTO requestDTO) {
        long boardNo = requestDTO.getBoardNo();
        String title = requestDTO.getTitle();
        String content = requestDTO.getContent();

        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        board.setTitle(title);
        board.setContent(content);
    }


    @Override
    public void deleteBoard(long boardNo) {
        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        boardRepository.delete(board);
    }

    public BoardListResponseDTO getPage(PageDTO dto) {
        Pageable pageable = PageRequest.of(
                dto.getPage() -1,
                dto.getSize(),
                Sort.by("createDate").descending()
        );

        Page<Board> boards = boardRepository.findAll(pageable);

        List<Board> boardList = boards.getContent();

        List<BoardDetailResponseDTO> detailList
                = boardList.stream()
                            .map(board -> new BoardDetailResponseDTO(board))
                            .collect(Collectors.toList());

        return BoardListResponseDTO.builder()
                .postCount(detailList.size())
                .pageInfo(new PageResponseDTO(boards))
                .boards(detailList)
                .build();
    };

}
