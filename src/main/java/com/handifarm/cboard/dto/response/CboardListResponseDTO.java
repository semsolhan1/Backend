package com.handifarm.cboard.dto.response;

import com.handifarm.cboard.dto.page.PageResponseDTO;
import lombok.*;

import java.util.List;


@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class CboardListResponseDTO {

    private String error;

    private int count;

    private PageResponseDTO pageInfo;

    private List<CboardDetailResponseDTO> board;

    private CboardDetailResponseDTO previousCboard;

}
