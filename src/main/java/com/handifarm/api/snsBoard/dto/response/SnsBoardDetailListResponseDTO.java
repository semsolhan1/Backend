package com.handifarm.api.snsBoard.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SnsBoardDetailListResponseDTO {

    private long snsNo;
    private String profileImg;
    private List<SnsBoardResponseDTO> snsList;

}
