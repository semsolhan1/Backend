package com.handifarm.api.snsBoard.service;

import com.handifarm.api.snsBoard.dto.request.SnsBoardCreateRequestDTO;
import com.handifarm.api.snsBoard.dto.request.SnsBoardModifyRequestDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardDetailListResponseDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardListResponseDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardResponseDTO;
import com.handifarm.api.snsBoard.entity.*;
import com.handifarm.api.snsBoard.repository.*;
import com.handifarm.api.user.entity.User;
import com.handifarm.api.user.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final SnsBoardRepository snsBoardRepository;
    private final SnsHashTagRepository hashTagRepository;
    private final SnsImgRepository snsImgRepository;
    private final SnsLikeRepository likeRepository;
    private final SnsReplyRepository replyRepository;
    private final S3Service s3Service;
    private final String serviceName = "SNS";

    // SNS 전체 게시글 목록
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

    // SNS 유저 게시글 목록
    @Override
    public SnsBoardDetailListResponseDTO getSnsUserList(final long snsNo, final String userNick) {
        User user = userRepository.findByUserNick(userNick);

        List<SnsBoard> snsBoards = snsBoardRepository.findAllByUserNick(userNick);
        List<SnsBoardResponseDTO> snsResponseList = snsBoards.stream()
                .map(SnsBoardResponseDTO::new)
                .collect(Collectors.toList());

        return new SnsBoardDetailListResponseDTO(snsNo, user.getUserProfileImg(),snsResponseList);
    }

    // SNS 게시글 등록
//    @Async
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

        SnsBoard savedSnsBoard = snsBoardRepository.save(snsBoard);

        List<String> hashTags = requestDTO.getHashTags();

        for (String hashTag : hashTags) {
            SnsHashTag snsHashTag = hashTagRepository.save(SnsHashTag.builder().hashTag(hashTag).build());
            // 데이터 일치를 위해 엔티티에도 편의 메서드로 추가
            snsBoard.addHashTag(snsHashTag);
        }

//        // 비동기로 이미지 업로드 처리
//        List<ListenableFuture<String>> uploadFutures = snsImgs.stream()
//                .map(snsImg -> uploadImageAsync(snsImg, serviceName))
//                .collect(Collectors.toList());
//
//        // 비동기 업로드 작업이 모두 완료될 때까지 대기
//        List<String> uploadUrls = uploadFutures.stream()
//                .map(ListenableFuture::completable)
//                .map(CompletableFuture::join)
//                .collect(Collectors.toList());
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

        return new SnsBoardResponseDTO(savedSnsBoard);
    }

    // SNS 게시글 수정
    @Override
    public SnsBoardResponseDTO modifySns(final TokenUserInfo userInfo,
                                         final long snsNo,
                                         final SnsBoardModifyRequestDTO requestDTO) {
        SnsBoard snsBoard = snsBoardRepository.findById(snsNo)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 SNS 게시글 번호입니다."));

        if (!userInfo.getUserNick().equals(snsBoard.getUserNick())) throw new RuntimeException("권한이 없습니다.");

        snsBoard.setContent(requestDTO.getContent());

        List<String> existingHashTags = snsBoard.getExistingHashTags();
        List<String> newHashTags = requestDTO.getHashTags().stream()
                .filter(hashTag -> !existingHashTags.contains(hashTag))
                .collect(Collectors.toList());

        for (String newHashTag : newHashTags) {
            SnsHashTag snsHashTag = hashTagRepository.save(SnsHashTag.builder()
                    .hashTag(newHashTag)
                    .snsBoard(snsBoard)
                    .build());
            snsBoard.addHashTag(snsHashTag);
        }

        List<SnsHashTag> hashTagsToRemove = snsBoard.getHashTags().stream()
                .filter(hashTag -> !requestDTO.getHashTags().contains(hashTag.getHashTag()))
                .collect(Collectors.toList());

        for (SnsHashTag hashTag : hashTagsToRemove) {
            hashTagRepository.delete(hashTag); // DB에서 제거
            snsBoard.removeHashTag(hashTag); // 엔티티에서 제거
        }

        SnsBoard savedSnsBoard = snsBoardRepository.save(snsBoard);

        return new SnsBoardResponseDTO(savedSnsBoard);
    }

