package com.handifarm.api.snsBoard.service;

import com.handifarm.api.snsBoard.dto.request.SNSBoardCreateRequestDTO;
import com.handifarm.api.snsBoard.dto.request.SNSBoardModifyRequestDTO;
import com.handifarm.api.snsBoard.dto.response.SNSBoardListResponseDTO;
import com.handifarm.api.snsBoard.dto.response.SNSBoardResponseDTO;
import com.handifarm.api.util.page.PageDTO;
import com.handifarm.jwt.TokenUserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISnsBoardService {

    // SNS 게시글 목록
    SNSBoardListResponseDTO getSnsList(PageDTO pageDTO);

    // SNS 게시글 조회
    SNSBoardListResponseDTO getSns(long snsNo);

    // SNS 게시글 등록
    SNSBoardResponseDTO uploadSns(TokenUserInfo userInfo,
                                  SNSBoardCreateRequestDTO requestDTO,
                                  List<MultipartFile> snsImgs);

    // SNS 게시글 수정
    SNSBoardResponseDTO modifySns(TokenUserInfo userInfo,
                                  long snsNo,
                                  SNSBoardModifyRequestDTO requestDTO,
                                  List<MultipartFile> snsImgs);

    // SNS 게시글 삭제
    void deleteSns(TokenUserInfo userInfo, long snsNo);

}
