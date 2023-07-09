package com.handifarm.api.board.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter @Getter
@ToString @EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
@Builder
public class boardModifyDTO {

  @NotBlank
  @Size(min = 1, max = 20)
  private String title;

  private String content;
  @NotNull
  @Builder.Default
  private long boardNo = 0L;


}
