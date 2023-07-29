package com.handifarm.api.snsBoard.dto.response;

import com.handifarm.api.util.page.PageResponseDTO;
import lombok.*;

import java.util.List;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SNSBoardListResponseDTO {

    private int count;
    private PageResponseDTO<?> pageInfo;
    private List<SNSBoardResponseDTO> snsList;
    private boolean hasNextPage; // 페이징 처리 정보

}
