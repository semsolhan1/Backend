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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                .map(board -> new CboardDetailResponseDTO(board))
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

        PageDTO pageDTO = new PageDTO();

        return retrieve(pageDTO);
    }

    public CboardListResponseDTO delete(String cboardid) throws NotFoundException {

        try {
            Cboard deletedCboard = cboardRepository.findById(cboardid)
                    .orElseThrow(() -> new NotFoundException("해당 id에 해당하는 데이터를 찾을 수 없습니다. - id: " + cboardid));
            cboardRepository.deleteById(cboardid);
        } catch (Exception e){
            log.error("id가 없습니다. -id: {} , -err:{}" , cboardid, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        // 삭제 이후 해당 페이지 조회
        Pageable pageable = PageRequest.of(0, 10, Sort.by("boardTime").descending()); // 예시로 페이지 1, 사이즈 10으로 설정
        Page<Cboard> entityList = cboardRepository.findAll(pageable);

        List<Cboard> cboardList = entityList.getContent();
        List<CboardDetailResponseDTO> dtoList = cboardList.stream()
                .map(board -> new CboardDetailResponseDTO(board))
                .collect(Collectors.toList());

        return CboardListResponseDTO.builder()
                .count(dtoList.size())
                .pageInfo(new PageResponseDTO(entityList))
                .board(dtoList)
                .build();
    }

    public CboardListResponseDTO update(CboardModifyrequestDTO dto, final String uploadedFilePath) {

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

//        if(dto.getHashTags() == null) {
//
//            // 기존의 해시태그 삭제
//            List<HashTag> existingTags = hashTagRepository.findByCboard(cboardEntity);
//            hashTagRepository.deleteAll(existingTags);
//            cboardEntity.getHashTags().clear();
//        }


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

        // 이전 페이지 조회
        Pageable pageable = PageRequest.of(0, 10, Sort.by("boardTime").descending()); // 예시로 페이지 1, 사이즈 10으로 설정
        Page<Cboard> entityList = cboardRepository.findAll(pageable);

        List<Cboard> cboardList = entityList.getContent();
        List<CboardDetailResponseDTO> dtoList = cboardList.stream()
                .map(board -> new CboardDetailResponseDTO(board))
                .collect(Collectors.toList());

        return CboardListResponseDTO.builder()
                .count(dtoList.size())
                .pageInfo(new PageResponseDTO(entityList))
                .board(dtoList)
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
