package com.handifarm.recontent.service;

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

    public RecontentListResponseDTO contentretrieve(String cboardId, RecontentPageDTO dto) {

        Pageable pageable = PageRequest.of(
                dto.getRepage()-1,
                dto.getResize(),
                Sort.by("recontentTime").descending()
        );

        Page<Recontent> entityList;
        if (cboardId != null) {
            entityList = recontentRepository.findByCboard_CboardId(cboardId, pageable);
        } else {
            throw new IllegalArgumentException("cboard cannot be null.");
        }

        List<Recontent> recontentList = entityList.getContent();

        List<RecontentDetailResponseDTO> dtoList = recontentList.stream()
                .map(reboard -> new RecontentDetailResponseDTO(reboard))
                .collect(Collectors.toList());
        return RecontentListResponseDTO.builder()
                .count(dtoList.size())
                .pageInfo(new RecontentPageResponseDTO(entityList))
                .board(dtoList)
                .build();
    }


    public RecontentListResponseDTO create(RecontentCreateRequestDTO dto, RecontentPageDTO pageDTO)
            throws RuntimeException, IllegalStateException{

        Pageable pageable = PageRequest.of(
                pageDTO.getRepage()-1,
                pageDTO.getResize(),
                Sort.by("recontentTime").descending()
        );

        Page<Recontent> entityList = recontentRepository.findAll(pageable);


        Recontent recontentEntity = dto.toEntity();
        recontentRepository.save(recontentEntity);


        // 댓글 생성 이후에 댓글 목록을 조회
        List<Recontent> recontentList = (List<Recontent>) contentretrieve(recontentEntity.getCboard().getCboardId(), new RecontentPageDTO());

        List<RecontentDetailResponseDTO> recontentDTOList = recontentList.stream()
                .map(content -> new RecontentDetailResponseDTO(content))
                .collect(Collectors.toList());

        return RecontentListResponseDTO.builder()
                .count(recontentDTOList.size())
                .pageInfo(new RecontentPageResponseDTO(entityList))
                .board(recontentDTOList)
                .build();
    }
}
