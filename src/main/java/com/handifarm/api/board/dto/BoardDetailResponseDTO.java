package com.handifarm.api.board.dto;

import com.handifarm.api.board.entity.Board;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Setter @Getter
@ToString @EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
@Builder
public class BoardDetailResponseDTO {

  private String author;

  private String title;
  private String content;

  @JsonFormat(pattern = "yyyy/MM/dd")
  private LocalDateTime regDate;

  private String category;

  public BoardDetailResponseDTO(Board board) {
    this.author = board.getUserId();
    this.title = board.getTitle();
    this.content = board.getContent();
    this.regDate = board.getCreateDate();
    this.category = String.valueOf(board.getCategory());
  }


}
