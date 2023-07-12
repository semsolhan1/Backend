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

    public RecontentListResponseDTO contentretrieve(Recontent cboard, RecontentPageDTO dto) {

        Pageable pageable = PageRequest.of(
                dto.getRepage()-1,
                dto.getResize(),
                Sort.by("recontentTime").descending()
        );

        Page<Recontent> entityList;
        if (cboard != null) {
            entityList = recontentRepository.findByCboardId(cboard.getCboard().getCboardId(), pageable);
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


    public RecontentListResponseDTO create(RecontentCreateRequestDTO dto) {
        return null;
    }
}
