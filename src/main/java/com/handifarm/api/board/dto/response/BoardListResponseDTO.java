package com.handifarm.api.board.dto.response;

import com.handifarm.api.util.page.PageResponseDTO;
import lombok.*;

import java.util.List;

@Getter @Setter @ToString
@EqualsAndHashCode @AllArgsConstructor
@Builder
public class BoardListResponseDTO {

    private int postCount;

    private int totalPages;

    private PageResponseDTO pageInfo;

    private List<BoardDetailResponseDTO> boards;

}

