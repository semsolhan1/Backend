package com.handifarm.cboard.service;

import com.handifarm.api.market.entity.ItemImg;
import com.handifarm.api.market.entity.MarketItem;
import com.handifarm.aws.S3Service;
import com.handifarm.cboard.dto.page.PageDTO;
import com.handifarm.cboard.dto.page.PageResponseDTO;
import com.handifarm.cboard.dto.request.CboardCreateRequestDTO;
import com.handifarm.cboard.dto.request.CboardModifyrequestDTO;
import com.handifarm.cboard.dto.response.CboardDetailResponseDTO;
import com.handifarm.cboard.dto.response.CboardListResponseDTO;
import com.handifarm.cboard.entity.BoardImg;
import com.handifarm.cboard.entity.Cboard;
import com.handifarm.cboard.entity.HashTag;
import com.handifarm.cboard.repository.BoardImgRepository;
import com.handifarm.cboard.repository.CboardRepository;
import com.handifarm.cboard.repository.HashTagRepository;
import com.handifarm.recontent.dto.response.RecontentDetailResponseDTO;
import com.handifarm.recontent.entity.Recontent;
import com.handifarm.recontent.repository.RecontentRepository;
import com.handifarm.recontent.service.RecontentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CboardService {

    private final CboardRepository cboardRepository;

    private final HashTagRepository hashTagRepository;

    private final RecontentRepository recontentRepository;

    private final BoardImgRepository boardImgRepository;

    private final S3Service s3Service;

    @Autowired
    private final RecontentService recontentService;


    public CboardListResponseDTO retrieve(PageDTO dto) {


        Pageable pageable = PageRequest.of(
                dto.getPage()-1,
                dto.getSize(),
                Sort.by("boardTime").descending()
        );

        Page<Cboard> entityList = cboardRepository.findAll(pageable);

        List<Cboard> cboardList = entityList.getContent();


        List<CboardDetailResponseDTO> dtoList = cboardList.stream()
                .map(board -> {
                    String recontentId = board.getCboardId();
                    Pageable recontentPageable = PageRequest.of(0, 10, Sort.by("recontentOrder").ascending());
                    Page<Recontent> recontentList = recontentRepository.findByCboard_CboardId(recontentId,recontentPageable);
                    List<RecontentDetailResponseDTO> recontentDTOList = recontentList.stream()
                            .map(content -> new RecontentDetailResponseDTO(content))
                            .collect(Collectors.toList());
                    return new CboardDetailResponseDTO(board, recontentDTOList);
                })
                .collect(Collectors.toList());
        return CboardListResponseDTO.builder()
                .count(dtoList.size())
                .pageInfo(new PageResponseDTO(entityList))
                .board(dtoList)
                .build();
    }


    public CboardListResponseDTO create(CboardCreateRequestDTO dto, List<MultipartFile> itemImgs)
            throws RuntimeException, IllegalStateException{


        Cboard cboard = dto.toEntity();

        List<String> hashTags = dto.getHashTags();

        Cboard saved = cboardRepository.save(cboard);

        if(hashTags != null && hashTags.size() > 0){
            hashTags.forEach(hashtag -> {
                HashTag savedTag = hashTagRepository.save(
                    HashTag.builder()
                            .hashName(hashtag)
                            .cboard(saved)
                            .build()
                );
                saved.addHashTag(savedTag);
            });
        }
        String SNS = "SNS";
        if(itemImgs != null && !itemImgs.isEmpty()) {
            List<String> uploadUrls = itemImgs.stream()
                    .map(itemImg -> {
                        try {
                            String uuidFileName = UUID.randomUUID() + "_" + itemImg.getOriginalFilename();
                            String uploadUrl = s3Service.uploadToS3Bucket(itemImg.getBytes(), uuidFileName, SNS);
                            return uploadUrl;
                        } catch (IOException e) {
                            throw new RuntimeException("이미지 업로드에 실패하였습니다.", e);
                        }
                    })
                    .collect(Collectors.toList());
            uploadUrls.forEach(url -> {
                BoardImg savedItemImg = boardImgRepository.save(
                        BoardImg.builder()
                                .imgLink(url)
                                .cboard(saved)
                                .build());
                saved.addItemImg(savedItemImg);
            });
        }

        PageDTO pageDTO = new PageDTO();

        return retrieve(pageDTO);
    }

    public CboardListResponseDTO delete(CboardModifyrequestDTO dto, int currentPage) throws NotFoundException {

        Cboard deletedCboard = cboardRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("해당 id에 해당하는 데이터를 찾을 수 없습니다. - id: " + dto.getId()));

        // 작성자 검증
        if (!deletedCboard.getWriter().equals(dto.getWriter())) {
            throw new IllegalStateException("해당 게시물을 삭제할 권한이 없습니다.");
        }

        // MarketItem에 연결된 이미지들을 가져와서 삭제합니다.
        List<BoardImg> itemImgs = deletedCboard.getItemImgs();
        for (BoardImg itemImg : itemImgs) {
            try {
                // S3 버킷에서 이미지 삭제
                s3Service.deleteFromS3Bucket(itemImg.getImgLink());
                // 데이터베이스에서 이미지 삭제
                boardImgRepository.delete(itemImg);
            } catch (Exception e) {
                log.error("이미지 삭제 실패 - itemImg: {}, err: {}", itemImg.getImgLink(), e.getMessage());
                throw new RuntimeException("이미지 삭제 도중 예외가 발생했습니다.");
            }
        }

        try {
            cboardRepository.deleteById(dto.getId());
        } catch (Exception e) {
            log.error("존재하지 않는 게시글 번호로 삭제가 실패했습니다. - itemNo : {}, err : {}", dto.getId(), e.getMessage());
            throw new RuntimeException("게시글이 존재하지 않아 삭제에 실패했습니다.");
        }


        // 삭제된 게시물의 이전 게시물 조회
        Cboard previousCboard = cboardRepository.findFirstByBoardTimeLessThanOrderByBoardTimeDesc(deletedCboard.getBoardTime());
        // 삭제된 게시물의 다음 게시물 조회
        Cboard nextCboard = cboardRepository.findFirstByBoardTimeGreaterThanOrderByBoardTimeAsc(deletedCboard.getBoardTime());


        // 현재 페이지 조회
        int pageSize = 10; // 페이지 사이즈
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("boardTime").descending()); // 예시로 첫 번째 페이지 조회
        Page<Cboard> pageData = cboardRepository.findAll(pageable);

        List<Cboard> cboardList = pageData.getContent();
        List<CboardDetailResponseDTO> dtoList = cboardList.stream()
                .map(board -> new CboardDetailResponseDTO(board))
                .collect(Collectors.toList());

        // 페이지 정보 동적 갱신
        PageResponseDTO pageResponseDTO = new PageResponseDTO(pageData);
        pageResponseDTO.setCurrentPage(currentPage); // 현재 페이지 번호 갱신

        // board가 빈 값인 경우
        if (cboardList.isEmpty() && currentPage > 1) {
            // 이전 페이지로 이동
            currentPage--;
            pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("boardTime").descending());
            pageData = cboardRepository.findAll(pageable);
            cboardList = pageData.getContent();
            dtoList = cboardList.stream()
                    .map(board -> new CboardDetailResponseDTO(board))
                    .collect(Collectors.toList());
            pageResponseDTO = new PageResponseDTO(pageData);
            pageResponseDTO.setCurrentPage(currentPage);

            // board가 다시 비어 있는 경우
            if (cboardList.isEmpty()) {
                // 첫 페이지로 이동
                currentPage = 1;
                pageable = PageRequest.of(0, pageSize, Sort.by("boardTime").descending());
                pageData = cboardRepository.findAll(pageable);
                cboardList = pageData.getContent();
                dtoList = cboardList.stream()
                        .map(board -> new CboardDetailResponseDTO(board))
                        .collect(Collectors.toList());
                pageResponseDTO = new PageResponseDTO(pageData);
                pageResponseDTO.setCurrentPage(currentPage);
            }
        }

        CboardDetailResponseDTO previousOrNextCboard = null;
        if (previousCboard != null) {
            previousOrNextCboard = new CboardDetailResponseDTO(previousCboard);
        } else if (nextCboard != null) {
            previousOrNextCboard = new CboardDetailResponseDTO(nextCboard);
        }
        return CboardListResponseDTO.builder()
                .count(dtoList.size())
                .pageInfo(pageResponseDTO)
                .board(dtoList)
                .previousCboard(previousOrNextCboard)
                .build();
    }

    public CboardListResponseDTO update(CboardModifyrequestDTO dto, int page ,List<MultipartFile> itemImgs) {

        Cboard cboardEntity = getCboard(dto.getId());

        if(!(dto.getWriter() == null)){
            if (!dto.getWriter().equals(cboardEntity.getWriter())) {
                throw new IllegalStateException("작성자와 일치하지 않아 게시글을 수정할 수 없습니다.");
            }
            cboardEntity.setWriter(dto.getWriter());
        }
        if(!(dto.getContent() == null)){
        cboardEntity.setContent(dto.getContent());
        }

        // 이미지 링크를 처리합니다.
        List<BoardImg> existingItemImgs = cboardEntity.getItemImgs();
        List<String> newImgLinks = dto.getImgLinks();

        // 삭제된 이미지를 찾아 S3 버킷과 데이터베이스에서 삭제합니다.
        List<BoardImg> removedItemImgs = new ArrayList<>();
        for (BoardImg existingItemImg : existingItemImgs) {
            if (!newImgLinks.contains(existingItemImg.getImgLink())) {
                // S3 버킷에서 이미지 삭제
                s3Service.deleteFromS3Bucket(existingItemImg.getImgLink());
                // 데이터베이스에서 이미지 삭제
                boardImgRepository.delete(existingItemImg);
                // 동기화를 위해 삭제된 파일 목록 추가
                removedItemImgs.add(existingItemImg);
            }
        }

        cboardEntity.getItemImgs().removeAll(removedItemImgs);

        // 새로운 이미지를 데이터베이스와 S3 버킷에 추가합니다.
        addImgsToDBAndS3(itemImgs, cboardEntity);

        Cboard saved = cboardRepository.save(cboardEntity);


        if(!(dto.getHashTags() == null)){

            // 기존의 해시태그 삭제
            List<HashTag> existingTags = hashTagRepository.findByCboard(cboardEntity);
            hashTagRepository.deleteAll(existingTags);
            cboardEntity.getHashTags().clear();

            List<String> hashTags = dto.getHashTags();

            if(hashTags != null && !hashTags.isEmpty()){
                for (String hashtag : hashTags) {
                    HashTag savedTag = hashTagRepository.save(
                            HashTag.builder()
                                    .hashName(hashtag)
                                    .cboard(saved)
                                    .build()

                    );

                    saved.addHashTag(savedTag);
                }
            }

        }

        Cboard modifiedCboard = cboardRepository.findByCboardId(dto.getId());

        CboardDetailResponseDTO modified = new CboardDetailResponseDTO(modifiedCboard);

        // 이전 페이지 조회
        Pageable pageable = PageRequest.of(page -1, 10, Sort.by("boardTime").descending());
        Page<Cboard> pageData = cboardRepository.findAll(pageable);


        List<Cboard> cboardList = pageData.getContent();
        List<CboardDetailResponseDTO> dtoList = cboardList.stream()
                .map(board -> {
                    Page<Recontent> recontentPage = recontentRepository.findByCboard_CboardId(board.getCboardId(),Pageable.unpaged());
                    List<Recontent> recontentList = recontentPage.getContent();
                    List<RecontentDetailResponseDTO> recontentDTOList = recontentList.stream()
                            .map(content -> new RecontentDetailResponseDTO(content))
                            .collect(Collectors.toList());
                    return new CboardDetailResponseDTO(board, recontentDTOList);
                })
                .collect(Collectors.toList());

        // 페이지 정보 동적 갱신
        PageResponseDTO pageResponseDTO = new PageResponseDTO(pageData);
        pageResponseDTO.setCurrentPage(page); // 현재 페이지 번호 갱신

        return CboardListResponseDTO.builder()
                .count(dtoList.size())
                .pageInfo(pageResponseDTO)
                .board(dtoList)
                .previousCboard(modified)
                .build();
    }

    private Cboard getCboard(String id) {

        log.info("id 체크: {}" , id);

        Cboard cboardEntity = cboardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + "번 게시글은 존재하지 않습니다."));
        return cboardEntity;
    }


    public CboardDetailResponseDTO getBoardById(String cboardId) {
        Cboard cboardEntity = getCboard(cboardId);

        // CboardEntity를 CboardDetailResponseDTO로 변환
        CboardDetailResponseDTO responseDTO = new CboardDetailResponseDTO();
        responseDTO.setId(cboardEntity.getCboardId());
        responseDTO.setWriter(cboardEntity.getWriter());
        responseDTO.setContent(cboardEntity.getContent());
        responseDTO.setBoardTime(cboardEntity.getBoardTime());

        List<String> hashTags = cboardEntity.getHashTags()
                .stream()
                .map(HashTag::getHashName)
                .collect(Collectors.toList());
        responseDTO.setHashTags(hashTags);

        List<RecontentDetailResponseDTO> recontentDTOList = cboardEntity.getRecontents()
                .stream()
                .map(recontent -> new RecontentDetailResponseDTO(recontent))
                .sorted(Comparator.comparing(RecontentDetailResponseDTO::getRecontentOrder))
                .collect(Collectors.toList());
        responseDTO.setRecontentDTOList(recontentDTOList);

        List<String> imgLinks = cboardEntity.getItemImgs()
                .stream()
                .map(BoardImg::getImgLink)
                .collect(Collectors.toList());
        responseDTO.setImgLinks(imgLinks);

        responseDTO.setLikeCount(cboardEntity.getLikes().size());

        return responseDTO;
    }

    // 이미지 List DB와 S3 버킷에 추가하는 메서드
    private void addImgsToDBAndS3(List<MultipartFile> itemImgs, Cboard cboard) {
        String serviceName = "SNS";

        if (itemImgs != null && !itemImgs.isEmpty()) {
            List<String> uploadUrls = itemImgs.stream()
                    .map(itemImg -> {
                        try {
                            String uuidFileName = UUID.randomUUID() + "_" + itemImg.getOriginalFilename();
                            String uploadUrl = s3Service.uploadToS3Bucket(itemImg.getBytes(), uuidFileName, serviceName);
                            return uploadUrl;
                        } catch (IOException e) {
                            log.error("이미지 업로드에 실패하였습니다.", e);
                            throw new RuntimeException("이미지 업로드에 실패하였습니다.");
                        }
                    })
                    .collect(Collectors.toList());

            uploadUrls.forEach(url -> {
                BoardImg savedItemImg = boardImgRepository.save(BoardImg.builder()
                        .imgLink(url)
                        .cboard(cboard)
                        .build());
                cboard.addItemImg(savedItemImg);
            });
        }
    }
}
