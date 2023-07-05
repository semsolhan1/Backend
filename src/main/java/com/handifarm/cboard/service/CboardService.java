package com.handifarm.cboard.service;

import com.handifarm.cboard.dto.request.CboardCreateRequestDTO;
import com.handifarm.cboard.dto.request.CboardModifyrequestDTO;
import com.handifarm.cboard.dto.response.CboardDetailResponseDTO;
import com.handifarm.cboard.dto.response.CboardListResponseDTO;
import com.handifarm.cboard.entity.Cboard;
import com.handifarm.cboard.repository.CboardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${upload.path}")
    private String uploadRootPath;

    public CboardListResponseDTO retrieve() {

        List<Cboard> entityList = cboardRepository.findAll();

        List<CboardDetailResponseDTO> dtoList = entityList.stream()
                .map(board -> new CboardDetailResponseDTO(board))
                .collect(Collectors.toList());
        return CboardListResponseDTO.builder()
                .board(dtoList)
                .build();

    }

    public CboardListResponseDTO create(CboardCreateRequestDTO dto, final String uploadedFilePath)
            throws RuntimeException, IllegalStateException{


        Cboard cboard = dto.toEntity(uploadedFilePath);

        cboardRepository.save(cboard);

        return retrieve();
    }

    public CboardListResponseDTO delete(String cboardid) {

        try {
            cboardRepository.deleteById(cboardid);
        } catch (Exception e){
            log.error("id가 없습니다. -id: {} , -err:{}" , cboardid, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return  retrieve();
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

        cboardRepository.save(cboardEntity);

        return retrieve();
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
