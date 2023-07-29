package com.handifarm.api.snsBoard.service;

import com.handifarm.api.snsBoard.dto.request.SnsBoardCreateRequestDTO;
import com.handifarm.api.snsBoard.dto.request.SnsBoardModifyRequestDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardDetailListResponseDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardListResponseDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardResponseDTO;
import com.handifarm.api.util.page.PageDTO;
import com.handifarm.jwt.TokenUserInfo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISnsBoardService {

    // SNS 게시글 목록
    SnsBoardListResponseDTO getSnsList(PageDTO pageDTO);

    // SNS 게시글 조회
    SnsBoardDetailListResponseDTO getSns(long snsNo, String userNick);

    // SNS 게시글 등록
    SnsBoardResponseDTO uploadSns(TokenUserInfo userInfo, SnsBoardCreateRequestDTO requestDTO);

    // SNS 게시글 수정
    SnsBoardResponseDTO modifySns(TokenUserInfo userInfo, long snsNo, SnsBoardModifyRequestDTO requestDTO);

    // SNS 게시글 삭제
    void deleteSns(TokenUserInfo userInfo, long snsNo);

}
