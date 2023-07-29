package com.handifarm.api.snsBoard.service;

import com.handifarm.api.snsBoard.dto.request.SnsBoardCreateRequestDTO;
import com.handifarm.api.snsBoard.dto.request.SnsBoardModifyRequestDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardDetailListResponseDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardListResponseDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardResponseDTO;
import com.handifarm.api.snsBoard.entity.SnsBoard;
import com.handifarm.api.snsBoard.entity.SnsHashTag;
import com.handifarm.api.snsBoard.entity.SnsImg;
import com.handifarm.api.snsBoard.repository.SnsBoardRepository;
import com.handifarm.api.snsBoard.repository.SnsHashTagRepository;
import com.handifarm.api.snsBoard.repository.SnsImgRepository;
import com.handifarm.api.util.page.PageDTO;
import com.handifarm.api.util.page.PageResponseDTO;
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

import java.io.IOException;
import java.util.List;
import java.util.UUID;
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
    private final String serviceName = "SNS";

    // SNS 게시글 목록
    @Override
    public SnsBoardListResponseDTO getSnsList(PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPage() - 1,
                pageDTO.getSize(),
                Sort.by("uploadTime").descending());

        Page<SnsBoard> snsBoards = snsBoardRepository.findAll(pageable);

        List<SnsBoard> snsBoardList = snsBoards.getContent();

        List<SnsBoardResponseDTO> snsBoardResponseList = snsBoardList.stream()
                .map(SnsBoardResponseDTO::new)
                .collect(Collectors.toList());

        PageResponseDTO<SnsBoard> pageResponseDTO = new PageResponseDTO<>(snsBoards);

        SnsBoardListResponseDTO responseDTO = new SnsBoardListResponseDTO();
        responseDTO.setCount(pageResponseDTO.getTotalCount());
        responseDTO.setPageInfo(pageResponseDTO);
        responseDTO.setSnsList(snsBoardResponseList);

        return responseDTO;
    }

    // SNS 게시글 조회
    @Override
    public SnsBoardDetailListResponseDTO getSns(final long snsNo, final String userNick) {
        List<SnsBoard> snsBoards = snsBoardRepository.findAllByUserNick(userNick);
        List<SnsBoardResponseDTO> snsResponseList = snsBoards.stream()
                .map(SnsBoardResponseDTO::new)
                .collect(Collectors.toList());

        return new SnsBoardDetailListResponseDTO(snsNo, snsResponseList);
    }

    // SNS 게시글 등록
    @Override
    public SnsBoardResponseDTO uploadSns(final TokenUserInfo userInfo,
                                         final SnsBoardCreateRequestDTO requestDTO,
                                         final List<MultipartFile> snsImgs) {
        // SNS 게시판은 사진이 필수 값이므로 검증 -> 컨트롤러에서 필수 값 처리
//        if (snsImgs == null || snsImgs.isEmpty()) {
//            throw new RuntimeException("게시물 사진이 업로드되지 않았습니다.");
//        }

        SnsBoard snsBoard = SnsBoard.builder()
                .userNick(userInfo.getUserNick())
                .content(requestDTO.getContent())
                .build();

        SnsBoard save = snsBoardRepository.save(snsBoard);

        List<String> hashTags = requestDTO.getHashTags();

        for (String hashTag : hashTags) {
            SnsHashTag snsHashTag = hashTagRepository.save(SnsHashTag.builder().hashTag(hashTag).build());
            // 데이터 일치를 위해 엔티티에도 편의 메서드로 추가
            snsBoard.addHashTag(snsHashTag);
        }

        // S3에 이미지 업로드
        List<String> uploadUrls = snsImgs.stream()
                .map(snsImg -> {
                    try {
                        String uuidFileName = UUID.randomUUID() + "_" + snsImg.getOriginalFilename();
                        // S3에 업로드 된 URL을 리턴
                        return s3Service.uploadToS3Bucket(snsImg.getBytes(), uuidFileName, serviceName);
                    } catch (IOException e) {
                        log.error("이미지 업로드에 실패하였습니다.", e);
                        throw new RuntimeException("이미지 업로드에 실패하였습니다.");
                    }
                })
                .collect(Collectors.toList());

        // DB에 S3 UploadUrl 추가
        uploadUrls.forEach(url -> {
            SnsImg savedSnsImg = snsImgRepository.save(SnsImg.builder()
                    .snsImgLink(url)
                    .build());
            // 데이터 일치를 위해 엔티티에도 편의 메서드로 추가
            snsBoard.addSnsImg(savedSnsImg);
        });

        return new SnsBoardResponseDTO(snsBoard);
    }

    // SNS 게시글 수정
    @Override
    public SnsBoardResponseDTO modifySns(final TokenUserInfo userInfo,
                                         final long snsNo,
                                         final SnsBoardModifyRequestDTO requestDTO,
                                         final List<MultipartFile> snsImgs) {
        return null;
    }

    // SNS 게시글 삭제
    @Override
    public void deleteSns(final TokenUserInfo userInfo, final long snsNo) {

    }
}
