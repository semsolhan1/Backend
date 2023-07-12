package com.handifarm.api.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.handifarm.api.board.entity.Board;
import jdk.jfr.Category;
import lombok.*;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@EqualsAndHashCode @NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardDetailResponseDTO {

    private Long boardNo;
    private String userId;
    private Category category;
    private String title;
    private String content;
    private int views;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime createDate;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime updateDate;

    public BoardDetailResponseDTO(Board board) {
        this.boardNo = board.getBoardNo();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createDate = board.getCreateDate();
        this.updateDate = board.getUpdateDate();
    }
}
