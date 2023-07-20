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
    private String writer;

    @NotBlank
    private String content;

    private List<String> hashTags;

    private List<String> itemImgs;

    private List<Recontent> recontents;

    private LocalDateTime boardTime;

    public Cboard toEntity(){
        return Cboard.builder()
                .writer(this.writer)
                .content(this.content)
                .boardTime(this.boardTime)
                .build();
    }


}
