package com.handifarm.cboard.dto.request;

import com.handifarm.cboard.entity.Cboard;
import com.handifarm.recontent.entity.Recontent;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CboardCreateRequestDTO {

    @NotBlank
    @Size(min = 2, max = 20)
    private String title;

    @NotBlank
    private String writer;

    @NotBlank
    private String content;

    private List<String> hashTags;

    private List<Recontent> recontents;

    private LocalDateTime boardTime;

    public Cboard toEntity(String uploadedFilePath){
        return Cboard.builder()
                .title(this.title)
                .writer(this.writer)
                .content(this.content)
                .fileUp(uploadedFilePath)
                .boardTime(this.boardTime)
                .build();
    }


}
