package com.handifarm.cboard.service;

import com.handifarm.cboard.dto.request.CboardCreateRequestDTO;
import com.handifarm.cboard.dto.request.CboardModifyrequestDTO;
import com.handifarm.cboard.dto.response.CboardDetailResponseDTO;
import com.handifarm.cboard.dto.response.CboardListResponseDTO;
import com.handifarm.cboard.entity.Cboard;
import com.handifarm.cboard.repository.CboardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CboardService {

    private final CboardRepository cboardRepository;

    public CboardListResponseDTO retrieve() {

        List<Cboard> entityList = cboardRepository.findAll();

        List<CboardDetailResponseDTO> dtoList = entityList.stream()
                .map(board -> new CboardDetailResponseDTO(board))
                .collect(Collectors.toList());
        return CboardListResponseDTO.builder()
                .board(dtoList)
                .build();

    }

    public CboardListResponseDTO create(CboardCreateRequestDTO dto)
            throws RuntimeException, IllegalStateException{


        Cboard cboard = dto.toEntity();

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

    public CboardListResponseDTO update(CboardModifyrequestDTO dto) {

        Cboard cboardEntity = getCboard(dto.getId());

        cboardEntity.setTitle(dto.getTitle());

        cboardRepository.save(cboardEntity);

        return retrieve();
    }

    private Cboard getCboard(String id) {

        log.info("id 체크: {}" , id);

        Cboard cboardEntity = cboardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + "번 게시글은 존재하지 않습니다."));
        return cboardEntity;
    }
}
