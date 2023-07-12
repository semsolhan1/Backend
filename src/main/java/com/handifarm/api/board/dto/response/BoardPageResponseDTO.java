package com.handifarm.api.board.dto.response;

import lombok.*;

@Setter @Getter @ToString
@EqualsAndHashCode @NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardPageResponseDTO {

    private int startPage;

    private int endPage;

    private int currentPage;

    private boolean prev;
    private boolean next;

    private int totalPage;


    private static final int PAGE_COUNT = 10;


}
