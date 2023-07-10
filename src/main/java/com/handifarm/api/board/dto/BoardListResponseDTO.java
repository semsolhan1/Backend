package com.handifarm.api.board.dto;

import lombok.*;

import java.util.List;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardListResponseDTO {

  private int count;
  private PageResponseDTO pageInfo;
  private List<BoardDetailResponseDTO> boards;

}
