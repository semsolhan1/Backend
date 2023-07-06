package com.handifarm.cboard.dto.request;

import com.handifarm.cboard.entity.Cboard;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    private String content;

    private List<String> hashTags;

    public Cboard toEntity(String uploadedFilePath){
        return Cboard.builder()
                .title(this.title)
                .content(this.content)
                .fileUp(uploadedFilePath)
                .build();
    }


}
