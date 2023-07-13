package com.handifarm.cboard.service;

import com.handifarm.cboard.dto.page.PageDTO;
import com.handifarm.cboard.dto.page.PageResponseDTO;
import com.handifarm.cboard.dto.request.CboardCreateRequestDTO;
import com.handifarm.cboard.dto.request.CboardModifyrequestDTO;
import com.handifarm.cboard.dto.response.CboardDetailResponseDTO;
import com.handifarm.cboard.dto.response.CboardListResponseDTO;
import com.handifarm.cboard.entity.Cboard;
import com.handifarm.cboard.entity.HashTag;
import com.handifarm.cboard.repository.CboardRepository;
import com.handifarm.cboard.repository.HashTagRepository;
import com.handifarm.recontent.dto.page.RecontentPageDTO;
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

    @Autowired
    private final RecontentService recontentService;

    @Value("${upload.path}")
    private String uploadRootPath;


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
                    List<Recontent> recontentList = (List<Recontent>) recontentService.contentretrieve(recontentId ,new RecontentPageDTO());
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


    public CboardListResponseDTO create(CboardCreateRequestDTO dto, final String uploadedFilePath)
            throws RuntimeException, IllegalStateException{


        Cboard cboard = dto.toEntity(uploadedFilePath);

        List<String> hashTags = dto.getHashTags();

        List<Recontent> recontents = dto.getRecontents();

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


        PageDTO pageDTO = new PageDTO();

        return retrieve(pageDTO);
    }

    public CboardListResponseDTO delete(String cboardid, int currentPage) throws NotFoundException {

        Cboard deletedCboard = cboardRepository.findById(cboardid)
                .orElseThrow(() -> new NotFoundException("해당 id에 해당하는 데이터를 찾을 수 없습니다. - id: " + cboardid));
        cboardRepository.deleteById(cboardid);

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

    public CboardListResponseDTO update(CboardModifyrequestDTO dto, int page ,final String uploadedFilePath) {

        Cboard cboardEntity = getCboard(dto.getId());

        if(!(dto.getTitle() == null)){
        cboardEntity.setTitle(dto.getTitle());
        }
        if(!(dto.getContent() == null)){
        cboardEntity.setContent(dto.getContent());
        }
        if(!(dto.getFileUp() == null)){
        cboardEntity.setFileUp(uploadedFilePath);
        }

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
                .map(board -> new CboardDetailResponseDTO(board))
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



    public String uploadProfileImage(MultipartFile profileImg) throws IOException  {

        File rootDirectory = new File(uploadRootPath);
        if(!rootDirectory.exists()){
            rootDirectory.mkdir();
        }

        String FileName = UUID.randomUUID()
                + "_" + profileImg.getOriginalFilename();

        File uploadFile = new File(uploadRootPath + "/" + FileName);
        profileImg.transferTo(uploadFile);

        return  FileName;

    }

}
