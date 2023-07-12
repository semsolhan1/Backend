package com.handifarm.api.board.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter @ToString
@EqualsAndHashCode @NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardListResponseDTO {

    private List<BoardDetailResponseDTO> postList;

    private int postCount;

    private BoardPageResponseDTO pageInfo;

}
