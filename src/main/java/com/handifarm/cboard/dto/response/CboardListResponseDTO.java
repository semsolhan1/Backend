package com.handifarm.cboard.dto.response;

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

    private List<CboardDetailResponseDTO> board;

}
