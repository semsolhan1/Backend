package com.handifarm.api.board.repository;

import com.handifarm.api.board.dto.request.BoardWriteRequestDTO;
import com.handifarm.api.board.service.BoardService;
import com.handifarm.jwt.TokenUserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class BoardRepositoryTest {

    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("게시글 100개 등록")
    void registTest() {
        //given
        for (int i = 1; i <= 100; i++) {
            String category = "INFORMATION";
            String title = "정보 " + i;
            String content = "정보내용 " + i;

            BoardWriteRequestDTO requestDTO = new BoardWriteRequestDTO();
            requestDTO.setCategory(category);
            requestDTO.setTitle(title);
            requestDTO.setContent(content);

            TokenUserInfo userInfo = new TokenUserInfo();
        //when
            boardService.registBoard(requestDTO, userInfo);
        //then
    }

    }


}