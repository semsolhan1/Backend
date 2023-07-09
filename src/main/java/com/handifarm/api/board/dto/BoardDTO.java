package com.handifarm.api.board.dto;

import lombok.*;

@Getter @Setter
@ToString @EqualsAndHashCode
@AllArgsConstructor
@Builder
public class BoardDTO {

  private int page;
  private int size;

  public BoardDTO() {

    this.page = 1;
    this.size = 10;

  }
}
