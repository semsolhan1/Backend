package com.handifarm.api.board.service;

import com.handifarm.api.board.dto.request.BoardModifyRequestDTO;
import com.handifarm.api.board.dto.request.BoardWriteRequestDTO;
import com.handifarm.api.board.dto.response.BoardDetailResponseDTO;
import com.handifarm.api.board.dto.response.BoardListResponseDTO;
import com.handifarm.api.board.entity.Board;
import com.handifarm.api.board.repository.BoardRepository;
import com.handifarm.api.user.entity.User;
import com.handifarm.api.user.repository.UserRepository;
import com.handifarm.jwt.TokenUserInfo;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                .postList(dtoList)
                .build();
    }

    @Override
    public BoardListResponseDTO retrieve(String userId) {
        User user = getUser(userId);

        List<Board> entityList = boardRepository.findAllByUser(user);

        List<BoardDetailResponseDTO> dtoList = entityList.stream()
                .map(BoardDetailResponseDTO::new)
                .collect(Collectors.toList());

        return BoardListResponseDTO.builder()
                .postList(dtoList)
                .build();
    }
    private User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("회원 정보가 없습니다.")
        );
        return user;
    }


    @Override
    public void registBoard(BoardWriteRequestDTO requestDTO, TokenUserInfo userInfo) {
        String userId = requestDTO.getUserId();
        Board.Category category = Board.Category.valueOf(requestDTO.getCategory());
        String title = requestDTO.getTitle();
        String content = requestDTO.getContent();

        User user = getUser(userId);

        Board board = Board.builder()
                .user(user)
                .category(category)
                .title(title)
                .content(content)
                .build();

        boardRepository.save(board);
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
}
