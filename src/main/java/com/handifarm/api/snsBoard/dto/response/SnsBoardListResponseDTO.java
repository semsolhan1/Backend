package com.handifarm.api.snsBoard.dto.response;

import com.handifarm.api.util.page.PageResponseDTO;
import lombok.*;

import java.util.List;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SnsBoardListResponseDTO {

    private int count;
    private PageResponseDTO<?> pageInfo;
    private List<SnsBoardResponseDTO> snsList;

}
