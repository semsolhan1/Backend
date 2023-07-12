package com.handifarm.api.board.dto.response;

import lombok.*;

@Setter @Getter @ToString
@EqualsAndHashCode @NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardListResponseDTO {


    private String error;

    private int postCount;

    private List<BoardResponseDetailDTO> postList;

    private BoardPageResponseDTO pageInfo;
}
}
