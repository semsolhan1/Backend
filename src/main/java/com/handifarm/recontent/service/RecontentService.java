package com.handifarm.recontent.service;

import com.handifarm.cboard.entity.Cboard;
import com.handifarm.cboard.repository.CboardRepository;
import com.handifarm.recontent.dto.page.RecontentPageDTO;
import com.handifarm.recontent.dto.page.RecontentPageResponseDTO;
import com.handifarm.recontent.dto.request.RecontentCreateRequestDTO;
import com.handifarm.recontent.dto.response.RecontentDetailResponseDTO;
import com.handifarm.recontent.dto.response.RecontentListResponseDTO;
import com.handifarm.recontent.entity.Recontent;
import com.handifarm.recontent.repository.RecontentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RecontentService {


    private final RecontentRepository recontentRepository;

    private final CboardRepository cboardRepository;

    public RecontentListResponseDTO contentretrieve(String cboardId, RecontentPageDTO dto) {
        Pageable pageable = PageRequest.of(
                dto.getRepage() - 1,
                dto.getResize(),
                Sort.by("recontentTime").descending()
        );

        Page<Recontent> entityList = recontentRepository.findByCboard_CboardId(cboardId, pageable);
        List<Recontent> recontentList = entityList.getContent();

        List<RecontentDetailResponseDTO> dtoList = recontentList.stream()
                .map(recontent -> new RecontentDetailResponseDTO(recontent))
                .collect(Collectors.toList());

        return RecontentListResponseDTO.builder()
                .count(dtoList.size())
                .pageInfo(new RecontentPageResponseDTO(entityList))
                .board(dtoList)
                .build();
    }


    public RecontentListResponseDTO create(String cboardId, RecontentCreateRequestDTO dto, RecontentPageDTO pageDTO)
            throws RuntimeException, IllegalStateException {
        Cboard cboard = cboardRepository.findById(cboardId).orElseThrow();

        Recontent recontentEntity = dto.toEntity(cboard);
        recontentRepository.save(recontentEntity);

        // 댓글 목록을 조회
        Pageable pageable = PageRequest.of(
                pageDTO.getRepage() - 1,
                pageDTO.getResize(),
                Sort.by("recontentTime").descending()
        );
        Page<Recontent> entityList = recontentRepository.findByCboard_CboardId(cboardId, pageable);
        List<Recontent> recontentList = entityList.getContent();

        List<RecontentDetailResponseDTO> recontentDTOList = recontentList.stream()
                .map(recontent -> new RecontentDetailResponseDTO(recontent))
                .collect(Collectors.toList());

        return RecontentListResponseDTO.builder()
                .count(recontentDTOList.size())
                .pageInfo(new RecontentPageResponseDTO(entityList))
                .board(recontentDTOList)
                .build();
    }
}
