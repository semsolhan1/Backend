package com.handifarm.api.snsBoard.service;

import com.handifarm.api.snsBoard.dto.request.SNSBoardCreateRequestDTO;
import com.handifarm.api.snsBoard.dto.request.SNSBoardModifyRequestDTO;
import com.handifarm.api.snsBoard.dto.response.SNSBoardListResponseDTO;
import com.handifarm.api.snsBoard.dto.response.SNSBoardResponseDTO;
import com.handifarm.api.snsBoard.entity.SnsBoard;
import com.handifarm.api.snsBoard.repository.SnsBoardRepository;
import com.handifarm.api.snsBoard.repository.SnsHashTagRepository;
import com.handifarm.api.snsBoard.repository.SnsImgRepository;
import com.handifarm.api.util.page.PageDTO;
import com.handifarm.aws.S3Service;
import com.handifarm.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SnsBoardService implements ISnsBoardService {

    private final SnsBoardRepository snsBoardRepository;
    private final SnsHashTagRepository hashTagRepository;
    private final SnsImgRepository snsImgRepository;
    private final S3Service s3Service;

    // SNS 게시글 목록
    @Override
    public SNSBoardListResponseDTO getSnsList(final PageDTO pageDTO) {

        Pageable pageable = PageRequest.of(pageDTO.getPage() - 1,
                pageDTO.getSize(),
                Sort.by("uploadTime").descending());

        Page<SnsBoard> snsBoards = snsBoardRepository.findAll(pageable);

        List<SnsBoard> snsBoardList = snsBoards.getContent();

//        snsBoards.stream().map(SNSBoardResponseDTO::new).collect(Collectors.toList())

        return null;
    }

    // SNS 게시글 조회
    @Override
    public SNSBoardListResponseDTO getSns(final long snsNo) {
        return null;
    }

    // SNS 게시글 등록
    @Override
    public SNSBoardResponseDTO uploadSns(final TokenUserInfo userInfo,
                                         final SNSBoardCreateRequestDTO requestDTO,
                                         final List<MultipartFile> snsImgs) {



        return null;
    }

    // SNS 게시글 수정
    @Override
    public SNSBoardResponseDTO modifySns(final TokenUserInfo userInfo,
                                         final long snsNo,
                                         final SNSBoardModifyRequestDTO requestDTO,
                                         final List<MultipartFile> snsImgs) {
        return null;
    }

    // SNS 게시글 삭제
    @Override
    public void deleteSns(final TokenUserInfo userInfo, final long snsNo) {

    }
}
