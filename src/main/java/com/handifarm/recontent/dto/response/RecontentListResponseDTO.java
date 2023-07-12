package com.handifarm.recontent.dto.response;

import com.handifarm.recontent.dto.page.RecontentPageResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class RecontentListResponseDTO {

    private String error;

    private int count;

    private RecontentPageResponseDTO pageInfo;

    private List<RecontentDetailResponseDTO> board;

    private RecontentDetailResponseDTO previousCboard;

}
