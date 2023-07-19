package com.handifarm.recontent.service;

import com.handifarm.cboard.entity.Cboard;
import com.handifarm.cboard.repository.CboardRepository;
import com.handifarm.recontent.dto.page.RecontentPageDTO;
import com.handifarm.recontent.dto.page.RecontentPageResponseDTO;
import com.handifarm.recontent.dto.request.RecontentCreateRequestDTO;
import com.handifarm.recontent.dto.request.RecontentModifyRequestDTO;
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
                Sort.by("recontentOrder").ascending()
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
                Sort.by("recontentOrder").ascending()
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

    public RecontentListResponseDTO delete(String cboardId, int recontentOrder, RecontentModifyRequestDTO dto, RecontentPageDTO page) {

        Recontent recontent = recontentRepository.findByCboard_CboardIdAndRecontentOrder(cboardId, recontentOrder)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        if (!recontent.getCboard().getCboardId().equals(cboardId)) {
            throw new IllegalStateException("해당 댓글을 삭제할 권한이 없습니다.");
        }

        if (!recontent.getRewriter().equals(dto.getRewriter())) {
            throw new IllegalStateException("댓글 작성자와 일치하지 않아 삭제할 수 없습니다.");
        }

        recontentRepository.delete(recontent);

        // 댓글 목록을 조회
        Pageable pageable = PageRequest.of(page.getRepage() - 1, page.getResize(), Sort.by("recontentOrder").ascending());
        Page<Recontent> entityList = recontentRepository.findByCboard_CboardId(cboardId, pageable);
        List<Recontent> recontentList = entityList.getContent();

        List<RecontentDetailResponseDTO> recontentDTOList = recontentList.stream()
                .map(recontents -> new RecontentDetailResponseDTO(recontents))
                .collect(Collectors.toList());

        return RecontentListResponseDTO.builder()
                .count(recontentDTOList.size())
                .pageInfo(new RecontentPageResponseDTO(entityList))
                .board(recontentDTOList)
                .build();
    }

    public RecontentListResponseDTO update(String cboardId, int recontentOrder, RecontentModifyRequestDTO dto, RecontentPageDTO page) {
        Recontent recontent = recontentRepository.findByCboard_CboardIdAndRecontentOrder(cboardId, recontentOrder)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        if (!recontent.getCboard().getCboardId().equals(cboardId)) {
            throw new IllegalStateException("해당 댓글을 수정할 권한이 없습니다.");
        }

        if (!recontent.getRewriter().equals(dto.getRewriter())) {
            throw new IllegalStateException("댓글 작성자와 일치하지 않아 수정할 수 없습니다.");
        }

        recontent.setRecontent(dto.getRecontent());

        Recontent modifiedRecontent = recontentRepository.save(recontent);

        // 댓글 목록을 조회
        Pageable pageable = PageRequest.of(page.getRepage() - 1, page.getResize(), Sort.by("recontentOrder").ascending());
        Page<Recontent> entityList = recontentRepository.findByCboard_CboardId(cboardId, pageable);
        List<Recontent> recontentList = entityList.getContent();

        List<RecontentDetailResponseDTO> recontentDTOList = recontentList.stream()
                .map(recontents -> new RecontentDetailResponseDTO(recontents))
                .collect(Collectors.toList());

        return RecontentListResponseDTO.builder()
                .count(recontentDTOList.size())
                .pageInfo(new RecontentPageResponseDTO(entityList))
                .board(recontentDTOList)
                .build();
    }
}