//    @Async
//    protected ListenableFuture<String> uploadImageAsync(final MultipartFile snsImg, final String serviceName) {
//        try {
//            // 임시 디렉토리 설정
//            String tempDirectoryPath = "C:/work/asyncTemp";
//            System.setProperty("java.io.tmpdir", tempDirectoryPath);
//
//            // 임시 디렉토리가 존재하지 않으면 생성
//            Path tempDirectory = Paths.get(tempDirectoryPath);
//            if (!Files.exists(tempDirectory)) {
//                Files.createDirectories(tempDirectory);
//            }
//
//            // 파일 저장할 경로 생성
//            String uuidFileName = UUID.randomUUID() + "_" + snsImg.getOriginalFilename();
//            String filePath = tempDirectoryPath + "/" + uuidFileName;
//            byte[] imageData = snsImg.getBytes();
//
//            // 이미지 파일을 생성된 경로에 저장
//            Files.write(Paths.get(filePath), imageData);
//
//            // S3에 업로드 된 URL을 리턴
//            return new AsyncResult<>(s3Service.uploadToS3Bucket(imageData, uuidFileName, serviceName));
//        } catch (IOException e) {
//            log.error("이미지 업로드에 실패하였습니다.", e);
//            throw new RuntimeException("이미지 업로드에 실패하였습니다.");
//        }
//    }

    // SNS 게시글 삭제
    @Override
    public void deleteSns(final TokenUserInfo userInfo, final long snsNo) {
        SnsBoard snsBoard = snsBoardRepository.findById(snsNo)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 SNS 게시글 번호입니다."));

        if (!userInfo.getUserNick().equals(snsBoard.getUserNick())) throw new RuntimeException("권한이 없습니다.");

        // S3 버킷에 업로드 된 이미지 먼저 삭제
        snsBoard.getSnsImgs().stream()
                .map(SnsImg::getSnsImgLink)
                .forEach(s3Service::deleteFromS3Bucket);

        // 게시글 삭제
        snsBoardRepository.delete(snsBoard);
    }

    // SNS 게시글 댓글 등록
    @Override
    public SnsBoardDetailListResponseDTO registReply(final TokenUserInfo userInfo, final long snsNo, final String reply) {
        SnsBoard snsBoard = snsBoardRepository.findById(snsNo)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 SNS 게시글 번호입니다."));

        replyRepository.save(SnsReply.builder().reply(reply).snsBoard(snsBoard).writer(userInfo.getUserNick()).build());

        User user = userRepository.findByUserNick(snsBoard.getUserNick()); // 댓글 게시글의 User

        List<SnsBoard> snsBoards = snsBoardRepository.findAllByUserNick(snsBoard.getUserNick());

        List<SnsBoardResponseDTO> snsResponseList = snsBoards.stream()
                .map(SnsBoardResponseDTO::new)
                .collect(Collectors.toList());

        return new SnsBoardDetailListResponseDTO(snsNo, user.getUserProfileImg(), snsResponseList);
    }

    // SNS 게시글 댓글 수정
    @Override
    public SnsBoardDetailListResponseDTO modifyReply(final TokenUserInfo userInfo, final long snsNo,
                                                     final long replyNo, final String reply) {
        SnsReply snsReply = replyRepository.findById(replyNo)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 SNS 댓글 번호입니다."));

        if (!userInfo.getUserNick().equals(snsReply.getWriter())) throw new RuntimeException("권한이 없습니다.");

        User user = userRepository.findByUserNick(snsReply.getSnsBoard().getUserNick()); // 댓글 게시글의 User

        snsReply.setReply(reply);
        SnsReply savedSnsReply = replyRepository.save(snsReply);

        List<SnsBoard> snsBoards = snsBoardRepository.findAllByUserNick(savedSnsReply.getSnsBoard().getUserNick());

        List<SnsBoardResponseDTO> snsResponseList = snsBoards.stream()
                .map(SnsBoardResponseDTO::new)
                .collect(Collectors.toList());

        return new SnsBoardDetailListResponseDTO(snsNo, user.getUserProfileImg(), snsResponseList);
    }

    // SNS 게시글 댓글 삭제
    @Override
    public SnsBoardDetailListResponseDTO deleteReply(final TokenUserInfo userInfo, final long snsNo, final long replyNo) {
        SnsReply snsReply = replyRepository.findById(replyNo)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 SNS 댓글 번호입니다."));

        if (!userInfo.getUserNick().equals(snsReply.getWriter())) throw new RuntimeException("권한이 없습니다.");

        User user = userRepository.findByUserNick(snsReply.getSnsBoard().getUserNick()); // 댓글의 게시글 User

        replyRepository.delete(snsReply);

        replyRepository.flush();

        List<SnsBoard> snsBoards = snsBoardRepository.findAllByUserNick(user.getUserNick());

        List<SnsBoardResponseDTO> snsResponseList = snsBoards.stream()
                .map(SnsBoardResponseDTO::new)
                .collect(Collectors.toList());

        return new SnsBoardDetailListResponseDTO(snsNo, user.getUserProfileImg(), snsResponseList);
    }

    // SNS 게시글 좋아요
    @Override
    public SnsBoardDetailListResponseDTO snsLike(final TokenUserInfo userInfo, final long snsNo) {
        User user = userRepository.findById(userInfo.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 User 정보입니다."));

        SnsBoard snsBoard = snsBoardRepository.findById(snsNo)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 SNS 게시글 번호입니다."));

        if (!likeRepository.existsBySnsBoardSnsNoAndUserUserNick(snsNo, userInfo.getUserNick())) {
            // 좋아요 추가
            SnsLike like = likeRepository.save(SnsLike.builder().snsBoard(snsBoard).user(user).build());
            snsBoard.getLikes().add(like); // 좋아요 관련 리스트에 추가
        } else {
            // 좋아요 취소
            likeRepository.delete(likeRepository.findBySnsBoardSnsNoAndUserUserNick(snsNo, userInfo.getUserNick()));
            snsBoard.getLikes().removeIf(like -> like.getUser().getUserNick().equals(userInfo.getUserNick())); // 좋아요 관련 리스트에서 제거
        }

        List<SnsBoard> snsBoards = snsBoardRepository.findAllByUserNick(userInfo.getUserNick());

        List<SnsBoardResponseDTO> snsResponseList = snsBoards.stream()
                .map(SnsBoardResponseDTO::new)
                .collect(Collectors.toList());

        return new SnsBoardDetailListResponseDTO(snsNo, user.getUserProfileImg(), snsResponseList);
    }

}
