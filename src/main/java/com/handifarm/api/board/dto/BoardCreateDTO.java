package com.handifarm.api.board.dto;

import com.handifarm.api.board.entity.Board;
import com.handifarm.api.board.entity.Category;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardCreateDTO {

  @NotBlank
  @Size(min = 2, max = 5)
  private String userId;

  @NotBlank
  @Size(min = 1, max = 20)
  private String title;

  private String content;

  private String category;

  public Board toEntity() {
    return Board.builder()
            .userId(this.userId)
            .content(this.content)
            .title(this.title)
            .category(Category.valueOf(this.category))
            .build();
  }

}
