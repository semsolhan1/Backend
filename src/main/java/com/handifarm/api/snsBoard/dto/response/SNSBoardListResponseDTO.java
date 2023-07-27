package com.handifarm.api.snsBoard.dto.response;

import com.handifarm.cboard.dto.page.PageResponseDTO;
import lombok.*;

import java.util.List;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SNSBoardListResponseDTO {

    private int count;
    private PageResponseDTO<?> pageInfo;
    private List<SNSBoardDetailResponseDTO> snsList;

}
