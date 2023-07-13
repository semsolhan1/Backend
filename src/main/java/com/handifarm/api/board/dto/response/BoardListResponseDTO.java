package com.handifarm.api.board.dto.response;

import com.handifarm.api.board.entity.Board;
import lombok.*;

import java.util.List;

@Getter @Setter @ToString
@EqualsAndHashCode @NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardListResponseDTO {

    private List<BoardDetailResponseDTO> postList;

    private int postCount;

    private BoardPageResponseDTO pageInfo;


//    public BoardListResponseDTO(List<Board> boardList) {
//        this.postList =
//    }
}
